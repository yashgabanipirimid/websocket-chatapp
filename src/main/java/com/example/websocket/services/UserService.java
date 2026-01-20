package com.example.websocket.services;

import com.example.websocket.entity.User;
import com.example.websocket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String username) {
        return userRepo.findByUsername(username).orElseGet(() -> {
            User u = new User();
            u.setUsername(username.toLowerCase());
            u.setEmail(username + "@chat.com");
            return userRepo.save(u);
        });
    }
}
