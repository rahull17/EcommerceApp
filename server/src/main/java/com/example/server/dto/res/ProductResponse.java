package com.example.server.dto.res;

import com.example.server.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {
    private boolean success;
    private Object data;
    private String message;

    public static ProductResponse success(Object data) {
        return ProductResponse.builder()
                .success(true)
                .data(data)
                .build();
    }

    public static ProductResponse successMessage(String msg) {
        return ProductResponse.builder()
                .success(true)
                .message(msg)
                .build();
    }

    public static ProductResponse error(String msg) {
        return ProductResponse.builder()
                .success(false)
                .message(msg)
                .build();
    }
}
