package com.tongbora.pipelineservice.service;

import com.tongbora.pipelineservice.client.account.AccountClient;
import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AccountClientService {

    private final AccountClient accountClient;
    private final CircuitBreakerRegistry circuitBreakerRegistry;


    public AccountResponse getAccountInfo() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry
                .circuitBreaker("accountCircuitBreaker");

        Supplier<AccountResponse> supplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, () -> accountClient.getAccountInfo());

        try {
            return supplier.get();
        } catch (Exception e) {
            return fallback(e);
        }
    }

    private AccountResponse fallback(Throwable throwable) {
        System.err.println("Circuit breaker fallback triggered: " + throwable.getMessage());

        return AccountResponse.builder()
                .accountId("000")
                .accountName("Fallback Account")
                .accountEmail("default@gmail.com")
                .build();
    }

}
