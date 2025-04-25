package com.example.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Propriedades de configuração para o contexto padrão da aplicação */
@Data
@Configuration
@ConfigurationProperties(prefix = "openai.context")
public class ContextProperties {
  private String filePath;
}
