package com.example.websocket.services;

import com.example.websocket.entity.Permission;
import com.example.websocket.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {
    private static final String Secret_Key = "THIS_IS_A_32+_BYTE_LONG_SECRET_KEY_FOR_JWT_123456";
    private static final long Expiration = 1000 * 60;
    private final Key key = Keys.hmacShaKeyFor(Secret_Key.getBytes());

    public String generateToken(User user) {

        List<String> authorities = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(Permission::getName).toList();

        return Jwts.builder().setSubject(user.getUsername()).claim("authorities", authorities).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + Expiration)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
