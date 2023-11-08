DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_items;

create table products
(
    product_id BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price bigint NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL
);

create table orders
(
    order_id BINARY(16) PRIMARY KEY,
    email VARCHAR(30) NOT NULL,
    address VARCHAR(30) NOT NULL,
    postcode VARCHAR(20) NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL
);

create table order_items
(
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    product_name VARCHAR(20) NOT NULL,
    price BIGINT NOT NULL,
    quantity int NOT NULL
)
