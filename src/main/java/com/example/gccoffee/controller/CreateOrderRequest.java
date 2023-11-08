package com.example.gccoffee.controller;

import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        String email,
        String address,
        String postcode,
        List<OrderItem> orderItems) {

    public Order toEntity() {
        return new Order(
                UUID.randomUUID(),
                new Email(this.email),
                this.address,
                this.postcode,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                null
        );
    }
}
