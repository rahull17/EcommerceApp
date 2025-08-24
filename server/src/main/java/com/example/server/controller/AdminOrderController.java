package com.example.server.controller;

import com.example.server.dto.req.UpdateOrderStatusRequest;
import com.example.server.dto.res.ApiResponse;
import com.example.server.entity.Order;
import com.example.server.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrdersOfAllUsers() {
        List<Order> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Orders fetched successfully", orders)
        );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderDetailsForAdmin(@PathVariable Long id) {
        Order order = adminOrderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(false, "Order not found", null)
            );
        }
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order fetched successfully", order)
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        Order updatedOrder = adminOrderService.updateOrderStatus(id, request.getOrderStatus());
        if (updatedOrder == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Order not found", null)
            );
        }
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order updated successfully", updatedOrder)
        );
    }
}
