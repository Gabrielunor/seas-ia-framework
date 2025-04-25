package com.example.core.domain.repository;

import com.example.core.domain.model.Conversation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Interface para o repositório de conversas */
public interface ConversationRepository {

  /**
   * Salva uma conversa
   *
   * @param conversation Conversa a ser salva
   * @return Conversa salva
   */
  Conversation save(Conversation conversation);

  /**
   * Busca uma conversa pelo ID
   *
   * @param id ID da conversa
   * @return Conversa encontrada ou vazio
   */
  Optional<Conversation> findById(UUID id);

  /**
   * Busca todas as conversas de um cliente
   *
   * @param clientId ID do cliente
   * @return Lista de conversas do cliente
   */
  List<Conversation> findByClientId(String clientId);

  /**
   * Busca todas as conversas ativas de um cliente
   *
   * @param clientId ID do cliente
   * @return Lista de conversas ativas do cliente
   */
  List<Conversation> findActiveByClientId(String clientId);

  /**
   * Exclui uma conversa
   *
   * @param id ID da conversa
   */
  void deleteById(UUID id);
}
