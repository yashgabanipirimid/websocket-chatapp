package com.example.websocket.dto;


public class SendMessageRequest {

    private Long senderId;
    private String content;

    public SendMessageRequest() {
    }

    public SendMessageRequest(Long senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
