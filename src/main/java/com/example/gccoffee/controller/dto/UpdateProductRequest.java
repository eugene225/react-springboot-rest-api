package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.product.Category;

public record UpdateProductRequest (String productName, Category category, int quantity, long price, String description) {
}
