package com.myrestaurant.dishes.dto;

import com.myrestaurant.dishes.entity.Unit;

public class StockValueDTO {
    private Double quantity;
    private Unit unit;

    public StockValueDTO() {}

    public StockValueDTO(Double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
}