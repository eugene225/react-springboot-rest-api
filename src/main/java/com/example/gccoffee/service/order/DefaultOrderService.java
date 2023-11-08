package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.controller.UpdateOrderRequest;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService{
    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order(UUID.randomUUID(), new Email(createOrderRequest.email()), createOrderRequest.address(), createOrderRequest.postcode(), createOrderRequest.orderItems(), OrderStatus.ACCEPTED, LocalDateTime.now(), null);
        return orderRepository.create(order);
    }

    @Override
    public void update(UUID orderId, UpdateOrderRequest updateOrderRequest) {
        orderRepository.update(orderId, updateOrderRequest);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<OrderItem> findItemsById(UUID orderId) {
        return orderRepository.findItemsById(orderId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus);
    }

    @Override
    public void deleteById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }
}
