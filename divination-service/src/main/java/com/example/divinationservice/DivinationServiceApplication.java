package com.example.divinationservice;

import org.common.config.EventBusAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@Import({EventBusAutoConfiguration.class})
public class DivinationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DivinationServiceApplication.class, args);
    }

}
