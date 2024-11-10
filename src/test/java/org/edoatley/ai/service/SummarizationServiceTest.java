package org.edoatley.ai.service;

import org.edoatley.ai.service.chat.DataRetrievalService;
import org.edoatley.ai.service.summarize.SummarizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummarizationServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private DataRetrievalService dataRetrievalService;

    @InjectMocks
    private SummarizationService summarizationService;

    @Test
    void shouldReturnMessageWhenNoContentFound() {
        when(dataRetrievalService.searchData(anyString())).thenReturn(List.of());

        String result = summarizationService.summarizeContent("kafka");

        assertThat(result).contains("No relevant content found");
    }

    @Test
    void shouldGenerateSummaryWhenContentFound() {
        Document doc = new Document("Sample Kafka content about brokers");
        when(dataRetrievalService.searchData("kafka")).thenReturn(List.of(doc));
        ChatClientRequestSpec mockRequestSpec = Mockito.mock(ChatClientRequestSpec.class);
        CallResponseSpec mockCallResponseSpec = Mockito.mock(CallResponseSpec.class);

        // call sequence from ChatClient.prompt() call
        when(chatClient.prompt(anyString())).thenReturn(mockRequestSpec);
        when(mockRequestSpec.call()).thenReturn(mockCallResponseSpec);
        when(mockCallResponseSpec.content()).thenReturn("Summary of Kafka brokers");
        
        String result = summarizationService.summarizeContent("kafka");
        assertThat(result).isEqualTo("Summary of Kafka brokers");
    }
} 