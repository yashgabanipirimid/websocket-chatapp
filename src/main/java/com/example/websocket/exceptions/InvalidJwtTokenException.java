package com.example.websocket.exceptions;


public class InvalidJwtTokenException extends AuthException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
