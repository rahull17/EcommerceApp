package com.example.server.controller;

import com.example.server.dto.req.ProductRequest;
import com.example.server.dto.res.ProductResponse;
import com.example.server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/upload-image")
    public Map<String, Object> uploadImage(@RequestParam("my_file") MultipartFile file) throws IOException {
        return productService.uploadImage(file);
    }

    @PostMapping("/add")
    public ProductResponse addProduct(@RequestBody ProductRequest req) {
        return productService.addProduct(req);
    }

    @PutMapping("/edit/{id}")
    public ProductResponse updateProduct(@PathVariable String id, @RequestBody ProductRequest req) {
        return productService.updateProduct(id, req);
    }

    @DeleteMapping("/delete/{id}")
    public ProductResponse deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/get")
    public ProductResponse getProducts() {
        return productService.getProducts();
    }
}
