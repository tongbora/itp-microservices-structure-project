package com.tongbora.pipelineservice.service;

import com.tongbora.pipelineservice.client.account.AccountClient;
import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountClientService3 {

    private final AccountClient accountClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public AccountResponse getAccountInfo() {
        return circuitBreakerFactory.create("accountCircuitBreaker").run(
                () -> accountClient.getAccountInfo(),
                throwable -> fallback(throwable)
        );
    }

    private AccountResponse fallback(Throwable throwable) {
        // Log the error
        System.err.println("Circuit breaker fallback triggered: " + throwable.getMessage());

        // Return fallback response
        return AccountResponse.builder()
                .accountId("000")
                .accountName("Fallback Account")
                .accountEmail("default@gmail.com")
                .build();
    }
}