package com.example.gccoffee.repository.product;

import com.example.gccoffee.model.product.ProductQuantity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.example.gccoffee.Utils.toUUID;

@Repository
public class ProductQuantityJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductQuantityJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ProductQuantity> findByProductId(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM product_quantity WHERE product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", productId.toString()), productQuantityRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductQuantity update(UUID productId, int quantity) {
        var parameters = new MapSqlParameterSource()
                .addValue("productId", productId.toString())
                .addValue("quantity", quantity);
        var update = jdbcTemplate.update("UPDATE product_quantity SET quantity = :quantity WHERE product_id = UUID_TO_BIN(:productId)",
                parameters);
        if(update != 1) {
            throw new RuntimeException("Nothing was updated!");
        }
        return new ProductQuantity(productId, quantity);
    }

    private static final RowMapper<ProductQuantity> productQuantityRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var quantity = resultSet.getInt("quantity");
        return new ProductQuantity(productId, quantity);
    };
}
