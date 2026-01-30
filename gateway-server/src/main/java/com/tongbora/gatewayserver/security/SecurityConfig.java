package com.tongbora.gatewayserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity  // Enable Spring Security for WebFlux applications
public class SecurityConfig {

    // For traditional servlet-based applications, define the security filter chain by implementing a SecurityFilterChain bean with HttpSecurity
    // For reactive applications, define the security filter chain by implementing a SecurityWebFilterChain bean with ServerHttpSecurity
    @Bean
    public SecurityWebFilterChain webSecurity(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> exchange
                .pathMatchers("/account/public/test").permitAll()
                .pathMatchers("/actuator/**").permitAll()  // Allow access to actuator endpoints without authentication
                .pathMatchers("/pipeline/**").permitAll()  // Allow access to pipeline endpoints without authentication
                .anyExchange().permitAll()  // Require authentication for all requests
        );
        // Disable CSRF protection, if not disable it will require CSRF token for state-changing requests
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        // Disable form login for reactive applications
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http.logout(ServerHttpSecurity.LogoutSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);


        // We use this because we want gateway act as a resource server to the authorization server
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
        );


        // Enable OAuth2 login for reactive applications
        // OAuth2 Client Login, We will use this to use form-based login from the authorization
        // gateway is an oauth2 client, so it can authenticate users via oauth2
        // We use this because we want gateway act as a client to the authorization server
        // http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
