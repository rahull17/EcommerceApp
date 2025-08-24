package com.example.server.dto.res;

import com.example.server.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private boolean success;
    private String message;
    private UserDto user;
}
