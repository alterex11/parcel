package com.mynt.exam.parcel.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        // Set custom timeout values (in milliseconds)
        int connectTimeout = 5000; // 5 seconds
        int readTimeout = 5000;    // 5 seconds

        // Create a ClientHttpRequestFactory with custom timeouts
        ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        ((SimpleClientHttpRequestFactory) factory).setConnectTimeout(connectTimeout);
        ((SimpleClientHttpRequestFactory) factory).setReadTimeout(readTimeout);

        // Build RestTemplate using the custom ClientHttpRequestFactory
        return restTemplateBuilder.requestFactory(() -> factory).build();
    }
}
