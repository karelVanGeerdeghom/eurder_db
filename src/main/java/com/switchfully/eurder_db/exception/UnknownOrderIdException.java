package com.switchfully.eurder_db.exception;

public class UnknownOrderIdException extends RuntimeException {
    public UnknownOrderIdException() {
        super("Unknown order id");
    }
}
