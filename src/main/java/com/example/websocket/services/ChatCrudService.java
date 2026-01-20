package com.example.websocket.services;

import com.example.websocket.dto.ChatMessageResponse;
import com.example.websocket.dto.ConversationResponse;
import com.example.websocket.entity.Conversation;
import com.example.websocket.entity.ConversationMember;
import com.example.websocket.entity.ConversationType;
import com.example.websocket.entity.MemberRole;
import com.example.websocket.entity.Message;
import com.example.websocket.entity.User;
import com.example.websocket.repository.ConversationMemberRepository;
import com.example.websocket.repository.ConversationRepository;
import com.example.websocket.repository.MessageRepository;
import com.example.websocket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatCrudService {

    private final UserRepository userRepo;
    private final ConversationRepository conversationRepo;
    private final ConversationMemberRepository memberRepo;
    private final MessageRepository messageRepo;

    public ChatCrudService(UserRepository userRepo, ConversationRepository conversationRepo, ConversationMemberRepository memberRepo, MessageRepository messageRepo) {
        this.userRepo = userRepo;
        this.conversationRepo = conversationRepo;
        this.memberRepo = memberRepo;
        this.messageRepo = messageRepo;
    }

    public User login(String username) {
        String normalized = username.toLowerCase();
        return userRepo.findByUsername(normalized).orElseGet(() -> userRepo.save(new User(normalized, normalized + "@chat.com")));
    }

    public Conversation createOneToOneChat(String user1, String user2) {

        User u1 = userRepo.findByUsername(user1.toLowerCase()).orElseThrow();
        User u2 = userRepo.findByUsername(user2.toLowerCase()).orElseThrow();

        Conversation conversation = conversationRepo.save(new Conversation(ConversationType.ONE_TO_ONE, null));

        memberRepo.save(new ConversationMember(conversation, u1, MemberRole.MEMBER));
        memberRepo.save(new ConversationMember(conversation, u2, MemberRole.MEMBER));

        return conversation;
    }

    public List<ConversationResponse> getUserConversations(String username) {

        String me = username.toLowerCase();

        List<Conversation> conversations = memberRepo.findConversationsByUsername(me);

        return conversations.stream().map(c -> {

            if (c.getType() == ConversationType.GROUP) {
                return new ConversationResponse(c.getId(), c.getTitle(), c.getType());
            }

            // ONE_TO_ONE → find the OTHER user
            String otherUser = c.getMembers().stream().map(m -> m.getUser().getUsername()).filter(u -> !u.equals(me)).findFirst().orElse("Unknown");

            return new ConversationResponse(c.getId(), otherUser, c.getType());

        }).toList();
    }


    public List<ChatMessageResponse> getMessages(Long conversationId) {

        List<Message> messages = messageRepo.findMessagesByConversationId(conversationId);

        return messages.stream().map(m -> {
            ChatMessageResponse dto = new ChatMessageResponse();
            dto.setMessageId(m.getId());
            dto.setConversationId(conversationId);
            dto.setSender(m.getSender().getUsername());
            dto.setContent(m.getContent());
            dto.setSentAt(m.getSentAt());
            return dto;
        }).toList();
    }

    public Conversation createOneToOne(Long user1Id, Long user2Id) {

        User user1 = userRepo.findById(user1Id).orElseThrow();
        User user2 = userRepo.findById(user2Id).orElseThrow();

        Conversation existing = conversationRepo.findOneToOneConversation(user1Id, user2Id, ConversationType.ONE_TO_ONE);

        if (existing != null) {
            return existing;
        }

        Conversation conversation = conversationRepo.save(new Conversation(ConversationType.ONE_TO_ONE, null));

        memberRepo.save(new ConversationMember(conversation, user1, MemberRole.MEMBER));
        memberRepo.save(new ConversationMember(conversation, user2, MemberRole.MEMBER));

        return conversation;
    }

    public Conversation createGroup(String title, Long adminId, List<Long> members) {

        User admin = userRepo.findById(adminId).orElseThrow();

        Conversation conversation = conversationRepo.save(new Conversation(ConversationType.GROUP, title));

        memberRepo.save(new ConversationMember(conversation, admin, MemberRole.ADMIN));

        for (Long id : members) {
            User user = userRepo.findById(id).orElseThrow();
            memberRepo.save(new ConversationMember(conversation, user, MemberRole.MEMBER));
        }

        return conversation;
    }

}
