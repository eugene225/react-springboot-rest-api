package com.example.gccoffee.model.order;

import java.util.UUID;

public class OrderItem {
    private final UUID productId;
    private final String productName;
    private final long price;
    private final int quantity;

    public OrderItem(UUID productId, String productName, long price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
