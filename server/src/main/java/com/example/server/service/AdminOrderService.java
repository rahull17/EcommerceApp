package com.example.server.service;

import com.example.server.entity.Order;
import com.example.server.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        System.out.println(status);
        return orderRepository.findById(id).map(order -> {
            order.setOrderStatus(status);
            order.setOrderUpdateDate(LocalDateTime.now());
            return orderRepository.save(order);
        }).orElse(null);
    }
}
