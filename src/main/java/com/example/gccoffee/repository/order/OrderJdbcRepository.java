package com.example.gccoffee.repository.order;

import com.example.gccoffee.controller.CreateOrderRequest;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderStatus;
import com.example.gccoffee.model.product.Category;
import com.example.gccoffee.model.product.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.gccoffee.Utils.toLocalDateTime;
import static com.example.gccoffee.Utils.toUUID;

@Repository
public class OrderJdbcRepository implements OrderRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order create(Order order) {
        var update = jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at)" +
                " VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)", toParamMap(order));
        if(update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        return order;
    }

    @Override
    public void update(Order order) {
        var update = jdbcTemplate.update(
                "UPDATE orders SET email = :email, address = :address, postcode = :postcode, order_status = :orderStatus, created_at = :createdAt, updated_at = :updatedAt" +
                        " WHERE order_id = UUID_TO_BIN(:orderId)",
                toParamMap(order)
        );
        if(update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("select * from orders", orderRowMapper);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString()), orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus orderStatus) {
        return jdbcTemplate.query("select * from orders WHERE order_status = :orderStatus",
                Collections.singletonMap("orderStatus", orderStatus.toString()),
                orderRowMapper);
    }

    @Override
    public void deleteById(UUID orderId) {
        jdbcTemplate.update("DELETE FROM orders where order_id = UUID_TO_BIN(:orderId)"
                , Collections.singletonMap("orderId", orderId.toString()));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Order order){
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }
}
