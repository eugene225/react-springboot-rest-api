package com.example.gccoffee.repository.order;

import com.example.gccoffee.controller.UpdateOrderRequest;
import com.example.gccoffee.model.order.Email;
import com.example.gccoffee.model.order.Order;
import com.example.gccoffee.model.order.OrderItem;
import com.example.gccoffee.model.order.OrderStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Transactional(rollbackFor = Exception.class)
    public Order create(Order order) {
        var update = jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at)" +
                " VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)", toOrderParamMap(order));
        if(update != 1) {
            throw new RuntimeException("Nothing was inserted");
        } else{
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> {
                var update1 = jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, product_name, price, quantity)" +
                        " VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :productName, :price, :quantity)", toItemParamMap(order, orderItem));
                if(update1 != 1) {
                    throw new RuntimeException("INSERT ERROR : Order_items");
                }
            });
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UUID orderId, UpdateOrderRequest updateOrderRequest) {
        var parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId.toString())
                .addValue("address", updateOrderRequest.address())
                .addValue("postcode", updateOrderRequest.postcode())
                .addValue("orderStatus", updateOrderRequest.orderStatus().toString())
                .addValue("updatedAt", LocalDateTime.now());
        var update = jdbcTemplate.update(
                "UPDATE orders SET address = :address, postcode = :postcode, order_status = :orderStatus, updated_at = :updatedAt" +
                        " WHERE order_id = UUID_TO_BIN(:orderId)", parameters);
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
    public List<OrderItem> findItemsById(UUID orderId) {
        return jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString()), orderItemRowMapper);
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

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");

        return new OrderItem(orderId, productId, productName, price, quantity);
    };

    private Map<String, Object> toOrderParamMap(Order order){
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

    private Map<String, Object> toItemParamMap(Order order, OrderItem orderItem){
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString());
        paramMap.put("productId", orderItem.getProductId().toString());
        paramMap.put("productName", orderItem.getProductName());
        paramMap.put("price", orderItem.getPrice());
        paramMap.put("quantity", orderItem.getQuantity());
        return paramMap;
    }
}
