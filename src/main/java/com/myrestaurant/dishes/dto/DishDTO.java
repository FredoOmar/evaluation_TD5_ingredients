package com.myrestaurant.dishes.dto;

import com.myrestaurant.dishes.entity.DishTypeEnum;

import java.util.List;

public class DishDTO {
    private Integer id;
    private String name;
    private Double price;
    private DishTypeEnum dishType;
    private List<IngredientDTO> ingredients;

    public DishDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public DishTypeEnum getDishType() { return dishType; }
    public void setDishType(DishTypeEnum dishType) { this.dishType = dishType; }

    public List<IngredientDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDTO> ingredients) { this.ingredients = ingredients; }
}