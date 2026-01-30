package com.tongbora.pipelineservice.client.jsonPlaceholder;

import com.tongbora.pipelineservice.client.jsonPlaceholder.dto.JsonPlaceholderResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface JsonPlaceholderClient {


    @GetExchange("/users")
    List<JsonPlaceholderResponse> getAllUsers();

}
