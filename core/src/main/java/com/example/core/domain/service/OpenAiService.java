package com.example.core.domain.service;

import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

/** Interface para o serviço de integração com a API da OpenAI */
public interface OpenAiService {

  /**
   * Envia uma mensagem para a API da OpenAI com o contexto da conversa
   *
   * @param conversation Conversa atual com histórico de mensagens
   * @param message Nova mensagem a ser enviada
   * @return Resposta da API da OpenAI
   */
  Mono<OpenAiResponse> sendMessage(Conversation conversation, Message message);

  /**
   * Envia uma mensagem para a API da OpenAI com o contexto padrão
   *
   * @param message Mensagem a ser enviada
   * @return Resposta da API da OpenAI
   */
  Mono<OpenAiResponse> sendMessage(Message message);

  /**
   * Cria uma nova conversa com o contexto padrão
   *
   * @param clientId Identificador do cliente
   * @return Nova conversa
   */
  Conversation createConversation(String clientId);

  /**
   * Recupera uma conversa
   *
   * @param conversationId Identificador da conversa
   * @return Conversa
   */
  Conversation retrieveConversation(UUID conversationId);

  /**
   * Adiciona uma mensagem a uma conversa existente
   *
   * @param conversation Conversa existente
   * @param message Mensagem a ser adicionada
   * @return Conversa atualizada
   */
  Conversation addMessageToConversation(Conversation conversation, Message message);

  /**
   * Obtém o contexto padrão do sistema
   *
   * @return Lista de mensagens que compõem o contexto padrão
   */
  List<Message> getDefaultContext();
}
