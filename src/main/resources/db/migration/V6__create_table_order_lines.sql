CREATE TABLE order_lines
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id         BIGINT,
    item_name       VARCHAR(255),
    item_price_id   BIGINT,
    amount_in_order INTEGER,
    shipping_date   date,
    order_id        BIGINT,
    CONSTRAINT pk_order_lines PRIMARY KEY (id)
);

ALTER TABLE order_lines
    ADD CONSTRAINT uc_order_lines_item_price UNIQUE (item_price_id);

ALTER TABLE order_lines
    ADD CONSTRAINT FK_ORDER_LINES_ON_ITEM_PRICE FOREIGN KEY (item_price_id) REFERENCES prices (id);

ALTER TABLE order_lines
    ADD CONSTRAINT FK_ORDER_LINES_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);