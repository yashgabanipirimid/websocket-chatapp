package com.example.websocket.dto;

import java.util.List;

public class GroupChatRequest {

    private String title;
    private Long adminId;
    private List<Long> memberIds;

    public GroupChatRequest() {
    }

    public GroupChatRequest(String title, Long adminId, List<Long> memberIds) {
        this.title = title;
        this.adminId = adminId;
        this.memberIds = memberIds;
    }

    public String getTitle() {
        return title;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
