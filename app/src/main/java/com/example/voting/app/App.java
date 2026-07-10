package com.example.voting.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.voting")
@EnableJpaRepositories(basePackages = "com.example.voting.adapters.persistence.jpa.repository")
@EntityScan(basePackages = "com.example.voting.adapters.persistence.jpa.entity")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
