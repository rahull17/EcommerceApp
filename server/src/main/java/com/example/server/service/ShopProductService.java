package com.example.server.service;

import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Product;
import com.example.server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopProductService {

    private final ProductRepository productRepository;

    public ApiResponse<List<Product>> getFilteredProducts(String category, String brand, String sortBy) {
        try {
            List<String> categories = (category != null && !category.isEmpty())
                    ? Arrays.asList(category.split(","))
                    : null;

            List<String> brands = (brand != null && !brand.isEmpty())
                    ? Arrays.asList(brand.split(","))
                    : null;

            // sorting
            Sort sort;
            switch (sortBy) {
                case "price-hightolow":
                    sort = Sort.by(Sort.Direction.DESC, "price");
                    break;
                case "title-atoz":
                    sort = Sort.by(Sort.Direction.ASC, "title");
                    break;
                case "title-ztoa":
                    sort = Sort.by(Sort.Direction.DESC, "title");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.ASC, "price");
            }

            List<Product> products = productRepository.findWithFilters(categories, brands, sort);

            return new ApiResponse<>(true, "Products fetched successfully", products);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Some error occurred: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Product> getProductDetails(String id) {
        try {
            return productRepository.findById(id)
                    .map(product -> new ApiResponse<>(true, "Product found", product))
                    .orElse(new ApiResponse<>(false, "Product not found", null));
        } catch (Exception e) {
            return new ApiResponse<>(false, "Some error occurred: " + e.getMessage(), null);
        }
    }
}
