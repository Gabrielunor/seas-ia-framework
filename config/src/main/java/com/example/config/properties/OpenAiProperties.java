package com.example.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Propriedades de configuração para a API da OpenAI */
@Data
@Configuration
@ConfigurationProperties(prefix = "openai.api")
public class OpenAiProperties {
  private String url;
  private String key;
  private String model;
  private int timeout;
}
