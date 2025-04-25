package com.example.core.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;

/** Representa uma conversa com a API da OpenAI */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
  private UUID id;
  private String clientId;
  private List<Message> messages;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder.Default private boolean active = true;
}
