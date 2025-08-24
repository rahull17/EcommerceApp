package com.example.server.controller;

import com.example.server.dto.req.LoginRequest;
import com.example.server.dto.req.UserRegisterRequest;
import com.example.server.dto.res.LoginResponse;
import com.example.server.dto.res.UserRegisterResponse;
import com.example.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserRegisterResponse register(@RequestBody UserRegisterRequest request,
                                         HttpServletResponse response) {
        return authService.register(request, response);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request,
                               HttpServletResponse response) {
        return authService.login(request, response);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletResponse response) {
        authService.logout(response);

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Logged out successfully");

        return res;
    }

    @GetMapping("/check-auth")
    public Map<String, Object> checkAuth(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new RuntimeException("Token not found");
        }

        return authService.checkAuth(token);
    }
}
