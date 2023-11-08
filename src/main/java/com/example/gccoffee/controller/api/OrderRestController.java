package com.example.gccoffee.controller.api;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.service.order.OrderService;
import com.example.gccoffee.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;
    private final ProductService productService;

    public OrderRestController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/create-order")
    public String createOrder(@RequestParam(name = "category", required = false) String category, Model model) {
        List<Product> products;
        if(category != null && !category.isEmpty()) {
            products = productService.getProductByCategory(Category.valueOf(category));
        } else {
            products = productService.getAllProducts();
        }

        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "new-order";
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);

        if (order != null) {
            return ResponseEntity.ok("주문이 성공적으로 처리되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 처리에 실패했습니다.");
        }
    }

    @GetMapping
    public String orderList(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @GetMapping("/order-detail/{orderId}")
    public String getOrderDetail(@PathVariable UUID orderId, Model model) {
        Order order = orderService.findById(orderId).get();
        List<OrderItem> orderItems = orderService.findItemsById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "order-detail";
    }

}
