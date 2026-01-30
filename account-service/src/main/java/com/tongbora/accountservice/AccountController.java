package com.tongbora.accountservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @GetMapping
    public Map<String, Object> getAccountInfo() {
        return Map.of(
                "accountId", "123456",
                "accountName", "Tong Bora",
                "accountEmail", "tongbora.official@gmail.com"
        );
    }
}
