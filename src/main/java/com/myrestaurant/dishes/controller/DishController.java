package com.myrestaurant.dishes.controller;

import com.myrestaurant.dishes.dto.DishDTO;
import com.myrestaurant.dishes.dto.IngredientDTO;
import com.myrestaurant.dishes.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    // d) GET /dishes
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    // e) PUT /dishes/{id}/ingredients
    @PutMapping("/{id}/ingredients")
    public ResponseEntity<DishDTO> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody(required = false) List<IngredientDTO> ingredients
    ) {
        if (ingredients == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dishService.updateDishIngredients(id, ingredients));
    }

    // EVALUATION : GET /dishes/{id}/ingredients?ingredientName={i}&ingredientPriceAround={p}
    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientDTO>> getDishIngredients(
            @PathVariable Integer id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround
    ) {
        return ResponseEntity.ok(dishService.getDishIngredients(id, ingredientName, ingredientPriceAround));
    }
}