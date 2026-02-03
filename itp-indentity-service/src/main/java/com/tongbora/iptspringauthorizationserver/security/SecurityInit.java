package com.tongbora.iptspringauthorizationserver.security;

import com.tongbora.iptspringauthorizationserver.domain.Role;
import com.tongbora.iptspringauthorizationserver.domain.User;
import com.tongbora.iptspringauthorizationserver.feature.oauth2.JpaRegisteredClientRepository;
import com.tongbora.iptspringauthorizationserver.feature.role.RoleRepository;
import com.tongbora.iptspringauthorizationserver.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityInit {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setUsername("tongbora");
            user.setPassword(passwordEncoder.encode("qwer"));
            user.setEmail("tongbora@gmail.com");
            user.setDob(LocalDate.of(1998, 9, 9));
            user.setProfileImage("https://media.licdn.com/dms/image/v2/D5603AQENfXtIwzZblA/profile-displayphoto-scale_400_400/B56ZurICgOIoAg-/0/1768102575297?e=1771459200&v=beta&t=2zdJTcal1JslegqY7iJjcSHXkXDfH5TaQS24p4hvHSk");
            user.setCoverImage("https://images.lifestyleasia.com/wp-content/uploads/sites/6/2025/09/08112013/untitled-design-2024-11-12t170849-678-1600x900.jpeg");
            user.setGender("Male");
//            user.setProfileImage("default_profile.jpg");
//            user.setCoverImage("default_cover.jpg");
            user.setFamilyName("Tong");
            user.setGivenName("Bora");
            user.setPhoneNumber("077815896");
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setIsEnabled(true);

            // Assign role to user
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ADMIN"));
            roles.add(roleRepository.findByName("USER"));
            user.setRoles(roles);

            userRepository.save(user);
            log.info("User has been saved: {}", user.getId());
        }
    }

    @PostConstruct
    void initOAuth2() {

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofDays(3))
                .reuseRefreshTokens(false) // refresh token rotation
                .refreshTokenTimeToLive(Duration.ofDays(5))
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(false)
                .build();

        var itpStandard = RegisteredClient.withId("itp-standard")
                .clientId("itp-standard")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    uris.add("http://localhost:9090/login/oauth2/code/itp-standard");
                    uris.add("http://localhost:9999/login/oauth2/code/itp-standard");
                    uris.add("http://localhost:8080");
                    uris.add("https://cstad.edu.kh/");
                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:9090");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)//TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                    grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        var itpFrontBff = RegisteredClient.withId("itp-frontbff")
                .clientId("itp-frontbff")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    uris.add("http://localhost:9990/login/oauth2/code/itp-frontbff");
                    uris.add("http://localhost:9990");
                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:9990");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)//TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        // Client for front-bff-admin
        var itpFrontBffForAdmin = RegisteredClient.withId("itp-frontbff-for-admin")
                .clientId("itp-frontbff-for-admin")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    uris.add("http://localhost:9991/login/oauth2/code/itp-frontbff-for-admin");
                    uris.add("http://localhost:9991");
                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:9991");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)//TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        RegisteredClient registeredClient = jpaRegisteredClientRepository.findByClientId("itp-standard");
        RegisteredClient registeredClient2 = jpaRegisteredClientRepository.findByClientId("itp-frontbff");
        RegisteredClient registeredClient3 = jpaRegisteredClientRepository.findByClientId("itp-frontbff-for-admin");
        log.info("Registered client: {}", registeredClient);
        log.info("Registered client: {}", registeredClient2);
        log.info("Registered client: {}", registeredClient3);

        if (registeredClient == null) {
            jpaRegisteredClientRepository.save(itpStandard);
        }
        if (registeredClient == null) {
            jpaRegisteredClientRepository.save(itpFrontBff);
        }
        if (registeredClient == null) {
            jpaRegisteredClientRepository.save(itpFrontBffForAdmin);
        }

    }
}