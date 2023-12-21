package com.switchfully.eurder_db.exception;

public class UnknownAdminEmailException extends RuntimeException {
    public UnknownAdminEmailException() {
        super("Unknown admin email");
    }
}
