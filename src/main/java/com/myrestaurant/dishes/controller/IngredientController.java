package com.myrestaurant.dishes.controller;

import com.myrestaurant.dishes.dto.IngredientDTO;
import com.myrestaurant.dishes.dto.StockValueDTO;
import com.myrestaurant.dishes.entity.Unit;
import com.myrestaurant.dishes.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<StockValueDTO> getIngredientStock(
            @PathVariable Integer id,
            @RequestParam(required = false) Instant at,
            @RequestParam(required = false) Unit unit
    ) {
        return ResponseEntity.ok(ingredientService.getStockAt(id, at, unit));
    }
}