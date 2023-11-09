package com.example.gccoffee.repository;

import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import com.example.gccoffee.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductJdbcRepositoryTest {

    @Autowired
    ProductRepository repository;

    private final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1000L);

    @Test
    @DisplayName("상품을 추가할 수 있다")
    void testInsert() {
        repository.insert(newProduct);
        var all = repository.findAll();
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @DisplayName("상품을 이름으로 조회할 수 있다")
    void testFindByName() {
        repository.insert(newProduct);
        var product = repository.findByName(newProduct.getProductName());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @DisplayName("상품을 상품아이디로 조회할 수 있다")
    void testFindById() {
        repository.insert(newProduct);
        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @DisplayName("상품들을 카테고리로 조회할 수 있다")
    void testFindByCategory() {
        repository.insert(newProduct);
        var product = repository.findByCategory(newProduct.getCategory());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @DisplayName("상품을 수정할 수 있다.")
    void testUpdate() {
        repository.insert(newProduct);
        newProduct.setProductName("updated-product");
        //repository.update(newProduct);

        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get().getProductName(), is(newProduct.getProductName()));
    }

    @Test
    @DisplayName("상품을 전체 삭제한다")
    void testDeleteAll() {
        repository.insert(newProduct);
        repository.deleteAll();
        var all = repository.findAll();
        assertThat(all.isEmpty(), is(true));
    }

}