package com.switchfully.eurder_db.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class CreateOrderDto {
    @Valid
    private List<CreateOrderLineDto> orderLines;
    @NotNull
    private LocalDate orderDate;

    public CreateOrderDto() {}

    public CreateOrderDto(List<CreateOrderLineDto> orderLines, LocalDate orderDate) {
        this.orderLines = orderLines;
        this.orderDate = orderDate;
    }

    public List<CreateOrderLineDto> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<CreateOrderLineDto> orderLines) {
        this.orderLines = orderLines;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
