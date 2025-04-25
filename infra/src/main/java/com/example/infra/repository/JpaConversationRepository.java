package com.example.infra.repository;

import com.example.infra.repository.entity.ConversationEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repositório JPA para conversas */
@Repository
public interface JpaConversationRepository extends JpaRepository<ConversationEntity, UUID> {

  /**
   * Busca todas as conversas de um cliente
   *
   * @param clientId ID do cliente
   * @return Lista de conversas do cliente
   */
  List<ConversationEntity> findByClientId(String clientId);

  /**
   * Busca todas as conversas ativas de um cliente
   *
   * @param clientId ID do cliente
   * @return Lista de conversas ativas do cliente
   */
  @Query("SELECT c FROM ConversationEntity c WHERE c.clientId = :clientId AND c.active = true")
  List<ConversationEntity> findActiveByClientId(@Param("clientId") String clientId);
}
