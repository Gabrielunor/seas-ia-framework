package com.example.infra.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para requisição à API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRequestDto {
  private String model;
  private List<MessageDto> messages;

  @JsonProperty("max_tokens")
  private Integer maxTokens;

  private Double temperature;

  @JsonProperty("top_p")
  private Double topP;

  @JsonProperty("frequency_penalty")
  private Double frequencyPenalty;

  @JsonProperty("presence_penalty")
  private Double presencePenalty;
}
