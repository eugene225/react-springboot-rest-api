package com.example.gccoffee.controller.api;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.service.order.OrderService;
import com.example.gccoffee.service.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
    public String submitOrder(@RequestParam("email") String email,
                              @RequestParam("address") String address,
                              @RequestParam("postcode") String postcode,
                              @ModelAttribute("selectedItems") List<OrderItem> selectedItems,
                              Model model) {
        // 여기서 주문 정보를 처리하고 저장하는 로직을 구현
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(email, address, postcode, selectedItems);
        orderService.createOrder(createOrderRequest);

        // 주문 정보 처리 후, 화면에 메시지를 전달할 수 있음
        model.addAttribute("message", "주문이 성공적으로 완료되었습니다.");

        return "redirect:/orders/create-order"; // 주문 완료 페이지로 이동
    }

}
