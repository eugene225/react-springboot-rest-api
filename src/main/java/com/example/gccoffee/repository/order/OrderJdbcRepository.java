package com.example.gccoffee.repository.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.gccoffee.Utils.toLocalDateTime;
import static com.example.gccoffee.Utils.toUUID;

public class OrderJdbcRepository implements OrderRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order create(CreateOrderRequest createOrderRequest) {
        return null;
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("select * from orders", orderRowMapper);
    }

    @Override
    public Optional<Order> findById() {
        return Optional.empty();
    }

    @Override
    public Optional<Order> findByStatus() {
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID orderId) {

    }

    @Override
    public void deleteAll() {

    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = resultSet.getString("email");
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, new Email(email), address, postcode, orderStatus, createdAt, updatedAt);
    };
}
