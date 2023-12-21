package com.switchfully.eurder_db.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReOrderDto {
    @NotNull
    private LocalDate orderDate;

    public ReOrderDto() {}

    public ReOrderDto(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
