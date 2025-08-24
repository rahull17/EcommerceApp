package com.example.server.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String image;
    private String title;
    private String description;
    private String category;
    private String brand;
    private Double price;
    private Double salePrice;
    private Integer totalStock;
    private Double averageReview;
}
