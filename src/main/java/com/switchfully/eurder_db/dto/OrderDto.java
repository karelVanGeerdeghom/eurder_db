package com.switchfully.eurder_db.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private Long id;
    private Long customerId;
    private String customerAddress;
    private List<OrderLineDto> orderLines;
    private LocalDate orderDate;
    private PriceDto totalPrice;

    public OrderDto() {}

    public OrderDto(Long id, Long customerId, String customerAddress, List<OrderLineDto> orderLines, LocalDate orderDate, PriceDto totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.customerAddress = customerAddress;
        this.orderLines = orderLines;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLineDto> orderLines) {
        this.orderLines = orderLines;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public PriceDto getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(PriceDto totalPrice) {
        this.totalPrice = totalPrice;
    }
}
