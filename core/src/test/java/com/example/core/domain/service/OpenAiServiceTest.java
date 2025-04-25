package com.example.core.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/** Testes unitários para o serviço, OpenAI */
public class OpenAiServiceTest {

  @Test
  void testCreateConversation() {
    // Arrange
    OpenAiService service = Mockito.mock(OpenAiService.class);
    String clientId = "test-client";
    Conversation expectedConversation =
        Conversation.builder()
            .id(UUID.randomUUID())
            .clientId(clientId)
            .messages(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();

    when(service.createConversation(clientId)).thenReturn(expectedConversation);

    // Act
    Conversation result = service.createConversation(clientId);

    // Assert
    assertNotNull(result);
    assertEquals(clientId, result.getClientId());
    assertTrue(result.isActive());
    verify(service, times(1)).createConversation(clientId);
  }

  @Test
  void testAddMessageToConversation() {
    // Arrange
    OpenAiService service = Mockito.mock(OpenAiService.class);
    Conversation conversation =
        Conversation.builder()
            .id(UUID.randomUUID())
            .clientId("test-client")
            .messages(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();

    Message message = Message.userMessage("Test message");

    Conversation expectedUpdatedConversation =
        Conversation.builder()
            .id(conversation.getId())
            .clientId(conversation.getClientId())
            .messages(List.of(message))
            .createdAt(conversation.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .active(conversation.isActive())
            .build();

    when(service.addMessageToConversation(conversation, message))
        .thenReturn(expectedUpdatedConversation);

    // Act
    Conversation result = service.addMessageToConversation(conversation, message);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getMessages().size());
    assertEquals(message, result.getMessages().get(0));
    verify(service, times(1)).addMessageToConversation(conversation, message);
  }

  @Test
  void testSendMessage() {
    // Arrange
    OpenAiService service = Mockito.mock(OpenAiService.class);
    Message message = Message.userMessage("Test message");

    OpenAiResponse expectedResponse =
        OpenAiResponse.builder()
            .id("test-id")
            .model("gpt-4")
            .message(Message.assistantMessage("Test response"))
            .promptTokens("10")
            .completionTokens("20")
            .totalTokens("30")
            .finishReason("stop")
            .build();

    when(service.sendMessage(message)).thenReturn(Mono.just(expectedResponse));

    // Act
    Mono<OpenAiResponse> resultMono = service.sendMessage(message);

    // Assert
    StepVerifier.create(resultMono)
        .assertNext(
            response -> {
              assertNotNull(response);
              assertEquals("test-id", response.getId());
              assertEquals("gpt-4", response.getModel());
              assertEquals("Test response", response.getMessage().getContent());
              verify(service, times(1)).sendMessage(message);
              assertNotNull(response.getMessage());
            })
        .expectComplete()
        .verify();
  }
}
