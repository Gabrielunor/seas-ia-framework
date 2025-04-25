package com.example.infra.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para resposta da API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponseDto {
  private String id;
  private String object;
  private long created;
  private String model;
  private List<ChoiceDto> choices;
  private UsageDto usage;
}
