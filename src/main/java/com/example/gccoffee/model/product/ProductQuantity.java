package com.example.gccoffee.model.product;

import java.util.UUID;

public class ProductQuantity {
    private final UUID productId;
    private int quantity;

    public ProductQuantity(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
