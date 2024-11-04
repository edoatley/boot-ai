package org.edoatley.ai.chat.rest;

import lombok.extern.slf4j.Slf4j;
import org.edoatley.ai.chat.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ChatBotController {

    private final ChatBotService chatBotService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @GetMapping(value = "/rag/chat")
    public Map<String, String> chat(@RequestParam(name = "query") String query) {
        log.info("Received query {}", query);
        return Map.of("answer", chatBotService.chat(query));
    }
}


