package com.example.gccoffee.repository.order;

import com.example.gccoffee.controller.dto.UpdateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order create(Order order);
    void update(UUID orderId, UpdateOrderRequest updateOrderRequest);
    List<Order> findAll();
    Optional<Order> findById(UUID orderId);
    List<OrderItem> findItemsById(UUID orderId);
    List<Order> findByStatus(OrderStatus orderStatus);
    void deleteById(UUID orderId);
    void deleteAll();
}
