package org.edoatley.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    class OpenAiConfig {

        @Bean
        ChatClient chatClient(ChatClient.Builder builder) {
            return builder.build();
        }

        @Bean
        TextSplitter textSplitter() {
            return new TokenTextSplitter();
        }
    }