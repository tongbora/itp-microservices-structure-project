//package com.tongbora.iptspringauthorizationserver.security;
//
//import com.tongbora.iptspringauthorizationserver.domain.User;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//@Getter
//@RequiredArgsConstructor
//public class CustomUserDetails implements UserDetails {
//
//    private final User user;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//
//        // Add roles (already implements GrantedAuthority)
//        user.getRoles().forEach(role -> {
//            authorities.add(role);  // Adds "ROLE_xxx"
//
//            // Add permissions from roles (already implements GrantedAuthority)
//            authorities.addAll(role.getPermissions());
//        });
//
//        // Add direct user permissions
//        authorities.addAll(user.getPermissions());
//
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return user.getAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return user.getAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return user.getCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return user.getIsEnabled();
//    }
//
//    // Custom getters for additional fields
////    public String getEmail() {
////        return user.getEmail();
////    }
////
////    public String getUuid() {
////        return user.getUuid();
////    }
////
////    public String getFamilyName() {
////        return user.getFamilyName();
////    }
////
////    public String getGivenName() {
////        return user.getGivenName();
////    }
////
////    public String getPhoneNumber() {
////        return user.getPhoneNumber();
////    }
////
////    public String getProfileImage() {
////        return user.getProfileImage();
////    }
//}