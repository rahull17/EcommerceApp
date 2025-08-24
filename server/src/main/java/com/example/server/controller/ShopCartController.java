package com.example.server.controller;

import com.example.server.dto.req.CartRequest;
import com.example.server.dto.res.ApiResponse;
import com.example.server.dto.res.CartResponse;
import com.example.server.service.ShopCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/cart")
@RequiredArgsConstructor
public class ShopCartController {

    private final ShopCartService cartService;

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/get/{userId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @PutMapping("/update-cart")
    public ApiResponse<CartResponse> updateCart(@RequestBody CartRequest request) {
        return cartService.updateCart(request);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ApiResponse<CartResponse> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        return cartService.removeFromCart(userId, productId);
    }
}
