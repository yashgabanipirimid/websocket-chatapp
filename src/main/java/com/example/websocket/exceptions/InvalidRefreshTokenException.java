package com.example.websocket.exceptions;

public class InvalidRefreshTokenException extends AuthException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token");
    }
}
