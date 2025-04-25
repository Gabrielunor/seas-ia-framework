package com.example.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Representa uma resposta da API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponse {
  private String id;
  private String model;
  private Message message;
  private String promptTokens;
  private String completionTokens;
  private String totalTokens;
  private String finishReason;
}
