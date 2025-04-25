package com.example.infra.openai.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.core.domain.model.Message;
import com.example.core.domain.model.OpenAiResponse;
import com.example.infra.openai.dto.ChoiceDto;
import com.example.infra.openai.dto.MessageDto;
import com.example.infra.openai.dto.OpenAiResponseDto;
import com.example.infra.openai.dto.UsageDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Testes unitários para o mapper OpenAI */
public class OpenAiMapperTest {

  private OpenAiMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new OpenAiMapper();
  }

  @Test
  void testToMessageDto() {
    // Arrange
    Message message =
        Message.builder()
            .role("user")
            .content("Test message")
            .timestamp(LocalDateTime.now())
            .build();

    // Act
    MessageDto result = mapper.toMessageDto(message);

    // Assert
    assertNotNull(result);
    assertEquals("user", result.getRole());
    assertEquals("Test message", result.getContent());
  }

  @Test
  void testToMessageDtoList() {
    // Arrange
    List<Message> messages = new ArrayList<>();
    messages.add(Message.systemMessage("System message"));
    messages.add(Message.userMessage("User message"));

    // Act
    List<MessageDto> result = mapper.toMessageDtoList(messages);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("system", result.get(0).getRole());
    assertEquals("System message", result.get(0).getContent());
    assertEquals("user", result.get(1).getRole());
    assertEquals("User message", result.get(1).getContent());
  }

  @Test
  void testToOpenAiResponse() {
    // Arrange
    MessageDto messageDto = new MessageDto("assistant", "Test response");
    ChoiceDto choiceDto = new ChoiceDto(0, messageDto, "stop");
    UsageDto usageDto = new UsageDto(10, 20, 30);

    OpenAiResponseDto responseDto = new OpenAiResponseDto();
    responseDto.setId("test-id");
    responseDto.setModel("gpt-4");
    responseDto.setChoices(List.of(choiceDto));
    responseDto.setUsage(usageDto);

    // Act
    OpenAiResponse result = mapper.toOpenAiResponse(responseDto);

    // Assert
    assertNotNull(result);
    assertEquals("test-id", result.getId());
    assertEquals("gpt-4", result.getModel());
    assertNotNull(result.getMessage());
    assertEquals("assistant", result.getMessage().getRole());
    assertEquals("Test response", result.getMessage().getContent());
    assertEquals("10", result.getPromptTokens());
    assertEquals("20", result.getCompletionTokens());
    assertEquals("30", result.getTotalTokens());
    assertEquals("stop", result.getFinishReason());
  }

  @Test
  void testToOpenAiResponseWithNullInput() {
    // Act
    OpenAiResponse result = mapper.toOpenAiResponse(null);

    // Assert
    assertNull(result);
  }

  @Test
  void testToOpenAiResponseWithEmptyChoices() {
    // Arrange
    OpenAiResponseDto responseDto = new OpenAiResponseDto();
    responseDto.setId("test-id");
    responseDto.setModel("gpt-4");
    responseDto.setChoices(new ArrayList<>());

    // Act
    OpenAiResponse result = mapper.toOpenAiResponse(responseDto);

    // Assert
    assertNull(result);
  }
}
