package com.cyfrahub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CyfraHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(CyfraHubApplication.class, args);
    }
}
