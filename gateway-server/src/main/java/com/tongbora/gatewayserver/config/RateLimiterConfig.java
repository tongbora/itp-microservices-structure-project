//package com.tongbora.gatewayserver.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//
//@Component
//public class RateLimiterConfig {
//
//    // This is the config for redis rate limiter
////    @Bean
////    KeyResolver userKeyResolver() {
////        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("apiKey"))
////                .defaultIfEmpty("anonymous");
////    }
////    @Bean
////    public KeyResolver userKeyResolver() {
////        return exchange -> {
////            String clientIp = exchange.getRequest()
////                    .getRemoteAddress()
////                    .getAddress()
////                    .getHostAddress();
////
////            return Mono.just("ip:" + clientIp);
////        };
////    }
//
//
//    @Bean
//    public KeyResolver userKeyResolver() {
//        return exchange -> {
//            String authHeader = exchange.getRequest()
//                    .getHeaders()
//                    .getFirst("Authorization");
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                try {
//                    String token = authHeader.substring(7);
//                    String subject = extractSubjectFromToken(token);
//
//                    if (subject != null && !subject.isEmpty()) {
//                        // Rate limit key will be: "user:itp-standard"
//                        return Mono.just("user:" + subject);
//                    }
//                } catch (Exception e) {
//                    // Log error but continue to fallback
//                    e.printStackTrace();
//                }
//            }
//
//            // Fallback to IP
//            String clientIp = exchange.getRequest()
//                    .getRemoteAddress()
//                    .getAddress()
//                    .getHostAddress();
//
//            return Mono.just("ip:" + clientIp);
//        };
//    }
//
//    private String extractSubjectFromToken(String token) {
//        try {
//            // Remove signature part for parsing (we don't verify)
//            int lastDotIndex = token.lastIndexOf('.');
//            String withoutSignature = token.substring(0, lastDotIndex + 1);
//
//            Claims claims = Jwts.parserBuilder()
//                    .build()
//                    .parseClaimsJwt(withoutSignature)
//                    .getBody();
//
//            return claims.getSubject(); // Returns "itp-standard"
//
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//
//
//}
