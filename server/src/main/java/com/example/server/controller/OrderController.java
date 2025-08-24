package com.example.server.controller;

import com.example.server.dto.req.CaptureRequestDTO;
import com.example.server.dto.req.OrderRequestDTO;
import com.example.server.dto.res.ApiResponse;
import com.example.server.dto.res.PaymentCreationData;
import com.example.server.entity.Order;
import com.example.server.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<PaymentCreationData> createPayment(@RequestBody OrderRequestDTO request) throws Exception {
        return orderService.createPayment(request);
    }

    @PostMapping("/capture")
    public ApiResponse<Order> capturePayment(@RequestBody CaptureRequestDTO dto) throws Exception {
        orderService.capturePayment(dto.getPaymentId(), dto.getPayerId());

        Order updatedOrder = orderService.markOrderAsPaid(
                Long.valueOf(dto.getOrderId()),
                dto.getPaymentId(),
                dto.getPayerId()
        );

        return new ApiResponse<>(true, "Payment captured successfully", updatedOrder);
    }

    @GetMapping("/list/{userId}")
    public ApiResponse<List<Order>> getAllOrdersByUser(@PathVariable String userId) {
        return new ApiResponse<>(true, "Orders fetched successfully", orderService.getAllOrdersByUser(userId));
    }

    @GetMapping("/details/{id}")
    public ApiResponse<Order> getOrderDetails(@PathVariable Long id) {
        return new ApiResponse<>(true, "Order fetched successfully", orderService.getOrderDetails(id));
    }
}
