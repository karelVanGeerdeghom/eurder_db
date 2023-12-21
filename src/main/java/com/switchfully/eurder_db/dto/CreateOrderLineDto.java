package com.switchfully.eurder_db.dto;

import jakarta.validation.constraints.NotNull;

public class CreateOrderLineDto {
    @NotNull
    private Long itemId;
    @NotNull
    private Integer amountInOrder;

    public CreateOrderLineDto(Long itemId, Integer amountInOrder) {
        this.itemId = itemId;
        this.amountInOrder = amountInOrder;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getAmountInOrder() {
        return amountInOrder;
    }

    public void setAmountInOrder(Integer amountInOrder) {
        this.amountInOrder = amountInOrder;
    }
}
