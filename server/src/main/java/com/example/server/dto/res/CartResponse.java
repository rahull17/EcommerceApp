package com.example.server.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String id;  // cart id
    private String userId;
    private List<CartItemResponse> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemResponse {
        private String productId;
        private String image;
        private String title;
        private double price;
        private double salePrice;
        private int quantity;
    }
}
