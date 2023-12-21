package com.switchfully.eurder_db.dto;

import com.switchfully.eurder_db.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePriceDto {
    @NotNull
    private Double amount;
    @NotBlank
    private Currency currency;

    public CreatePriceDto() {}

    public CreatePriceDto(Double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
