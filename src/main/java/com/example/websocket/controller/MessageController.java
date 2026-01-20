package com.example.websocket.controller;

import com.example.websocket.dto.ChatMessageResponse;
import com.example.websocket.services.ChatCrudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {

    private final ChatCrudService service;

    public MessageController(ChatCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChatMessageResponse> getMessages(@PathVariable Long conversationId) {
        return service.getMessages(conversationId);
    }
}
