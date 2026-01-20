package com.example.websocket.repository;

import com.example.websocket.entity.Conversation;
import com.example.websocket.entity.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
                SELECT c FROM Conversation c
                JOIN c.members m
                WHERE m.user.id = :userId
            """)
    List<Conversation> findAllByUserId(@Param("userId") Long userId);

    @Query(value = """
                SELECT c FROM Conversation c
                WHERE c.type = :type
                AND c.id IN (
                    SELECT m1.conversation.id
                    FROM ConversationMember m1
                    WHERE m1.user.id = :user1
                )
                AND c.id IN (
                    SELECT m2.conversation.id
                    FROM ConversationMember m2
                    WHERE m2.user.id = :user2
                )
            """)
    Conversation findOneToOneConversation(@Param("user1") Long user1, @Param("user2") Long user2, @Param("type") ConversationType type);


}

