package org.edoatley.ai.config;

import org.edoatley.ai.poetry.model.Poetry;
import org.edoatley.ai.poetry.service.PoetryService;
import org.edoatley.ai.poetry.service.PoetryServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;

/**
 * This class mocks out the OpenAI services so we can test in their absence
 */
@TestConfiguration
public class OpenAITestConfiguration {

    @Bean
    @Primary
    public PoetryService testPoetryService() {
        return () -> new Poetry(
                PoetryServiceImpl.WRITE_ME_HAIKU_ABOUT_CAT,
                "Cats are cute and sweet\nLoving and Playful, like me!\nBut, Dogs are better!");
    }

    @Bean
    @Primary
    public ChatModel chatModel() {
          return new ChatModel() {
              @Override
              public ChatResponse call(Prompt prompt) {
                  return null;
              }

              @Override
              public ChatOptions getDefaultOptions() {
                  return null;
              }
          };
    }

    @Bean
    @Primary
    public ChatClient chatClient() {
          return new ChatClient() {

              @Override
              public ChatClientRequestSpec prompt() {
                  return null;
              }

              @Override
              public ChatClientRequestSpec prompt(String content) {
                  return null;
              }

              @Override
              public ChatClientRequestSpec prompt(Prompt prompt) {
                  return null;
              }

              @Override
              public Builder mutate() {
                  return null;
              }
          };
    }

    @Bean
    public VectorStore vectorStore() {
        return new VectorStore() {
            @Override
            public void add(List<Document> documents) {

            }

            @Override
            public Optional<Boolean> delete(List<String> idList) {
                return Optional.empty();
            }

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return List.of();
            }
        };
    }
}
