CREATE TABLE orders
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    customer_id      BIGINT,
    customer_address VARCHAR(255),
    order_date       date,
    CONSTRAINT pk_order PRIMARY KEY (id)
);