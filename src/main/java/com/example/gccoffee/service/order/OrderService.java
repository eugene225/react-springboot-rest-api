package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAllOrders();
    Order createOrder(CreateOrderRequest createOrderRequest);
}
