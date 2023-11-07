package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.repository.order.OrderJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DefaultOrderServiceTest {
    @Mock
    private OrderJdbcRepository orderRepository;
    @InjectMocks
    private DefaultOrderService orderService;

    @Test
    void createOrder() {
        // Given
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                "test@example.com", "123 Test St", "12345", null);

        // When
        Order order = orderService.createOrder(createOrderRequest);

        // Then
        assertNotNull(order);
        verify(orderRepository).create(order);
    }
}