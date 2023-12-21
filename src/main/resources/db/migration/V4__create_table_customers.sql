CREATE TABLE customers
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email        VARCHAR(255),
    password     VARCHAR(255),
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    phone_number VARCHAR(255),
    address      VARCHAR(255),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);