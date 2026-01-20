package com.example.websocket.repository;

import com.example.websocket.entity.Conversation;
import com.example.websocket.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    @Query("""
            SELECT cm.conversation
            FROM ConversationMember cm
            WHERE LOWER(cm.user.username) = LOWER(:username)
            """)
    List<Conversation> findConversationsByUsername(String username);
}