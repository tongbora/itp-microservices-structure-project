package com.tongbora.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class RateLimiterConfig {

    // This is the config for redis rate limiter
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("apiKey"))
                .defaultIfEmpty("anonymous");
    }


}
