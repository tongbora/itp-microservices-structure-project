package com.tongbora.pipelineservice.controller.account;


import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import com.tongbora.pipelineservice.service.AccountClientService3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountClientController {
//    private final AccountClient accountClient;
    private final AccountClientService3 accountService;
//private final AccountClientService2 accountService;

    @GetMapping
    public AccountResponse getAccountInfo() {
        log.debug("Getting account info from AccountClientService3");
        return accountService.getAccountInfo();
    }
}
