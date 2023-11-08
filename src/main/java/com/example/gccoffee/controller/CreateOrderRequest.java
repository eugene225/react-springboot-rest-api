package com.example.gccoffee.controller;

import com.example.gccoffee.model.order.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        String email,
        String address,
        String postcode,
        List<OrderItem> orderItems) {
}
