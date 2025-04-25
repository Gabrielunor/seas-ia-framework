package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO para requisição de mensagem na API REST */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
  private String content;
}
