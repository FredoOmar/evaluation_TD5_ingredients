package com.myrestaurant.dishes.entity;

import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Integer ingredientId;
    private MovementTypeEnum type;
    private StockValue value;
    private Instant creationDatetime;

    public StockMovement() {
        this.creationDatetime = Instant.now();
    }

    public StockMovement(MovementTypeEnum type, StockValue value) {
        this.type = type;
        this.value = value;
        this.creationDatetime = Instant.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIngredientId() { return ingredientId; }
    public void setIngredientId(Integer ingredientId) { this.ingredientId = ingredientId; }

    public MovementTypeEnum getType() { return type; }
    public void setType(MovementTypeEnum type) { this.type = type; }

    public StockValue getValue() { return value; }
    public void setValue(StockValue value) { this.value = value; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    @Override
    public String toString() {
        return "StockMovement{type=" + type + ", value=" + value + ", date=" + creationDatetime + '}';
    }
}