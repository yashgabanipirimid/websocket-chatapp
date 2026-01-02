package com.example.websocket.dto;

import com.example.websocket.entity.ConversationType;

public class ConversationResponse {

    private final Long id;
    private final String name;
    private final ConversationType type;

    public ConversationResponse(Long id, String name, ConversationType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ConversationType getType() {
        return type;
    }
}
