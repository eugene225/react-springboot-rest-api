package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.dto.CreateOrderRequest;
import com.example.gccoffee.controller.dto.UpdateOrderRequest;
import com.example.gccoffee.exception.InventoryShortageException;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.model.product.ProductQuantity;
import com.example.gccoffee.repository.order.OrderRepository;
import com.example.gccoffee.repository.product.ProductQuantityJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductQuantityJdbcRepository productQuantityJdbcRepository;

    public DefaultOrderService(OrderRepository orderRepository, ProductQuantityJdbcRepository productQuantityJdbcRepository) {
        this.orderRepository = orderRepository;
        this.productQuantityJdbcRepository = productQuantityJdbcRepository;
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order(UUID.randomUUID(), new Email(createOrderRequest.email()), createOrderRequest.address(), createOrderRequest.postcode(), createOrderRequest.orderItems(), OrderStatus.ACCEPTED, LocalDateTime.now(), null);
        List<OrderItem> orderItems = createOrderRequest.orderItems();
        for (OrderItem orderItem : orderItems) {
            ProductQuantity productQuantity = productQuantityJdbcRepository.findByProductId(orderItem.getProductId()).get();
            int availableQuantity = productQuantity.getQuantity();

            if (availableQuantity < orderItem.getQuantity()) {
                throw new InventoryShortageException("[재고부족] 상품 : " + orderItem.getProductName() + ", 남은수량 : " + availableQuantity);
            }
        }
        orderItems.forEach(orderItem -> {
            ProductQuantity productQuantity = productQuantityJdbcRepository.findByProductId(orderItem.getProductId()).get();
            int availableQuantity = productQuantity.getQuantity();
            productQuantityJdbcRepository.update(orderItem.getProductId(), availableQuantity - orderItem.getQuantity());
        });
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
