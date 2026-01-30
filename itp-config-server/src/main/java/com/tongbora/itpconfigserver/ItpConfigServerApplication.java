package com.tongbora.itpconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ItpConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItpConfigServerApplication.class, args);
    }

}
