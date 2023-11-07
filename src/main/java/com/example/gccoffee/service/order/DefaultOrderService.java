package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService{
    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAllOrders() {
        return null;
    }

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order(UUID.randomUUID(), new Email(createOrderRequest.email()), createOrderRequest.address(), createOrderRequest.postcode(), OrderStatus.ACCEPTED, LocalDateTime.now(), null);
        return orderRepository.create(order);
    }
}
