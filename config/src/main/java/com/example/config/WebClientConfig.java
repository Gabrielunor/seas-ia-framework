package com.example.config;

import com.example.config.properties.OpenAiProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/** Configuração do WebClient para integração com a API da OpenAI */
@Configuration
public class WebClientConfig {

  @Bean
  public WebClient openAiWebClient(OpenAiProperties openAiProperties) {
    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, openAiProperties.getTimeout())
            .responseTimeout(Duration.ofMillis(openAiProperties.getTimeout()))
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(
                                openAiProperties.getTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(
                                openAiProperties.getTimeout(), TimeUnit.MILLISECONDS)));

    return WebClient.builder()
        .baseUrl(openAiProperties.getUrl())
        .defaultHeader("Authorization", "Bearer " + openAiProperties.getKey())
        .defaultHeader("Content-Type", "application/json")
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }
}
