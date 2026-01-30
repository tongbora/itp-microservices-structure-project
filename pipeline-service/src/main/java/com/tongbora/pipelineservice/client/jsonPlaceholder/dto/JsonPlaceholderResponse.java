package com.tongbora.pipelineservice.client.jsonPlaceholder.dto;

public record JsonPlaceholderResponse(
        String id,
        String name,
        String username,
        String email,
        String phone,
        String website
) {
}
