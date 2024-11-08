package org.edoatley.ai.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatBotService {

    private static final String PROMPT_BLUEPRINT = """
      Answer the query strictly referring the provided context:
      {context}
      Query:
      {query}
      In case you don't have any answer from the context provided, just say:
      I'm sorry I don't have the information you are looking for.
    """;

    private final ChatModel chatClient;
    private final DataRetrievalService dataRetrievalService;

    public ChatBotService(ChatModel chatModel, DataRetrievalService dataRetrievalService) {
          this.chatClient = chatModel;
          this.dataRetrievalService = dataRetrievalService;
    }

    public String chat(String query) {
        log.info("Calling chat()");
        List<Document> context = dataRetrievalService.searchData(query);
        log.info("Calling chat() found {} documents", context.size());
        return chatClient.call(createPrompt(query, context));
    }

    private String createPrompt(String query, List<Document> context) {
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_BLUEPRINT);
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }
}
