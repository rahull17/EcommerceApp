package com.example.server.service;

import com.example.server.dto.req.CartRequest;
import com.example.server.dto.res.ApiResponse;
import com.example.server.dto.res.CartResponse;
import com.example.server.entity.Cart;
import com.example.server.entity.Product;
import com.example.server.entity.User;
import com.example.server.repository.CartRepository;
import com.example.server.repository.ProductRepository;
import com.example.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopCartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private CartResponse mapToResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getItems().stream()
                        .map(item -> new CartResponse.CartItemResponse(
                                item.getProduct().getId(),
                                item.getProduct().getImage(),   // assumes Product has imageUrl field
                                item.getProduct().getTitle(),
                                item.getProduct().getPrice(),
                                item.getProduct().getSalePrice(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }


    public ApiResponse<CartResponse> addToCart(CartRequest request) {
        try {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (userOpt.isEmpty()) {
                return new ApiResponse<>(false, "User not found", null);
            }

            Optional<Product> productOpt = productRepository.findById(request.getProductId());
            if (productOpt.isEmpty()) {
                return new ApiResponse<>(false, "Product not found", null);
            }

            User user = userOpt.get();
            Product product = productOpt.get();

            if (request.getQuantity() > product.getTotalStock()) {
                return new ApiResponse<>(false,
                        "Only " + product.getTotalStock() + " items available in stock",
                        null);
            }

            Cart cart = cartRepository.findByUser(user)
                    .orElse(Cart.builder().user(user).build());

            Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                int newQty = existingItem.get().getQuantity() + request.getQuantity();
                if (newQty > product.getTotalStock()) {
                    return new ApiResponse<>(false,
                            "Only " + product.getTotalStock() + " items available in stock",
                            null);
                }
                existingItem.get().setQuantity(newQty);
            } else {
                cart.getItems().add(new Cart.CartItem(product, request.getQuantity()));
            }

            Cart saved = cartRepository.save(cart);
            return new ApiResponse<>(true, "Item added to cart", mapToResponse(saved));

        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }


    public ApiResponse<CartResponse> getCart(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        return cartRepository.findByUser(userOpt.get())
                .map(cart -> new ApiResponse<>(true, "Cart fetched successfully", mapToResponse(cart)))
                .orElse(new ApiResponse<>(true, "Cart is empty", null));
    }

    public ApiResponse<CartResponse> updateCart(CartRequest request) {
        try {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (userOpt.isEmpty()) {
                return new ApiResponse<>(false, "User not found", null);
            }

            Optional<Product> productOpt = productRepository.findById(request.getProductId());
            if (productOpt.isEmpty()) {
                return new ApiResponse<>(false, "Product not found", null);
            }

            Product product = productOpt.get();
            if (request.getQuantity() > product.getTotalStock()) {
                return new ApiResponse<>(false,
                        "Only " + product.getTotalStock() + " items available in stock",
                        null);
            }

            return cartRepository.findByUser(userOpt.get())
                    .map(cart -> {
                        cart.getItems().forEach(item -> {
                            if (item.getProduct().getId().equals(product.getId())) {
                                item.setQuantity(request.getQuantity());
                            }
                        });
                        Cart updated = cartRepository.save(cart);
                        return new ApiResponse<>(true, "Cart updated successfully", mapToResponse(updated));
                    })
                    .orElse(new ApiResponse<>(false, "Cart not found", null));

        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<CartResponse> removeFromCart(String userId, String productId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new ApiResponse<>(false, "User not found", null);
            }

            return cartRepository.findByUser(userOpt.get())
                    .map(cart -> {
                        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
                        Cart updated = cartRepository.save(cart);
                        return new ApiResponse<>(true, "Item removed from cart", mapToResponse(updated));
                    })
                    .orElse(new ApiResponse<>(false, "Cart not found", null));

        } catch (Exception e) {
            return new ApiResponse<>(false, "Error: " + e.getMessage(), null);
        }
    }
}
