package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.product.Category;

public record UpdateProductRequest (String productName, Category category, long price, String description) {
}
