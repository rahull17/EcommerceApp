package com.example.server.dto.req;

import lombok.Data;

@Data
public class CartRequest {
    private String userId;
    private String productId;
    private int quantity;
}
