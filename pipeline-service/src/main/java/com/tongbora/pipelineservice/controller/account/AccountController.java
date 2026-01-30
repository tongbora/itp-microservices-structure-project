package com.tongbora.pipelineservice.controller.account;


import com.tongbora.pipelineservice.client.account.AccountClient;
import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import com.tongbora.pipelineservice.service.AccountService;
import com.tongbora.pipelineservice.service.AccountService2;
import com.tongbora.pipelineservice.service.AccountService3;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
//    private final AccountClient accountClient;
    private final AccountService3 accountService;
//private final AccountService2 accountService;

    @GetMapping
    public AccountResponse getAccountInfo() {
        return accountService.getAccountInfo();
    }
}
