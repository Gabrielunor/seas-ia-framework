package com.example.infra.repository.mapper;

import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.infra.repository.entity.ConversationEntity;
import com.example.infra.repository.entity.MessageEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Mapper para conversão entre entidades JPA e objetos de domínio */
@Component
public class ConversationMapper {

  /**
   * Converte uma entidade JPA para um objeto de domínio
   *
   * @param entity Entidade JPA
   * @return Objeto de domínio
   */
  public Conversation toDomain(ConversationEntity entity) {
    if (entity == null) {
      return null;
    }

    List<Message> messages =
        entity.getMessages().stream().map(this::messageEntityToDomain).collect(Collectors.toList());

    return Conversation.builder()
        .id(entity.getId())
        .clientId(entity.getClientId())
        .messages(messages)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .active(entity.isActive())
        .build();
  }

  /**
   * Converte um objeto de domínio para uma entidade JPA
   *
   * @param domain Objeto de domínio
   * @return Entidade JPA
   */
  public ConversationEntity toEntity(Conversation domain) {
    if (domain == null) {
      return null;
    }

    ConversationEntity entity =
        ConversationEntity.builder()
            .id(domain.getId())
            .clientId(domain.getClientId())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .active(domain.isActive())
            .build();

    List<MessageEntity> messageEntities =
        domain.getMessages().stream()
            .map(message -> messageToEntity(message, entity))
            .collect(Collectors.toList());

    entity.setMessages(messageEntities);

    return entity;
  }

  /**
   * Converte uma entidade JPA de mensagem para um objeto de domínio
   *
   * @param entity Entidade JPA
   * @return Objeto de domínio
   */
  private Message messageEntityToDomain(MessageEntity entity) {
    return Message.builder()
        .id(entity.getId())
        .role(entity.getRole())
        .content(entity.getContent())
        .timestamp(entity.getTimestamp())
        .build();
  }

  /**
   * Converte um objeto de domínio de mensagem para uma entidade JPA
   *
   * @param domain Objeto de domínio
   * @param conversationEntity Entidade JPA da conversa
   * @return Entidade JPA
   */
  private MessageEntity messageToEntity(Message domain, ConversationEntity conversationEntity) {
    return MessageEntity.builder()
        .id(domain.getId())
        .role(domain.getRole())
        .content(domain.getContent())
        .timestamp(domain.getTimestamp())
        .conversation(conversationEntity)
        .build();
  }
}
