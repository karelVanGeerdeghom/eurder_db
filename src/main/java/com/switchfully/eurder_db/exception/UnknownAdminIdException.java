package com.switchfully.eurder_db.exception;

public class UnknownAdminIdException extends RuntimeException {
    public UnknownAdminIdException() {
        super("Unknown admin id");
    }
}
