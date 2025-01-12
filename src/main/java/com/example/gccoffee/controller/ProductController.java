package com.example.gccoffee.controller;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.ProductDto;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.model.product.ProductQuantity;
import com.example.gccoffee.service.product.ProductQuantityService;
import com.example.gccoffee.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;
    private final ProductQuantityService productQuantityService;

    public ProductController(ProductService productService, ProductQuantityService productQuantityService) {
        this.productService = productService;
        this.productQuantityService = productQuantityService;
    }

    @GetMapping
    public String productsPage(Model model) {
        var products = productService.getAllProductDtos();
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
        productService.createProduct(createProductRequest);
        return "redirect:/products";
    }

    @GetMapping("/product-detail/{productId}")
    public String productDetail(@PathVariable UUID productId, Model model) {
        ProductDto productDto = productService.findById(productId);
        List<Category> categories = List.of(Category.values());

        model.addAttribute("product", productDto);
        model.addAttribute("categories", categories);
        return "product-detail";
    }

    @PostMapping("/product-detail/{productId}")
    public ResponseEntity<String> productUpdate(@PathVariable UUID productId,
                                                @ModelAttribute UpdateProductRequest updateProductRequest) {
        productService.updateProduct(productId, updateProductRequest);
        productQuantityService.updateQuantity(productId, updateProductRequest.quantity());
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/products")
                .body("");
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable UUID productId, RedirectAttributes redirectAttributes) {
        productService.deleteById(productId);
        redirectAttributes.addFlashAttribute("successMessage", "주문이 삭제되었습니다.");

        return "redirect:/products";
    }
}
