package com.tongbora.pipelineservice.client.account;

import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange("/api/v1/accounts")
public interface AccountClient {

    @GetExchange
    AccountResponse getAccountInfo();

}
