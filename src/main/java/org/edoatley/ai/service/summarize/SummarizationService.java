package org.edoatley.ai.service.summarize;

import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.rest.model.Summary;
import org.edoatley.ai.rest.model.SummarySearch;
import org.edoatley.ai.service.chat.DataRetrievalService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public SummarySearch summarizeContent(String searchTopic) {
        log.info("Generating summary for topic: {}", searchTopic);
        List<Document> relevantDocs = dataRetrievalService.searchData(searchTopic);
        log.info("Found {} relevant documents", relevantDocs.size());
        
        if (relevantDocs.isEmpty()) {
            return new SummarySearch(false, null, "No relevant content found for the topic: " + searchTopic);
        }

        String combinedContent = relevantDocs.stream()
                .map(Document::getContent)
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        return generateSummary(searchTopic, combinedContent);
    }

    private SummarySearch generateSummary(String topic, String content) {
        PromptTemplate promptTemplate = new PromptTemplate(SUMMARIZE_PROMPT);
        promptTemplate.add("topic", topic);
        promptTemplate.add("content", content);
        String summary = chatClient.prompt(promptTemplate.render()).call().content();
        return new SummarySearch(true, formatSummary(summary, topic), null);
    }

    private Summary formatSummary(String summary, String topic){
        // extract line 2 of the summary as the TLDR
        // extract everything on line 4 until the line before one begining **3 trim of empty trailing line if present
        // Capture each line after the 3** line and create a list with the text after each '- '
        // return a new Summary object with the TLDR, the detailed summary and the key points
        String[] lines = summary.replaceAll("\n\n", "\n").split("\n");
        if(lines.length < 4) {
            return new Summary(topic, "?", summary, List.of());
        }

        String tldr = lines[1].trim();
        StringBuilder detailedSummary = new StringBuilder();
        int titleThree = -1;
        for(int i = 3; i < lines.length; i++) {
            if (lines[i].startsWith("**")) {
                titleThree = i + 1;
                break;
            }
            detailedSummary.append(lines[i]).append("\n\n");
        }
        String[] rawBullets = Arrays.copyOfRange(lines, titleThree, lines.length);
        List<String> keyPoints = Arrays.stream(rawBullets)
                .map(l -> l.replace("- ", ""))
                .toList();
        return new Summary(topic, tldr, detailedSummary.toString().trim(), keyPoints);

    }
} 