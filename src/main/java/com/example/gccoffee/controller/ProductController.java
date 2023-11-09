package com.example.gccoffee.controller;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.service.product.ProductService;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String productsPage(Model model) {
        var products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("new-product")
    public String newProductPage(Model model) {
        var categories = List.of(Category.values());
        model.addAttribute("categories", categories);
        return "new-product";
    }

    @PostMapping
    public String newProduct(CreateProductRequest createProductRequest) {
        productService.createProduct(
                createProductRequest.productName(),
                createProductRequest.category(),
                createProductRequest.price(),
                createProductRequest.description());
        return "redirect:/products";
    }

    @GetMapping("/product-detail/{productId}")
    public String productDetail(@PathVariable UUID productId, Model model) {
        Product product = productService.findById(productId);
        List<Category> categories = List.of(Category.values());

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product-detail";
    }

    @PostMapping("/product-detail/{productId}")
    public ResponseEntity<String> productUpdate(@PathVariable UUID productId,
                                                @ModelAttribute UpdateProductRequest updateProductRequest) {
        productService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/products")
                .body("");
    }
}
