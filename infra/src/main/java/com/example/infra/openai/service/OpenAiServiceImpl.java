package com.example.infra.openai.service;

import com.example.config.properties.ContextProperties;
import com.example.config.properties.OpenAiProperties;
import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import com.example.core.domain.repository.ConversationRepository;
import com.example.core.domain.service.OpenAiService;
import com.example.infra.openai.dto.OpenAiResponseDto;
import com.example.infra.openai.mapper.OpenAiMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** Implementação do serviço de integração com a API da OpenAI */
@Service
@Slf4j
public class OpenAiServiceImpl implements OpenAiService {

  private final WebClient openAiWebClient;
  private final OpenAiProperties openAiProperties;
  private final ContextProperties contextProperties;
  private final OpenAiMapper mapper;
  private final ResourceLoader resourceLoader;
  private final ObjectMapper yamlMapper;
  private final ConversationRepository conversationRepository;

  private List<Message> defaultContext;

  public OpenAiServiceImpl(
      WebClient openAiWebClient,
      OpenAiProperties openAiProperties,
      ContextProperties contextProperties,
      OpenAiMapper mapper,
      ResourceLoader resourceLoader,
      ConversationRepository conversationRepository) {
    this.openAiWebClient = openAiWebClient;
    this.openAiProperties = openAiProperties;
    this.contextProperties = contextProperties;
    this.mapper = mapper;
    this.resourceLoader = resourceLoader;
    this.yamlMapper = new ObjectMapper(new YAMLFactory());
    this.conversationRepository = conversationRepository;
    // Carrega o contexto padrão
    this.defaultContext = loadDefaultContext();
  }

  @Override
  @Cacheable(value = "openai-responses", key = "#message.content")
  public Mono<OpenAiResponse> sendMessage(Conversation conversation, Message message) {
    List<Message> messages = new ArrayList<>(conversation.getMessages());
    messages.add(message);

    return sendToOpenAi(messages);
  }

  @Override
  @Cacheable(value = "openai-responses", key = "#message.content")
  public Mono<OpenAiResponse> sendMessage(Message message) {
    List<Message> messages = new ArrayList<>(getDefaultContext());
    messages.add(message);

    return sendToOpenAi(messages);
  }

  @Override
  public Conversation createConversation(String clientId) {
    Conversation conversation =
        Conversation.builder()
            .clientId(clientId)
            .messages(new ArrayList<>(getDefaultContext()))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();
    return conversationRepository.save(conversation);
  }

  @Override
  public Conversation retrieveConversation(UUID conversationId) {
    // TODO - Buscar as conversas do banco
    return conversationRepository.findById(conversationId).orElseThrow();
  }

  @Override
  public Conversation addMessageToConversation(Conversation conversation, Message message) {
    List<Message> messages = new ArrayList<>(conversation.getMessages());
    messages.add(message);

    conversation.setMessages(messages);
    conversation.setUpdatedAt(LocalDateTime.now());

    return conversationRepository.save(conversation);
  }

  @Override
  public List<Message> getDefaultContext() {
    return defaultContext;
  }

  /**
   * Envia uma lista de mensagens para a API da OpenAI
   *
   * @param messages Lista de mensagens
   * @return Resposta da API da OpenAI
   */
  private Mono<OpenAiResponse> sendToOpenAi(List<Message> messages) {
    var messageDtos = mapper.toMessageDtoList(messages);
    var requestDto = mapper.toRequestDto(openAiProperties.getModel(), messageDtos);

    log.debug("Enviando requisição para a API da OpenAI: {}", requestDto);

    return openAiWebClient
        .post()
        .uri("/chat/completions")
        .bodyValue(requestDto)
        .retrieve()
        .bodyToMono(OpenAiResponseDto.class)
        .doOnNext(responseDto -> log.debug("Resposta recebida da API da OpenAI: {}", responseDto))
        .map(mapper::toOpenAiResponse)
        .doOnError(e -> log.error("Erro ao enviar mensagem para a API da OpenAI", e))
        .onErrorMap(e -> new RuntimeException("Erro ao enviar mensagem para a API da OpenAI", e));
  }

  /**
   * Carrega o contexto padrão do arquivo de configuração
   *
   * @return Lista de mensagens do contexto padrão
   */
  private List<Message> loadDefaultContext() {
    try {
      Resource resource = resourceLoader.getResource(contextProperties.getFilePath());
      Map<String, Object> contextMap = yamlMapper.readValue(resource.getInputStream(), Map.class);

      List<Message> messages = new ArrayList<>();

      if (contextMap.containsKey("messages")) {
        List<Map<String, String>> messagesList =
            (List<Map<String, String>>) contextMap.get("messages");

        for (Map<String, String> messageMap : messagesList) {
          String role = messageMap.get("role");
          String content = messageMap.get("content");

          messages.add(
              Message.builder().role(role).content(content).timestamp(LocalDateTime.now()).build());
        }
      }

      return messages;
    } catch (IOException e) {
      log.warn("Não foi possível carregar o contexto padrão: {}", e.getMessage());
      log.info("Usando contexto padrão vazio");
      return Collections.emptyList();
    }
  }
}
