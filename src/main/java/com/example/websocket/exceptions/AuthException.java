package com.example.websocket.exceptions;

public abstract class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
