package com.tongbora.pipelineservice.controller.jsonPlaceholder;

import com.tongbora.pipelineservice.client.jsonPlaceholder.JsonPlaceholderClient;
import com.tongbora.pipelineservice.client.jsonPlaceholder.dto.JsonPlaceholderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class JsonPlaceholderController {

    private final JsonPlaceholderClient jsonPlaceholderClient;

    @GetMapping("/users")
    public List<JsonPlaceholderResponse> getUsers() {
        return jsonPlaceholderClient.getAllUsers();
    }
}
