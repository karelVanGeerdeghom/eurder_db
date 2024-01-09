package com.switchfully.eurder_db.exception;

public class WrongCustomerIdException extends RuntimeException {
    public WrongCustomerIdException() {
        super("Wrong customer id");
    }
}
