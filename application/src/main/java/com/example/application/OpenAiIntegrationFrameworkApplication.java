package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@EntityScan(basePackages = {"com.example.core.domain", "com.example.infra.repository.entity"})
@EnableJpaRepositories(basePackages = {"com.example.infra.repository"})
public class OpenAiIntegrationFrameworkApplication {

  public static void main(String[] args) {
    SpringApplication.run(OpenAiIntegrationFrameworkApplication.class, args);
  }
}
