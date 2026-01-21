package com.example.websocket.exceptions;

public class OAuthEmailNotFoundException extends AuthException {
    public OAuthEmailNotFoundException() {
        super("Email not provided by OAuth provider");
    }
}
