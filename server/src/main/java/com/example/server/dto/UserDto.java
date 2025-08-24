package com.example.server.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String id;
    private String email;
    private String role;
    private String userName;
}
