package com.example.websocket.exceptions;

public class OAuthAccountConflictException extends AuthException {
    public OAuthAccountConflictException() {
        super("Account already exists with password login");
    }
}
