package com.switchfully.eurder_db.exception;

public class UnknownItemIdException extends RuntimeException {
    public UnknownItemIdException() {
        super("Unknown item id");
    }
}
