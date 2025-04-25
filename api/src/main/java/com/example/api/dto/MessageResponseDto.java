package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para resposta de mensagem na API REST */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
  private String id;
  private String model;
  private String content;
  private String promptTokens;
  private String completionTokens;
  private String totalTokens;
  private String finishReason;
}
