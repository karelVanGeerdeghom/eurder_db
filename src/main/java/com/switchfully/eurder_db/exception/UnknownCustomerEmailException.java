package com.switchfully.eurder_db.exception;

public class UnknownCustomerEmailException extends RuntimeException {
    public UnknownCustomerEmailException() {
        super("Unknown customer email");
    }
}
