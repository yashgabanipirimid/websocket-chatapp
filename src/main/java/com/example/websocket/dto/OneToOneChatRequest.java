package com.example.websocket.dto;

public class OneToOneChatRequest {
    private Long user1Id;
    private Long user2Id;

    public OneToOneChatRequest() {
    }

    public OneToOneChatRequest(Long user1Id, Long user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Long getUser1Id() {
        return user1Id;
    }
    public Long getUser2Id() {
        return user2Id;
    }
}
