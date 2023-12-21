package com.switchfully.eurder_db.exception;

public class NoOrderLinesException extends RuntimeException {
    public NoOrderLinesException() {
        super("No order lines exception");
    }
}
