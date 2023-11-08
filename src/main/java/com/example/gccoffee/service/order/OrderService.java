package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    List<Order> findAllOrders();
    Order createOrder(CreateOrderRequest createOrderRequest);
    void update(Order order);
    Optional<Order> findById(UUID orderId);
    List<Order> findByStatus(OrderStatus orderStatus);
    void deleteById(UUID orderId);
    void deleteAll();
}
