package com.tongbora.pipelineservice.service;

import com.tongbora.pipelineservice.client.account.AccountClient;
import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountClientService2 {

    private final AccountClient accountClient;

    @CircuitBreaker(name = "accountCircuitBreaker", fallbackMethod = "fallback")
    public AccountResponse getAccountInfo() {
        return accountClient.getAccountInfo();
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