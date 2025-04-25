package com.example.api.controller;

import com.example.api.dto.MessageRequestDto;
import com.example.api.dto.MessageResponseDto;
import com.example.api.mapper.ApiMapper;
import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.core.domain.service.OpenAiService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/** Controller para endpoints de integração com a API da OpenAI */
@RestController
@RequestMapping("/api/v1")
public class OpenAiController {

  private static final Logger log = LoggerFactory.getLogger(OpenAiController.class);
  private final OpenAiService openAiService;
  private final ApiMapper apiMapper;

  public OpenAiController(OpenAiService openAiService, ApiMapper apiMapper) {
    this.openAiService = openAiService;
    this.apiMapper = apiMapper;
  }

  /**
   * Endpoint para enviar uma pergunta para a API da OpenAI
   *
   * @param request DTO com a pergunta
   * @return Resposta da API da OpenAI
   */
  @PostMapping("/ask")
  public Mono<ResponseEntity<MessageResponseDto>> ask(@RequestBody MessageRequestDto request) {
    log.info("Recebida requisição para enviar pergunta: {}", request);

    Message message = apiMapper.toMessage(request);
    return openAiService
        .sendMessage(message)
        .map(
            openAiResponse -> {
              MessageResponseDto responseDto = apiMapper.toResponseDto(openAiResponse);
              return ResponseEntity.ok(responseDto);
            });
  }

  /**
   * Endpoint para criar uma conversa
   *
   * @param clientId ID do cliente
   * @return Nova conversa criada
   */
  @PostMapping("/conversations")
  public ResponseEntity<UUID> createConversation(@RequestParam String clientId) {
    log.info("Recebida requisição para criar conversa para o cliente: {}", clientId);

    Conversation conversation = openAiService.createConversation(clientId);

    return ResponseEntity.ok(conversation.getId());
  }

  /**
   * Endpoint para enviar uma mensagem numa conversa existente
   *
   * @param conversationId ID da conversa
   * @param request DTO com a mensagem
   * @return Resposta da API da OpenAI
   */
  @PostMapping("/conversations/{conversationId}/messages")
  public Mono<ResponseEntity<MessageResponseDto>> sendMessage(
      @RequestParam UUID conversationId, @RequestBody MessageRequestDto request) {

    log.info(
        "Recebida requisição para enviar mensagem na conversa {}: {}", conversationId, request);

    Conversation conversation = openAiService.retrieveConversation(conversationId);
    Message message = apiMapper.toMessage(request);
    conversation = openAiService.addMessageToConversation(conversation, message);

    return openAiService
        .sendMessage(conversation, message)
        .map(
            openAiResponse -> {
              MessageResponseDto responseDto = apiMapper.toResponseDto(openAiResponse);
              return ResponseEntity.ok(responseDto);
            });
  }

  /**
   * Endpoint para verificar a saúde do serviço
   *
   * @return Status do serviço
   */
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Service is up and running");
  }
}
