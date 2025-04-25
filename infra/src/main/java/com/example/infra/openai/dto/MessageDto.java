package com.example.infra.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para mensagem na API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
  private String role;
  private String content;
}
