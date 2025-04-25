package com.example.infra.repository.impl;

import com.example.core.domain.model.Conversation;
import com.example.core.domain.repository.ConversationRepository;
import com.example.infra.repository.JpaConversationRepository;
import com.example.infra.repository.mapper.ConversationMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/** Implementação do repositório de conversas usando JPA */
@Repository
public class ConversationRepositoryImpl implements ConversationRepository {

  private final JpaConversationRepository jpaRepository;
  private final ConversationMapper mapper;

  public ConversationRepositoryImpl(
      JpaConversationRepository jpaRepository, ConversationMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Conversation save(Conversation conversation) {
    var entity = mapper.toEntity(conversation);
    var savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Conversation> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Conversation> findByClientId(String clientId) {
    return jpaRepository.findByClientId(clientId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Conversation> findActiveByClientId(String clientId) {
    return jpaRepository.findActiveByClientId(clientId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
