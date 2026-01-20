package com.example.websocket.controller;

import com.example.websocket.dto.ChatMessageResponse;
import com.example.websocket.dto.ConversationResponse;
import com.example.websocket.entity.User;
import com.example.websocket.services.ChatCrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final ChatCrudService chatCrudService;

    public UserController(ChatCrudService chatCrudService) {
        this.chatCrudService = chatCrudService;
    }

    @PostMapping("/login")
    public User login(@RequestParam String username) {
        return chatCrudService.login(username);
    }

}
