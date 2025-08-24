package com.example.server.dto.req;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String userName;
    private String email;
    private String password;
}
