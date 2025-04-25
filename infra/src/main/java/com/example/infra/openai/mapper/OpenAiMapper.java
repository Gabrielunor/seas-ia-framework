package com.example.infra.openai.mapper;

import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import com.example.infra.openai.dto.MessageDto;
import com.example.infra.openai.dto.OpenAiRequestDto;
import com.example.infra.openai.dto.OpenAiResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Mapper para conversão entre DTOs da API da OpenAI e objetos de domínio */
@Component
public class OpenAiMapper {

  /**
   * Converte uma lista de mensagens do domínio para DTOs
   *
   * @param messages Lista de mensagens do domínio
   * @return Lista de DTOs de mensagens
   */
  public List<MessageDto> toMessageDtoList(List<Message> messages) {
    return messages.stream().map(this::toMessageDto).collect(Collectors.toList());
  }

  /**
   * Converte uma mensagem do domínio para DTO
   *
   * @param message Mensagem do domínio
   * @return DTO de mensagem
   */
  public MessageDto toMessageDto(Message message) {
    return MessageDto.builder().role(message.getRole()).content(message.getContent()).build();
  }

  /**
   * Cria um DTO de requisição para a API da OpenAI
   *
   * @param model Modelo a ser usado
   * @param messages Lista de mensagens
   * @return DTO de requisição
   */
  public OpenAiRequestDto toRequestDto(String model, List<MessageDto> messages) {
    return OpenAiRequestDto.builder().model(model).messages(messages).build();
  }

  /**
   * Converte um DTO de resposta da API da OpenAI para um objeto de domínio
   *
   * @param responseDto DTO de resposta
   * @return Objeto de domínio
   */
  public OpenAiResponse toOpenAiResponse(OpenAiResponseDto responseDto) {
    if (responseDto == null
        || responseDto.getChoices() == null
        || responseDto.getChoices().isEmpty()) {
      return null;
    }

    var choice = responseDto.getChoices().get(0);
    var usage = responseDto.getUsage();

    Message message = null;
    if (choice.getMessage() != null) {
      message =
          Message.builder()
              .role(choice.getMessage().getRole())
              .content(choice.getMessage().getContent())
              .timestamp(LocalDateTime.now())
              .build();
    }

    return OpenAiResponse.builder()
        .id(responseDto.getId())
        .model(responseDto.getModel())
        .message(message)
        .promptTokens(String.valueOf(usage.getPromptTokens()))
        .completionTokens(String.valueOf(usage.getCompletionTokens()))
        .totalTokens(String.valueOf(usage.getTotalTokens()))
        .finishReason(choice.getFinishReason())
        .build();
  }
}
