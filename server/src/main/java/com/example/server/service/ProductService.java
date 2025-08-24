package com.example.server.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.server.dto.req.ProductRequest;
import com.example.server.dto.res.ProductResponse;
import com.example.server.entity.Product;
import com.example.server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("result", result);

        return response;
    }

    public ProductResponse addProduct(ProductRequest req) {
        Product product = Product.builder()
                .image(req.getImage())
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory())
                .brand(req.getBrand())
                .price(req.getPrice())
                .salePrice(req.getSalePrice())
                .totalStock(req.getTotalStock())
                .averageReview(req.getAverageReview())
                .build();

        Product saved = productRepository.save(product);
        return ProductResponse.success(saved);
    }

    public ProductResponse updateProduct(String id, ProductRequest req) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setImage(req.getImage());
                    product.setTitle(req.getTitle());
                    product.setDescription(req.getDescription());
                    product.setCategory(req.getCategory());
                    product.setBrand(req.getBrand());
                    product.setPrice(req.getPrice());
                    product.setSalePrice(req.getSalePrice());
                    product.setTotalStock(req.getTotalStock());
                    product.setAverageReview(req.getAverageReview());
                    Product updated = productRepository.save(product);
                    return ProductResponse.success(updated);
                })
                .orElse(ProductResponse.error("Product not found"));
    }

    public ProductResponse deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            return ProductResponse.error("Product not found");
        }
        productRepository.deleteById(id);
        return ProductResponse.successMessage("Product delete successfully");
    }

    public ProductResponse getProducts() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.success(products);
    }
}
