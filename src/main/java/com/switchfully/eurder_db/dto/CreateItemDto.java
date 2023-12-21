package com.switchfully.eurder_db.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private CreatePriceDto price;
    @NotNull
    private Integer amountInStock;

    public CreateItemDto() {}

    public CreateItemDto(String name, String description, CreatePriceDto price, Integer amountInStock) {
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

    public CreatePriceDto getPrice() {
        return price;
    }

    public void setPrice(CreatePriceDto price) {
        this.price = price;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Integer amountInStock) {
        this.amountInStock = amountInStock;
    }
}
