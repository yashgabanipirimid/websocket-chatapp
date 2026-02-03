package com.example.websocket.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuth(AuthException ex) {
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Map.of("error", ex.getMessage(), "type", ex.getClass().getSimpleName()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(Exception ex) {

        return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
    }
}
