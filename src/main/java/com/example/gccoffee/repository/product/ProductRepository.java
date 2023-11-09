package com.example.gccoffee.repository.product;

import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();
    Product insert(Product product);
    void update(UUID productId, UpdateProductRequest updateProductRequest);
    Optional<Product> findById(UUID productId);
    Optional<Product> findByName(String productName);
    List<Product> findByCategory(Category category);
    void deleteAll();
}
