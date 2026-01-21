package com.example.websocket.exceptions;

public class RoleNotFoundException extends AuthException {
    public RoleNotFoundException() {
        super("Role not found");
    }
}
