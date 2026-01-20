package com.example.websocket.controller;

import com.example.websocket.dto.ChatMessageRequest;
import com.example.websocket.dto.ChatMessageResponse;
import com.example.websocket.entity.Message;
import com.example.websocket.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequest request) {
        Message savedMessage = chatService.saveMessage(request);
        ChatMessageResponse response = new ChatMessageResponse();
        response.setMessageId(savedMessage.getId());
        response.setConversationId(request.getConversationId());
        response.setContent(savedMessage.getContent());
        response.setSender(savedMessage.getSender().getUsername());
        response.setSentAt(savedMessage.getSentAt());
        String topic = "/topic/conversation/" + request.getConversationId();
        messagingTemplate.convertAndSend(topic, response);
    }


}
