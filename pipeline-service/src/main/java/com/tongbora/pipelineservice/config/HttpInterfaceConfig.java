package com.tongbora.pipelineservice.config;

import com.tongbora.pipelineservice.client.account.AccountClient;
import com.tongbora.pipelineservice.client.account.dto.AccountResponse;
import com.tongbora.pipelineservice.client.jsonPlaceholder.JsonPlaceholderClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpInterfaceConfig {


    @Bean
    public JsonPlaceholderClient jsonPlaceholderClient(HttpInterfaceFactory factory){
        return factory.createNomalClient("https://jsonplaceholder.typicode.com", JsonPlaceholderClient.class);
    }

    /***
     * This is example of non-load balanced client
     */
//    @Bean
//    public AccountClient accountClient(HttpInterfaceFactory factory){
//        return factory.createClient("http://localhost:20261", AccountClient.class);
//    }

    /***
     * This is example of load balanced client
     * https:{service-name}
     */
    @Bean
    public AccountClient accountClient(HttpInterfaceFactory factory){
        return factory.createLoadBalancedClient("http://account", AccountClient.class);
    }
}
