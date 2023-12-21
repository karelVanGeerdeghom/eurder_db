package com.switchfully.eurder_db.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private UpdatePriceDto price;
    @NotNull
    private Integer amountInStock;

    public UpdateItemDto(String name, String description, UpdatePriceDto price, Integer amountInStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UpdatePriceDto getPrice() {
        return price;
    }

    public void setPrice(UpdatePriceDto price) {
        this.price = price;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Integer amountInStock) {
        this.amountInStock = amountInStock;
    }
}
