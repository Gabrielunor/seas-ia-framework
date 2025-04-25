package com.example.api.mapper;

import com.example.api.dto.MessageRequestDto;
import com.example.api.dto.MessageResponseDto;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import org.springframework.stereotype.Component;

/** Mapper para conversão entre DTOs da API REST e objetos de domínio */
@Component
public class ApiMapper {

  /**
   * Converte um DTO de requisição para um objeto de domínio
   *
   * @param requestDto DTO de requisição
   * @return Objeto de domínio
   */
  public Message toMessage(MessageRequestDto requestDto) {
    return Message.userMessage(requestDto.getContent());
  }

  /**
   * Converte um objeto de domínio para um DTO de resposta
   *
   * @param response Objeto de domínio
   * @return DTO de resposta
   */
  public MessageResponseDto toResponseDto(OpenAiResponse response) {
    if (response == null) {
      return null;
    }

    return MessageResponseDto.builder()
        .id(response.getId())
        .model(response.getModel())
        .content(response.getMessage() != null ? response.getMessage().getContent() : null)
        .promptTokens(response.getPromptTokens())
        .completionTokens(response.getCompletionTokens())
        .totalTokens(response.getTotalTokens())
        .finishReason(response.getFinishReason())
        .build();
  }
}
