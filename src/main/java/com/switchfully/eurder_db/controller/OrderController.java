package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.dto.CreateOrderDto;
import com.switchfully.eurder_db.dto.OrderDto;
import com.switchfully.eurder_db.dto.ReOrderDto;
import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.exception.InvalidAmountInOrderInOrderLineException;
import com.switchfully.eurder_db.exception.NoOrderLinesException;
import com.switchfully.eurder_db.exception.OrderIsNotForCustomerException;
import com.switchfully.eurder_db.exception.UnknownOrderIdException;
import com.switchfully.eurder_db.service.AdminService;
import com.switchfully.eurder_db.service.CustomerService;
import com.switchfully.eurder_db.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
@RequestMapping(path = "/orders")
public class OrderController {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final OrderService orderService;

    public OrderController(AdminService adminService, CustomerService customerService, OrderService orderService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@RequestHeader String email, @RequestHeader String password, @Valid @RequestBody CreateOrderDto createOrderDto) {
        Customer customer = customerService.authenticate(email, password);

        return orderService.placeOrder(customer, createOrderDto);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto reOrder(@RequestHeader String email, @RequestHeader String password, @PathVariable Long id, @Valid @RequestBody ReOrderDto reOrderDto) {
        Customer customer = customerService.authenticate(email, password);
        orderService.validateOrderByIdForCustomer(customer, id);

        return orderService.reOrder(customer, id, reOrderDto);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderByIdForCustomer(@RequestHeader String email, @RequestHeader String password, @PathVariable Long id) {
        Customer customer = customerService.authenticate(email, password);
        orderService.validateOrderByIdForCustomer(customer, id);

        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderDto> findAllOrdersForCustomer(@RequestHeader String email, @RequestHeader String password) {
        Customer customer = customerService.authenticate(email, password);

        return orderService.findAllOrdersForCustomer(customer);
    }

    @GetMapping("/shipping-date/{shippingDate}")
    public List<OrderDto> findAllOrdersForShippingDate(@RequestHeader String email, @RequestHeader String password, @PathVariable LocalDate shippingDate) {
        adminService.authenticate(email, password);

        return orderService.findAllOrdersForShippingDate(shippingDate);
    }

    @ExceptionHandler(UnknownOrderIdException.class)
    protected void unknownOrderIdException(UnknownOrderIdException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(InvalidAmountInOrderInOrderLineException.class)
    protected void invalidAmountInOrderInOrderLineException(InvalidAmountInOrderInOrderLineException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(NoOrderLinesException.class)
    protected void noOrderLinesException(NoOrderLinesException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(OrderIsNotForCustomerException.class)
    protected void orderIsNotForCustomerException(OrderIsNotForCustomerException e, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }
}
