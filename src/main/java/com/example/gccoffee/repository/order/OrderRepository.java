package com.example.gccoffee.repository.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order create(CreateOrderRequest createOrderRequest);
    void update(Order order);
    List<Order> findAll();
    Optional<Order> findById();
    Optional<Order> findByStatus();
    void deleteById(UUID orderId);
    void deleteAll();
}
