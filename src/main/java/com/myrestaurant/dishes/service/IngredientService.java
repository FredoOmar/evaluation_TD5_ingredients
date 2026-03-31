package com.myrestaurant.dishes.service;

import com.myrestaurant.dishes.dto.IngredientDTO;
import com.myrestaurant.dishes.dto.StockValueDTO;
import com.myrestaurant.dishes.entity.Ingredients;
import com.myrestaurant.dishes.entity.StockValue;
import com.myrestaurant.dishes.entity.Unit;
import com.myrestaurant.dishes.exception.ResourceNotFoundException;
import com.myrestaurant.dishes.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    // GET /ingredients
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // GET /ingredients/{id}
    public IngredientDTO getIngredientById(Integer id) {
        Ingredients ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient.id=" + id + " is not found"));
        return toDTO(ingredient);
    }

    // GET /ingredients/{id}/stock?at={temporal}&unit={unit}
    public StockValueDTO getStockAt(Integer id, Instant at, Unit unit) {
        if (at == null || unit == null) {
            throw new IllegalArgumentException(
                    "Either mandatory query parameter `at` or `unit` is not provided."
            );
        }
        Ingredients ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient.id=" + id + " is not found"));

        StockValue stockValue = ingredient.getStockValueAt(at);
        if (stockValue == null) {
            return new StockValueDTO(0.0, unit);
        }
        return new StockValueDTO(stockValue.getQuantity(), stockValue.getUnit());
    }

    private IngredientDTO toDTO(Ingredients ingredient) {
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCategory(),
                ingredient.getPrice()
        );
    }
}