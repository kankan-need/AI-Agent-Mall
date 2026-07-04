package com.learn.mall.agent.config;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class AgentHttpClientConfig {

    @Bean
    public RestClientCustomizer agentRestClientCustomizer() {
        return restClientBuilder -> {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(30_000);
            factory.setReadTimeout(120_000);
            restClientBuilder.requestFactory(factory);
        };
    }
}
