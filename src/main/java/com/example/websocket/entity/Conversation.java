package com.example.websocket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;
@Data
@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConversationType type;

    private String title;
    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<ConversationMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    public Conversation() {}
    public Conversation(ConversationType type, String title) {
        this.type = type;
        this.title = title;
    }

}
