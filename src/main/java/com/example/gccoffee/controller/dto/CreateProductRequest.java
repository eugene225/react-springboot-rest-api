package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.product.Category;

public record CreateProductRequest(String productName, Category category, int quantity, long price, String description) {
}
