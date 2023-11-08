package com.example.gccoffee.controller;

import com.example.gccoffee.model.order.OrderStatus;

import java.time.LocalDateTime;

public record UpdateOrderRequest(String address, String postcode, OrderStatus orderStatus, LocalDateTime updatedAt) {
}
