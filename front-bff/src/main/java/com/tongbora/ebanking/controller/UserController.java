package com.tongbora.ebanking.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/profile")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OidcUser user) {

        Map<String, Object> claims = user.getClaims();

        // Build response
        Map<String, Object> info = new HashMap<>();

        // Only include non-null values
        addIfPresent(info, "sub", claims.get("sub"));
        addIfPresent(info, "username", claims.getOrDefault("preferred_username", claims.get("sub")));
        addIfPresent(info, "uuid", claims.get("uuid"));
        addIfPresent(info, "email", claims.get("email"));
        addIfPresent(info, "email_verified", claims.get("email_verified"));
        addIfPresent(info, "family_name", claims.get("family_name"));
        addIfPresent(info, "given_name", claims.get("given_name"));
        addIfPresent(info, "name", claims.get("name"));
        addIfPresent(info, "phone_number", claims.get("phone_number"));
        addIfPresent(info, "picture", claims.get("picture"));
        addIfPresent(info, "cover_image", claims.get("cover_image"));

        info.put("authorities", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        System.out.println("Current User Info: " + info);

        return info;
    }

    private void addIfPresent(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}