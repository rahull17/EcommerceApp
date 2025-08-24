package com.example.server.dto.req;

import lombok.Data;

@Data
public class CaptureRequestDTO {
    private String paymentId;
    private String payerId;
    private String orderId;
}
