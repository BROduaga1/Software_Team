package com.example.gymcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GymCrmApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymCrmApplication.class, args);
    }

}
