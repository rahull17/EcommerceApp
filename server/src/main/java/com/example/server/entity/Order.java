package com.example.server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String cartId;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private double totalAmount;
    @CreationTimestamp
    private LocalDateTime orderDate;
    @UpdateTimestamp
    private LocalDateTime orderUpdateDate;
    private String paymentId;
    private String payerId;

    @ElementCollection
    @CollectionTable(name = "order_cart_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<CartItem> cartItems;

    @Embedded
    private AddressInfo addressInfo;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class CartItem {
        private String productId;
        private String title;
        private String image;
        private double price;
        private int quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class AddressInfo {
        private String addressId;
        private String address;
        private String city;
        private String pincode;
        private String phone;
        private String notes;
    }
}
