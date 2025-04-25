package com.example.infra.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para escolha na resposta da API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDto {
  private int index;
  private MessageDto message;

  @JsonProperty("finish_reason")
  private String finishReason;
}
