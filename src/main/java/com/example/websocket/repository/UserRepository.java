package com.example.websocket.repository;


import com.example.websocket.entity.Conversation;
import com.example.websocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT cm.conversation
            FROM ConversationMember cm
            WHERE cm.user.username = :username
            """)
    List<Conversation> findUserConversations(String username);
}

