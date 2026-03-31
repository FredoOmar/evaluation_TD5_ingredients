package com.myrestaurant.dishes.entity;

public class DishIngredient {
    private Integer id;
    private Dish dish;
    private Ingredients ingredient;
    private Double quantity;
    private Unit unit;

    public DishIngredient() {}

    public DishIngredient(Dish dish, Ingredients ingredient, Double quantity, Unit unit) {
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public Ingredients getIngredient() { return ingredient; }
    public void setIngredient(Ingredients ingredient) { this.ingredient = ingredient; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    @Override
    public String toString() {
        return "DishIngredient{ingredient=" + ingredient + ", quantity=" + quantity + ", unit=" + unit + '}';
    }
}