package com.tongbora.iptspringauthorizationserver.security;

import com.tongbora.iptspringauthorizationserver.domain.User;
import com.tongbora.iptspringauthorizationserver.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * USER AUTHENTICATION & TOKEN CUSTOMIZATION SERVICE
 *
 * This service handles two main responsibilities:
 * 1. Loading user details for authentication (login)
 * 2. Customizing JWT tokens with additional user information
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // PART 1: USER AUTHENTICATION

    /**
     * LOADS USER FOR AUTHENTICATION (Login Process)
     *
     * Called by Spring Security when user tries to log in.
     *
     * Flow:
     * 1. User submits username/password
     * 2. Spring Security calls this method with the username
     * 3. We fetch user from database
     * 4. We build all authorities (roles + permissions)
     * 5. We return UserDetails for password verification
     *
     * @param username - The username from login form
     * @return UserDetails - Contains username, password, and authorities
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Step 1: Fetch user from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Step 2: Build all authorities (roles + permissions)
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles and their associated permissions
        user.getRoles().forEach(role -> {
            authorities.add(role);                      // e.g., "ROLE_ADMIN"
            authorities.addAll(role.getPermissions());  // e.g., "read:users", "write:users"
        });

        // Step 3: Return Spring's UserDetails (Jackson can serialize this)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(!user.getAccountNonExpired())
                .accountLocked(!user.getAccountNonLocked())
                .credentialsExpired(!user.getCredentialsNonExpired())
                .disabled(!user.getIsEnabled())
                .build();
    }


    // PART 2: JWT TOKEN CUSTOMIZATION

    /**
     * CUSTOMIZES JWT TOKENS WITH USER INFORMATION
     *
     * Called by Spring Authorization Server when generating tokens.
     *
     * Flow:
     * 1. Client requests access token (after successful login)
     * 2. Spring Authorization Server prepares JWT
     * 3. This method is called to add custom claims
     * 4. We fetch full user data from database
     * 5. We add custom fields (email, name, uuid, etc.) to token
     * 6. Token is signed and returned to client
     *
     * Why fetch from database again?
     * - UserDetails (above) only has: username, password, authorities
     * - JWT needs more: email, full name, uuid, phone, profile image
     * - We need the full User entity with all fields
     *
     * @return OAuth2TokenCustomizer that adds custom claims to tokens
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {

            // Step 1: Get username from authenticated user
            String username = context.getPrincipal().getName();

            // Step 2: Fetch full user entity from database
            userRepository.findByUsername(username).ifPresent(user -> {

                // Step 3: Add custom claims to JWT token
                // These will be available in both Access Token and ID Token
                // Each .claim(key, value) adds a field to the JWT token

                context.getClaims().claim("uuid", user.getUuid());
                context.getClaims().claim("email", user.getEmail());
                context.getClaims().claim("email_verified", true);
                context.getClaims().claim("family_name", user.getFamilyName());
                context.getClaims().claim("given_name", user.getGivenName());
                context.getClaims().claim("name", user.getGivenName() + " " + user.getFamilyName());
                context.getClaims().claim("phone_number", user.getPhoneNumber());
                context.getClaims().claim("picture", user.getProfileImage());
                context.getClaims().claim("cover_image", user.getCoverImage());
            });
        };
    }
}
