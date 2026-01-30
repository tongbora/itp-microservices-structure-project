package com.tongbora.pipelineservice.client.account.dto;

import lombok.Builder;

@Builder
public record AccountResponse(
//        Map<String, Object> data
        String accountId,
        String accountName,
        String accountEmail
) {
}
