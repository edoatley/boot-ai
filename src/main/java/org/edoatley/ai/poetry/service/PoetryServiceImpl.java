package org.edoatley.ai.poetry.service;

import org.edoatley.ai.poetry.model.Poetry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PoetryServiceImpl implements PoetryService {

    public static final String WRITE_ME_HAIKU_ABOUT_CAT = """
        Write me Haiku about cat,
        haiku should start with the word cat obligatory""";

    private final ChatClient chatClient;

    public PoetryServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Poetry getCatHaiku() {
        String poem = chatClient.prompt(WRITE_ME_HAIKU_ABOUT_CAT).call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getContent();
        return new Poetry(WRITE_ME_HAIKU_ABOUT_CAT, poem);
    }
}