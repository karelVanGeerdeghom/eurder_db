package com.switchfully.eurder_db.dto;

import java.time.LocalDate;

public class OrderLineDto {
    private Long itemId;
    private String itemName;
    private PriceDto itemPrice;
    private Integer amountInOrder;
    private LocalDate shippingDate;
    private PriceDto totalPrice;

    public OrderLineDto() {}

    public OrderLineDto(Long itemId, String itemName, PriceDto itemPrice, Integer amountInOrder, LocalDate shippingDate, PriceDto totalPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.amountInOrder = amountInOrder;
        this.shippingDate = shippingDate;
        this.totalPrice = totalPrice;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PriceDto getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(PriceDto itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmountInOrder() {
        return amountInOrder;
    }

    public void setAmountInOrder(Integer amountInOrder) {
        this.amountInOrder = amountInOrder;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public PriceDto getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(PriceDto totalPrice) {
        this.totalPrice = totalPrice;
    }
}
