package com.example.gccoffee.service.product;

import com.example.gccoffee.exception.InventoryShortageException;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.product.ProductQuantity;
import com.example.gccoffee.repository.product.ProductQuantityJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void updateQuantityByOrderItems(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            ProductQuantity productQuantity = productQuantityJdbcRepository.findByProductId(orderItem.getProductId()).get();
            int availableQuantity = productQuantity.getQuantity();

            if (availableQuantity < orderItem.getQuantity()) {
                throw new InventoryShortageException("[재고부족] 상품 : " + orderItem.getProductName() + ", 남은수량 : " + availableQuantity);
            }
        }

        orderItems.forEach(orderItem -> {
            ProductQuantity productQuantity = productQuantityJdbcRepository.findByProductId(orderItem.getProductId()).get();
            int availableQuantity = productQuantity.getQuantity();
            productQuantityJdbcRepository.update(orderItem.getProductId(), availableQuantity - orderItem.getQuantity());
        });
    }
}
