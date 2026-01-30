package com.tongbora.pipelineservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
@RequiredArgsConstructor
public class HttpInterfaceFactory {


    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private final WebClient.Builder loadBalancedWebClientBuilder;

    // Method for create normal WebClient without load balancing
    public <T> T createNomalClient(String baseUrl, Class<T> interfaceClass){
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        return createClient(webClient, interfaceClass);
    }

    // Method for create WebClient with load balancing and OAuth2 client credentials flow
    public <T> T createLoadBalancedClient(String baseUrl, Class<T> interfaceClass) {

        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                oAuth2AuthorizedClientManager
        );
        // this id should match the client registration id in application.yml
        oauth2.setDefaultClientRegistrationId("itp-standard");

        /**
         * This webClient is use for load balanced call
         */

        WebClient webClient = loadBalancedWebClientBuilder
                .baseUrl(baseUrl)
                // when we use .appy it mean that WebClient will have the job to do client credential flow with oauth2 (when the request have security)
                .apply(oauth2.oauth2Configuration())
                .build();

        /**
         * This webClient is use for non-load balanced call
         */

//        WebClient webClient = WebClient.builder()
//                .baseUrl(baseUrl)
//                // when we use .appy it mean that WebClient will have the job to do client credential flow with oauth2 (when the request have security)
//                .apply(oauth2.oauth2Configuration())
//                .build();


        return createClient(webClient, interfaceClass);
    }

    // Main method to create client from WebClient and interface class
    public <T> T createClient(WebClient webClient, Class<T> interfaceClass) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(interfaceClass);
    }


}
