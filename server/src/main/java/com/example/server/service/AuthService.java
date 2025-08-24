package com.example.server.service;

import com.example.server.dto.UserDto;
import com.example.server.dto.req.LoginRequest;
import com.example.server.dto.req.UserRegisterRequest;
import com.example.server.dto.res.LoginResponse;
import com.example.server.dto.res.UserRegisterResponse;
import com.example.server.entity.User;
import com.example.server.repository.UserRepository;
import com.example.server.config.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Register
    public UserRegisterResponse register(UserRegisterRequest req, HttpServletResponse response) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return new UserRegisterResponse(false, "Email already exists");
        }
        if (userRepository.findByUserName(req.getUserName()).isPresent()) {
            return new UserRegisterResponse(false, "Username already exists");
        }

        User user = User.builder()
                .userName(req.getUserName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role("user")
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getEmail(), user.getUserName());
        addTokenCookie(response, token);

        return new UserRegisterResponse(true, "Registration successful");
    }

    // Login
    public LoginResponse login(LoginRequest req, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getEmail(), user.getUserName());
        addTokenCookie(response, token);

        return LoginResponse.builder()
                .success(true)
                .message("Logged in successfully")
                .user(UserDto.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .userName(user.getUserName())
                        .build())
                .build();
    }

    // Check Auth
    public Map<String, Object> checkAuth(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Claims claims = jwtUtil.extractClaims(token);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", (String) claims.get("id"));
        userMap.put("role", (String) claims.get("role"));
        userMap.put("email", (String) claims.get("email"));
        userMap.put("userName", (String) claims.get("userName"));
        userMap.put("iat", claims.getIssuedAt().toInstant().getEpochSecond());
        userMap.put("exp", claims.getExpiration().toInstant().getEpochSecond());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Authenticated user!");
        response.put("user", userMap);

        return response;
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // remove immediately
        response.addCookie(cookie);
    }
}
