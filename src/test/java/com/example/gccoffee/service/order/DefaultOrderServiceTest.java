package com.example.gccoffee.service.order;

import com.example.gccoffee.controller.dto.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.repository.order.OrderJdbcRepository;
import com.example.gccoffee.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

/*

1. 화면을 두개로 나누어서 왼쪽에는 내가 추가할 수 있는 상품의 목록이 카테고리별로 구분되어서 볼 수 있도록,
오른쪽에는 추가한 상품의 목록이 보이고, 이메일, 주소, 우편번호를 입력하는 창이 있고 총 가격이 보이도록 하고 싶어
왼쪽 창에 보이는 상품 목록을 표로 정리해서 깔끔하게 만들어줘
2. 스프링 프로젝트에서 추가할 수 있는 상품의 목록을 GET으로 가져올거야
3. 오른쪽 화면에 있는 추가된 상품 목록, 추가된 상품의 개수, 이메일, 주소, 우편번호를 POST로 서버로 보낼거야
4. 추가할 수 있는 상품 옆에는 추가 버튼이, 오른쪽 화면의 선택한 상품 옆에는 수량을 표시하고 삭제할 수 있는 버튼이 필요해
5. 적당한 부트스트랩 테마를 적용하고, 꼭 타임리프를 적용해줘


 */