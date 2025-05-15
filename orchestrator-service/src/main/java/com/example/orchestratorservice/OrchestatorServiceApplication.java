package com.example.orchestratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrchestatorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestatorServiceApplication.class, args);
    }

}
