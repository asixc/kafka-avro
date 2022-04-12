package com.example.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Car {
    private Long id;
    private String name;
    private BigDecimal cost;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayPurchase;

    public Car() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDate getDayPurchase() {
        return dayPurchase;
    }

    public void setDayPurchase(LocalDate dayPurchase) {
        this.dayPurchase = dayPurchase;
    }
}
