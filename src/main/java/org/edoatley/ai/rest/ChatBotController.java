package org.edoatley.ai.rest;

import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.service.chat.ChatBotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai/rag")
public class ChatBotController {

    private final ChatBotService chatBotService;
    
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @GetMapping(value = "/chat")
    public Map<String, String> chat(@RequestParam(name = "query") String query) {
        log.info("Received query {}", query);
        return Map.of("answer", chatBotService.chat(query));
    }
}


