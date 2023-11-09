package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.product.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(UUID productId, String productName, Category category, int quantity, long price, String description,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
}
