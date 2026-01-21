package com.example.websocket.exceptions;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException() {
        super("Username already exists");
    }
}
