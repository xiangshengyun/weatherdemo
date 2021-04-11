package com.xiangsy.demo.infrastructure.spring;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description: config http resttemplate
 *
 */
@SpringBootConfiguration
public class DemoAppConfig {
    @Autowired
    private Environment environment;

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int connectTimeOut = Integer.parseInt(environment.getProperty("http.connect.timeout"));
        int readTimeOut = Integer.parseInt(environment.getProperty("http.read.timeout"));
        factory.setConnectTimeout(connectTimeOut);
        factory.setReadTimeout(readTimeOut);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    @Bean
    public RetryTemplate retryTemplate() {
        int maxAttempt = Integer.parseInt(environment.getProperty("http.retry.maxAttempt"));
        int retryTimeInterval = Integer.parseInt(environment.getProperty("http.retry.retryTimeInterval"));

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempt);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(retryTimeInterval);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }
}
