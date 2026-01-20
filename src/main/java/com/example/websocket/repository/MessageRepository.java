package com.example.websocket.repository;

import com.example.websocket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
                SELECT m FROM Message m
                WHERE m.conversation.id = :conversationId
                ORDER BY m.sentAt ASC
            """)
    List<Message> findMessagesByConversationId(@Param("conversationId") Long conversationId);

    @Query("""
                SELECT m FROM Message m
                WHERE m.conversation.id = :conversationId
                ORDER BY m.sentAt DESC
                LIMIT 1
            """)
    Message findLastMessage(@Param("conversationId") Long conversationId);
}