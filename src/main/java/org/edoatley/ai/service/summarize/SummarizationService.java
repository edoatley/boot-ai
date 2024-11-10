package org.edoatley.ai.service.summarize;

import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.service.chat.DataRetrievalService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SummarizationService {
    private static final String SUMMARIZE_PROMPT = """
        Summarize the following content in a clear and concise way. The content is related to: {topic}
        
        Content to summarize:
        {content}
        
        Please provide:
        1. A one-line TLDR
        2. A detailed summary in 2-3 paragraphs
        3. Key points as bullet points
        """;

    private final ChatClient chatClient;
    private final DataRetrievalService dataRetrievalService;

    public SummarizationService(ChatClient chatClient, DataRetrievalService dataRetrievalService) {
        this.chatClient = chatClient;
        this.dataRetrievalService = dataRetrievalService;
    }

    public String summarizeContent(String searchTopic) {
        log.info("Generating summary for topic: {}", searchTopic);
        List<Document> relevantDocs = dataRetrievalService.searchData(searchTopic);
        log.info("Found {} relevant documents", relevantDocs.size());
        
        if (relevantDocs.isEmpty()) {
            return "No relevant content found for the topic: " + searchTopic;
        }

        String combinedContent = relevantDocs.stream()
                .map(Document::getContent)
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        return generateSummary(searchTopic, combinedContent);
    }

    private String generateSummary(String topic, String content) {
        PromptTemplate promptTemplate = new PromptTemplate(SUMMARIZE_PROMPT);
        promptTemplate.add("topic", topic);
        promptTemplate.add("content", content);
        return chatClient.prompt(promptTemplate.render()).call().content();
    }
} 