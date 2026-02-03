package com.example.websocket.controller;


import com.example.websocket.dto.AuthResponse;
import com.example.websocket.entity.RefreshToken;
import com.example.websocket.entity.Role;
import com.example.websocket.entity.User;
import com.example.websocket.exceptions.InvalidCredentialsException;
import com.example.websocket.exceptions.InvalidRefreshTokenException;
import com.example.websocket.exceptions.RoleNotFoundException;
import com.example.websocket.repository.RefreshTokenRepository;
import com.example.websocket.repository.RoleRepository;
import com.example.websocket.repository.UserRepository;
import com.example.websocket.services.JWTService;
import com.example.websocket.services.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository repo, PasswordEncoder encoder, AuthenticationManager authenticationManager, JWTService jwtService, UserRepository userRepository, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, RoleRepository roleRepository) {
        this.userRepo = repo;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception ex) {
            throw new InvalidCredentialsException();
        }
        User user = userRepository.findByUsername(username).orElseThrow(InvalidCredentialsException::new);

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
        RefreshToken token = refreshTokenService.verifyExpiration(refreshTokenRepository.findByToken(refreshToken).orElseThrow(InvalidRefreshTokenException::new));

        String newAccessToken = jwtService.generateToken(token.getUser());

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.getRoles().add(userRole);

        userRepo.save(user);
        return "User registered successfully";
    }

}
