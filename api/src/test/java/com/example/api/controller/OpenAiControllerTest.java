package com.example.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.api.dto.MessageRequestDto;
import com.example.api.dto.MessageResponseDto;
import com.example.api.mapper.ApiMapper;
import com.example.core.domain.model.Conversation;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import com.example.core.domain.service.OpenAiService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/** Testes unitários para o controller OpenAI */
@ExtendWith(MockitoExtension.class)
public class OpenAiControllerTest {

  @Mock private OpenAiService openAiService;

  @Mock private ApiMapper apiMapper;

  @InjectMocks private OpenAiController controller;

  @Test
  void testAsk() {
    // Arrange
    MessageRequestDto requestDto = new MessageRequestDto();
    requestDto.setContent("Test question");

    Message message = Message.userMessage("Test question");
    OpenAiResponse openAiResponse =
        OpenAiResponse.builder()
            .id("test-id")
            .model("gpt-4")
            .message(Message.assistantMessage("Test response"))
            .build();

    MessageResponseDto responseDto =
        MessageResponseDto.builder().id("test-id").model("gpt-4").content("Test response").build();

    when(apiMapper.toMessage(requestDto)).thenReturn(message);
    when(openAiService.sendMessage(message)).thenReturn(Mono.just(openAiResponse));
    when(apiMapper.toResponseDto(openAiResponse)).thenReturn(responseDto);

    // Act
    Mono<ResponseEntity<MessageResponseDto>> responseMono = controller.ask(requestDto);

    // Assert
    StepVerifier.create(responseMono)
        .assertNext(
            response -> {
              assertNotNull(response);
              assertEquals(HttpStatus.OK, response.getStatusCode());
              assertEquals(responseDto, response.getBody());
            })
        .expectComplete()
        .verify();

    verify(apiMapper, times(1)).toMessage(requestDto);
    verify(openAiService, times(1)).sendMessage(message);
    verify(apiMapper, times(1)).toResponseDto(openAiResponse);
  }

  @Test
  void testCreateConversation() {
    // Arrange
    String clientId = "test-client";
    UUID conversationId = UUID.randomUUID();
    Conversation conversation =
        Conversation.builder().id(conversationId).clientId(clientId).build();

    when(openAiService.createConversation(clientId)).thenReturn(conversation);

    // Act
    ResponseEntity<UUID> response = controller.createConversation(clientId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(conversationId, response.getBody());
    verify(openAiService, times(1)).createConversation(clientId);
  }

  @Test
  void testHealth() {
    // Act
    ResponseEntity<String> response = controller.health();

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Service is up and running", response.getBody());
  }
}
