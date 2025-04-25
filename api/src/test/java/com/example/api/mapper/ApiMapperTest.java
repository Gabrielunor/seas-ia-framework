package com.example.api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.api.dto.MessageRequestDto;
import com.example.api.dto.MessageResponseDto;
import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Testes unitários para o mapper da API */
public class ApiMapperTest {

  private ApiMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ApiMapper();
  }

  @Test
  void testToMessage() {
    // Arrange
    MessageRequestDto requestDto = new MessageRequestDto();
    requestDto.setContent("Test message");

    // Act
    Message result = mapper.toMessage(requestDto);

    // Assert
    assertNotNull(result);
    assertEquals("user", result.getRole());
    assertEquals("Test message", result.getContent());
    assertNotNull(result.getTimestamp());
  }

  @Test
  void testToResponseDto() {
    // Arrange
    Message message = Message.assistantMessage("Test response");
    OpenAiResponse response =
        OpenAiResponse.builder()
            .id("test-id")
            .model("gpt-4")
            .message(message)
            .promptTokens("10")
            .completionTokens("20")
            .totalTokens("30")
            .finishReason("stop")
            .build();

    // Act
    MessageResponseDto result = mapper.toResponseDto(response);

    // Assert
    assertNotNull(result);
    assertEquals("test-id", result.getId());
    assertEquals("gpt-4", result.getModel());
    assertEquals("Test response", result.getContent());
    assertEquals("10", result.getPromptTokens());
    assertEquals("20", result.getCompletionTokens());
    assertEquals("30", result.getTotalTokens());
    assertEquals("stop", result.getFinishReason());
  }

  @Test
  void testToResponseDtoWithNullInput() {
    // Act
    MessageResponseDto result = mapper.toResponseDto(null);

    // Assert
    assertNull(result);
  }

  @Test
  void testToResponseDtoWithNullMessage() {
    // Arrange
    OpenAiResponse response =
        OpenAiResponse.builder()
            .id("test-id")
            .model("gpt-4")
            .message(null)
            .promptTokens("10")
            .completionTokens("20")
            .totalTokens("30")
            .finishReason("stop")
            .build();

    // Act
    MessageResponseDto result = mapper.toResponseDto(response);

    // Assert
    assertNotNull(result);
    assertEquals("test-id", result.getId());
    assertEquals("gpt-4", result.getModel());
    assertNull(result.getContent());
  }
}
