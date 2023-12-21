package com.switchfully.eurder_db.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="amount")
    private Double amount;
    @Column(name ="currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Price() {}

    public Price(Double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
