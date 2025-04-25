package com.example.core.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Representa uma mensagem em uma conversa com a API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  private String id;
  private String role; // system, user, assistant
  private String content;
  private LocalDateTime timestamp;

  public static Message systemMessage(String content) {
    return Message.builder().role("system").content(content).timestamp(LocalDateTime.now()).build();
  }

  public static Message userMessage(String content) {
    return Message.builder().role("user").content(content).timestamp(LocalDateTime.now()).build();
  }

  public static Message assistantMessage(String content) {
    return Message.builder()
        .role("assistant")
        .content(content)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
