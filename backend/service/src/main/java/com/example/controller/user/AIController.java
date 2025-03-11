package com.example.controller.user;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;
@RestController
@RequestMapping
public class AIController {
    private final ChatClient chatClient;
    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";
    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/chat")
    public String chat(String input) {
        return this.chatClient.prompt()
                .user(input)
                .call()
                .content();
    }

    @GetMapping("/stream")
    public String stream(String input) {
        Flux<String> content = this.chatClient.prompt().user(input).stream().content();
        return Objects.requireNonNull(content.collectList().block()).stream().reduce((a, b) -> a + b).get();
    }
    @GetMapping("/test")
    public String test() {
        return "hello spring ai";
    }

}
