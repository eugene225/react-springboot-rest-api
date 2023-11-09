package com.example.gccoffee.service.product;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.ProductDto;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultProductService implements ProductService{
    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> getAllProductDtos() {
        return productRepository.findAllDto();
    }

    @Override
    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        var product = new ProductDto(UUID.randomUUID(), createProductRequest.productName(), createProductRequest.category(), createProductRequest.quantity(), createProductRequest.price(), createProductRequest.description()
        ,LocalDateTime.now(), null);
        return productRepository.insert(product);
    }

    @Override
    public Product findById(UUID productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public void updateProduct(UUID productId, UpdateProductRequest updateProductRequest) {
        productRepository.update(productId, updateProductRequest);
    }
}
