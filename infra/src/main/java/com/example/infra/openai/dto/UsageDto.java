package com.example.infra.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para informações de uso de tokens na resposta da API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageDto {
  @JsonProperty("prompt_tokens")
  private int promptTokens;

  @JsonProperty("completion_tokens")
  private int completionTokens;

  @JsonProperty("total_tokens")
  private int totalTokens;
}
