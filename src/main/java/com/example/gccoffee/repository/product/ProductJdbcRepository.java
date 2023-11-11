package com.example.gccoffee.repository.product;

import com.example.gccoffee.controller.dto.ProductDto;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.gccoffee.Utils.*;

@Repository
public class ProductJdbcRepository implements ProductRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products", productRowMapper);
    }

    @Override
    public List<ProductDto> findAllDto() {
        return jdbcTemplate.query("SELECT p.product_id, p.product_name, p.category, p.price, p.description, p.created_at AS created_at, p.updated_at AS updated_at, pq.quantity AS quantity\n" +
                "FROM products p LEFT JOIN product_quantity pq ON p.product_id = pq.product_id", productDtoRowMapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductDto insert(ProductDto productDto) {
        var update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)" +
                " VALUES (UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)", toParamMap(productDto));
        if(update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        var update1 = jdbcTemplate.update("INSERT INTO product_quantity(product_id, quantity) VALUES(UUID_TO_BIN(:productId), :quantity)", toQuantityParamMap(productDto));
        if(update1 != 1) {
            throw new RuntimeException("INSERT FAIL : product_quantity");
        }
        return productDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UUID productId, UpdateProductRequest updateProductRequest) {
        var parameters = new MapSqlParameterSource()
                .addValue("productId", productId.toString())
                .addValue("productName", updateProductRequest.productName())
                .addValue("category", updateProductRequest.category().toString())
                .addValue("price", updateProductRequest.price())
                .addValue("description", updateProductRequest.description())
                .addValue("updatedAt", LocalDateTime.now());
        var update = jdbcTemplate.update(
                "UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, updated_at = :updatedAt" +
                " WHERE product_id = UUID_TO_BIN(:productId)",
                parameters
        );
        if(update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_name = :productName",
                            Collections.singletonMap("productName", productName), productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(
                "SELECT * FROM products WHERE category = :category",
                Collections.singletonMap("category", category.toString()), productRowMapper
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(UUID productId) {
        jdbcTemplate.update("DELETE FROM product_quantity WHERE product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString()));
        jdbcTemplate.update("DELETE FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString()));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private static final RowMapper<ProductDto> productDtoRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var quantity = resultSet.getInt("quantity");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new ProductDto(productId, productName, category, quantity, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(ProductDto productDto){
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", productDto.productId().toString());
        paramMap.put("productName", productDto.productName());
        paramMap.put("category", productDto.category().toString());
        paramMap.put("price", productDto.price());
        paramMap.put("description", productDto.description());
        paramMap.put("createdAt", productDto.createdAt());
        paramMap.put("updatedAt", productDto.updatedAt());
        return paramMap;
    }

    private Map<String, Object> toQuantityParamMap(ProductDto productDto){
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", productDto.productId().toString());
        paramMap.put("quantity", productDto.quantity());
        return paramMap;
    }
}
