package com.switchfully.eurder_db.exception;

public class OrderIsNotForCustomerException extends RuntimeException {
    public OrderIsNotForCustomerException() {
        super("Order is not for customer");
    }
}
