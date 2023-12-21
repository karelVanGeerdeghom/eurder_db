package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.exception.UnknownAdminEmailException;
import com.switchfully.eurder_db.exception.UnknownItemIdException;
import com.switchfully.eurder_db.exception.WrongPasswordException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UnknownAdminEmailException.class)
    protected void unknownAdminEmailException(UnknownAdminEmailException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(UnknownItemIdException.class)
    protected void unknownItemIdException(UnknownItemIdException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(WrongPasswordException.class)
    protected void wrongPasswordException(WrongPasswordException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }
}
