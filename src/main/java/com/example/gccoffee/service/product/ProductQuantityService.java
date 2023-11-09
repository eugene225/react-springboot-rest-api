package com.example.gccoffee.service.product;

import com.example.gccoffee.model.product.ProductQuantity;
import com.example.gccoffee.repository.product.ProductQuantityJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductQuantityService {
    private final ProductQuantityJdbcRepository productQuantityJdbcRepository;

    public ProductQuantityService(ProductQuantityJdbcRepository productQuantityJdbcRepository) {
        this.productQuantityJdbcRepository = productQuantityJdbcRepository;
    }

    public ProductQuantity findByProductId(UUID productId) {
        return productQuantityJdbcRepository.findByProductId(productId).get();
    }

    public ProductQuantity updateQuantity(UUID productId, int quantity) {
        return productQuantityJdbcRepository.update(productId, quantity);
    }
}
