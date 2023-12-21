package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.dto.CreateCustomerDto;
import com.switchfully.eurder_db.dto.CustomerDto;
import com.switchfully.eurder_db.exception.InvalidAmountInOrderInOrderLineException;
import com.switchfully.eurder_db.exception.NoOrderLinesException;
import com.switchfully.eurder_db.exception.UnknownCustomerEmailException;
import com.switchfully.eurder_db.exception.UnknownCustomerIdException;
import com.switchfully.eurder_db.service.AdminService;
import com.switchfully.eurder_db.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
@RequestMapping(path = "/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AdminService adminService;

    public CustomerController(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        return customerService.createCustomer(createCustomerDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomer(@RequestHeader String email, @RequestHeader String password, @PathVariable Long id) {
        adminService.authenticate(email, password);

        return customerService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> findAllCustomers(@RequestHeader String email, @RequestHeader String password) {
        adminService.authenticate(email, password);

        return customerService.findAllCustomers();
    }

    @ExceptionHandler(UnknownCustomerEmailException.class)
    protected void unknownCustomerEmailException(UnknownCustomerEmailException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(UnknownCustomerIdException.class)
    protected void unknownCustomerIdException(UnknownCustomerIdException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }
}
