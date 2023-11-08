package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.repository.order.OrderJdbcRepository;
import com.example.gccoffee.repository.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)class DefaultOrderServiceTest {
    @Mock
    private OrderJdbcRepository mockOrderRepository;
    @InjectMocks
    private DefaultOrderService mockOrderService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Test
    void createOrder() {
        // Given
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                "test@example.com", "123 Test St", "12345", null);
        // Mock the behavior of orderRepository.create to return the same argument passed
        when(mockOrderRepository.create(any(Order.class))).then(AdditionalAnswers.returnsFirstArg());

        // When
        Order order = mockOrderService.createOrder(createOrderRequest);

        // Then
        assertNotNull(order);
        // Verify that orderRepository.create is called with the created order
        verify(mockOrderRepository).create(order);
    }

    @Test
    void updateOrder() {
        // Given
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                "test@example.com", "123 Test St", "12345", null);

        orderService.createOrder(createOrderRequest);
    }
}