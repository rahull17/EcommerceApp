package com.example.server.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String userId;
    private String cartId;
    private List<CartItem> cartItems;
    private AddressInfo addressInfo;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private double totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime orderUpdateDate;
    private String paymentId;
    private String payerId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItem {
        private String productId;
        private String title;
        private String image;
        private double price;
        private int quantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddressInfo {
        private String addressId;
        private String address;
        private String city;
        private String pincode;
        private String phone;
        private String notes;
    }
}
