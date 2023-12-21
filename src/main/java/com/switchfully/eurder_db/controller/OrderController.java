package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.dto.CreateOrderDto;
import com.switchfully.eurder_db.dto.OrderDto;
import com.switchfully.eurder_db.dto.ReOrderDto;
import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.service.AdminService;
import com.switchfully.eurder_db.service.CustomerService;
import com.switchfully.eurder_db.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
}
