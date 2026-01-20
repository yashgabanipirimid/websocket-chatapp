package com.example.websocket.controller;

import com.example.websocket.dto.ConversationResponse;
import com.example.websocket.dto.GroupChatRequest;
import com.example.websocket.dto.OneToOneChatRequest;
import com.example.websocket.entity.Conversation;
import com.example.websocket.services.ChatCrudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ChatCrudService service;

    public ConversationController(ChatCrudService service) {
        this.service = service;
    }
    @GetMapping("/{username}")
    public List<ConversationResponse> getChats(@PathVariable String username) {
        return service.getUserConversations(username);
    }

    @PostMapping("/one-to-one")
    public Conversation createOneToOne(@RequestBody OneToOneChatRequest request) {

        return service.createOneToOne(request.getUser1Id(), request.getUser2Id());
    }
    @PostMapping("/group")
    public Conversation createGroup(@RequestBody GroupChatRequest request) {

        return service.createGroup(request.getTitle(), request.getAdminId(), request.getMemberIds());
    }
}
