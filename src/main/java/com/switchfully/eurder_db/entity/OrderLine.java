package com.switchfully.eurder_db.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "order_lines")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "item_name")
    private String itemName;
    @OneToOne(cascade = {PERSIST})
    @JoinColumn(name = "item_price_id")
    private Price itemPrice;
    @Column(name = "amount_in_order")
    private Integer amountInOrder;
    @Column(name = "shipping_date")
    @Temporal(TemporalType.DATE)
    private LocalDate shippingDate;

    public OrderLine() {}

    public OrderLine(Long itemId, String itemName, Price itemPrice, Integer amountInOrder, LocalDate shippingDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.amountInOrder = amountInOrder;
        this.shippingDate = shippingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Price getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Price itemPrice) {
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

    public Price calculateTotalPrice() {
        return new Price(itemPrice.getAmount() * amountInOrder, Currency.EUR);
    }
}