package com.example.websocket.security;


import com.example.websocket.entity.Role;
import com.example.websocket.entity.User;
import com.example.websocket.exceptions.OAuthEmailNotFoundException;
import com.example.websocket.exceptions.RoleNotFoundException;
import com.example.websocket.repository.RoleRepository;
import com.example.websocket.repository.UserRepository;
import com.example.websocket.services.JWTService;
import com.example.websocket.services.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public OAuth2SuccessHandler(JWTService jwtService, RefreshTokenService refreshTokenService, UserRepository userRepository, RoleRepository roleRepository) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.isBlank()) {
            throw new OAuthEmailNotFoundException();
        }
        User user = userRepository.findByUsername(email).orElseGet(() -> {

            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new);

            User u = new User();
            u.setUsername(email);
            u.setPassword("OAUTH_USER");
            u.getRoles().add(userRole);

            return userRepository.save(u);
        });


        String accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);

        response.sendRedirect("http://localhost:8080/index.html");
    }
}
