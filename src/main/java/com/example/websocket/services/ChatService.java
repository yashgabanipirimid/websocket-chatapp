package com.example.websocket.services;

import com.example.websocket.dto.ChatMessageRequest;
import com.example.websocket.entity.Conversation;
import com.example.websocket.entity.Message;
import com.example.websocket.entity.User;
import com.example.websocket.repository.ConversationRepository;
import com.example.websocket.repository.MessageRepository;
import com.example.websocket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final ConversationRepository conversationRepo;

    public ChatService(MessageRepository messageRepo, UserRepository userRepo, ConversationRepository conversationRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.conversationRepo = conversationRepo;
    }

    public Message saveMessage(ChatMessageRequest request) {

        User sender = userRepo.findByUsername(request.getSenderUsername().toLowerCase()).orElseThrow(() -> new RuntimeException("User not found"));

        Conversation conversation = conversationRepo.findById(request.getConversationId()).orElseThrow(() -> new RuntimeException("Conversation not found"));

        return messageRepo.save(new Message(request.getContent(), conversation, sender));
    }
}
