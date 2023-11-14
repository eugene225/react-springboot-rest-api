package com.example.gccoffee;

import com.example.gccoffee.controller.OrderController;
import com.example.gccoffee.controller.dto.CreateOrderRequest;
import com.example.gccoffee.controller.dto.ProductDto;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.model.product.ProductQuantity;
import com.example.gccoffee.repository.order.OrderRepository;
import com.example.gccoffee.repository.product.ProductQuantityJdbcRepository;
import com.example.gccoffee.repository.product.ProductRepository;
import com.example.gccoffee.service.order.OrderService;
import com.example.gccoffee.service.product.ProductQuantityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SpringBootTest
public class OrderConcurrencyTest {
    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductQuantityJdbcRepository productQuantityJdbcRepository;
    @Autowired
    private ProductQuantityService productQuantityService;
    @Autowired
    private ProductRepository productRepository;

    private ProductDto productDto;
    private List<CreateOrderRequest> savedOrderList;

    @BeforeEach
    @Sql("classpath:schema.sql")
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @CsvSource({
            "300, 300, 300, 0, 0",
            "300, 299, 299, 0, 1",
            "300, 301, 300, 1, 0",
            "300, 305, 300, 5, 0"
    })
    @ParameterizedTest
    @DisplayName("동시에 주문 요청을 보내면 재고만큼 승인되고 나머지는 실패한다.")
    void orderConcurrencyTest(int stock, int orderCount, int expectedSuccess, int expectedFail, int expectedStock) {
        productDto = productRepository.insert(new ProductDto(UUID.randomUUID(), "TestProduct", Category.MILK, stock, 1500L, "concurrencyTest", LocalDateTime.now(), null));
        savedOrderList = getSavedOrderList(orderCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        executeConcurrentActions(orderIndex -> {
            try{
                orderController.createOrder(savedOrderList.get(orderIndex));
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }, orderCount, 2);

        ProductQuantity productQuantity = productQuantityService.findByProductId(productDto.productId());

        assertThat(productQuantity.getQuantity()).isEqualTo(expectedStock);
        assertThat(successCount.get()).isEqualTo(expectedSuccess);
        assertThat(failCount.get()).isEqualTo(expectedFail);
    }

    @Test
    void orderTest() {
        productDto = productRepository.insert(new ProductDto(UUID.randomUUID(), "TestProduct", Category.MILK, 10, 1500L, "concurrencyTest", LocalDateTime.now(), null));
        savedOrderList = getSavedOrderList(10);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for(CreateOrderRequest order : savedOrderList) {
            try {
                orderController.createOrder(order);
                successCount.incrementAndGet();
            }catch (Exception e) {
                failCount.incrementAndGet();
            }
        }

        ProductQuantity productQuantity = productQuantityService.findByProductId(productDto.productId());

        assertThat(productQuantity.getQuantity()).isEqualTo(0);
        assertThat(successCount.get()).isEqualTo(10);
        assertThat(failCount.get()).isEqualTo(0);
    }

    private List<CreateOrderRequest> getSavedOrderList(int orderCount) {
        List<CreateOrderRequest> orders = new ArrayList<>();
        for(long i = 1; i<=orderCount; i++) {
            final int quantity = 1;
            UUID orderId = UUID.randomUUID();
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(new OrderItem(orderId, productDto.productId(), productDto.productName(), productDto.price(), quantity));
            CreateOrderRequest order = new CreateOrderRequest("test"+i+"@example.com", "TestAddress"+i, "TestPostcode"+i, orderItems);
            orders.add(order);
        }
        return orders;
    }

    private void executeConcurrentActions(Consumer<Integer> action, int repeatCount, int threadSize) {
        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(repeatCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);

        for(int i=1; i<=repeatCount; i++) {
            executorService.execute(() -> {
                int index = atomicInteger.incrementAndGet() - 1;
                action.accept(index);
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
