package com.example.server.controller;

import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Product;
import com.example.server.service.ShopProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/products")
@RequiredArgsConstructor
public class ShopProductController {

    private final ShopProductService productService;

    // GET with filters
    @GetMapping("/get")
    public ApiResponse<List<Product>> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "price-lowtohigh") String sortBy
    ) {
        return productService.getFilteredProducts(category, brand, sortBy);
    }

    // GET single product details
    @GetMapping("/get/{id}")
    public ApiResponse<Product> getProductDetails(@PathVariable String id) {
        return productService.getProductDetails(id);
    }
}
