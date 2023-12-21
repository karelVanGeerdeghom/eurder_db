package com.switchfully.eurder_db.dto;

import com.switchfully.eurder_db.entity.StockIndicator;

import java.util.Objects;

public class ItemStockIndicatorDto {
    private Long id;
    private String name;
    private StockIndicator stockIndicator;

    public ItemStockIndicatorDto() {}

    public ItemStockIndicatorDto(Long id, String name, StockIndicator stockIndicator) {
        this.id = id;
        this.name = name;
        this.stockIndicator = stockIndicator;
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

    public StockIndicator getStockIndicator() {
        return stockIndicator;
    }

    public void setStockIndicator(StockIndicator stockIndicator) {
        this.stockIndicator = stockIndicator;
    }
}
