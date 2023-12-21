package com.switchfully.eurder_db.exception;

public class UnknownCustomerIdException extends RuntimeException {
    public UnknownCustomerIdException() {
        super("Unknown customer id");
    }
}
