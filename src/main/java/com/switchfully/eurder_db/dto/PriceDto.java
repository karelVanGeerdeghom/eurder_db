package com.switchfully.eurder_db.dto;

import com.switchfully.eurder_db.entity.Currency;
import jakarta.validation.constraints.NotBlank;

public class PriceDto {
    private Double amount;
    private Currency currency;

    public PriceDto() {}

    public PriceDto(Double amount, Currency currency) {
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
