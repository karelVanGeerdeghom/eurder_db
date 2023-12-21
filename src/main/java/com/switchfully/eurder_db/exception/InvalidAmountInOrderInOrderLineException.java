package com.switchfully.eurder_db.exception;

public class InvalidAmountInOrderInOrderLineException extends RuntimeException {
    public InvalidAmountInOrderInOrderLineException() {
        super("Invalid amount in order in orderline");
    }
}