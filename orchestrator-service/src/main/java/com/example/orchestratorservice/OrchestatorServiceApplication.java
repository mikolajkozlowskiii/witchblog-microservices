package com.example.orchestratorservice;

import org.common.config.EventBusAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(EventBusAutoConfiguration.class)
public class OrchestatorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestatorServiceApplication.class, args);
    }

}
