package com.example.gccoffee.service.product;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.ProductDto;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<Product> getProductByCategory(Category category);
    List<Product> getAllProducts();
    List<ProductDto> getAllProductDtos();
    ProductDto createProduct(CreateProductRequest createProductRequest);
    Product findById(UUID productId);
    void updateProduct(UUID productId, UpdateProductRequest updateProductRequest);
}
