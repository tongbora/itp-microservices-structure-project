package com.tongbora.accountservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class TestAccount {

    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
                "status", "success",
                "message", "Account service is running"
        );
    }
}
