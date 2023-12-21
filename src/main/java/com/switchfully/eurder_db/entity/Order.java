package com.switchfully.eurder_db.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "customer_address")
    private String customerAddress;
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private LocalDate orderDate;
    @OneToMany(cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLine> orderLines;

    public Order() {}

    public Order(Long customerId, String customerAddress, LocalDate orderDate, List<OrderLine> orderLines) {
        this.customerId = customerId;
        this.customerAddress = customerAddress;
        this.orderDate = orderDate;
        this.orderLines = orderLines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public Price calculateTotalPrice() {
        double totalPriceAmount = orderLines.stream().reduce(0.0, (totalPrice, orderLine) -> totalPrice + orderLine.calculateTotalPrice().getAmount(), Double::sum);

        return new Price(totalPriceAmount, Currency.EUR);
    }
}