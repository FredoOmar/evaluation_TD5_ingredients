package com.myrestaurant.dishes.service;

import com.myrestaurant.dishes.dto.DishDTO;
import com.myrestaurant.dishes.dto.IngredientDTO;
import com.myrestaurant.dishes.entity.Dish;
import com.myrestaurant.dishes.entity.DishIngredient;
import com.myrestaurant.dishes.entity.Ingredients;
import com.myrestaurant.dishes.exception.ResourceNotFoundException;
import com.myrestaurant.dishes.repository.DishRepository;
import com.myrestaurant.dishes.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishService(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // GET /dishes
    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // PUT /dishes/{id}/ingredients
    public DishDTO updateDishIngredients(Integer dishId, List<IngredientDTO> ingredientDTOs) {
        if (ingredientDTOs == null) {
            throw new IllegalArgumentException("Request body with ingredient list is mandatory.");
        }

        // Vérifier que le plat existe
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish.id=" + dishId + " is not found"));

        // Ne garder que les IDs d'ingrédients qui existent réellement en BDD (ignorer les inconnus)
        List<Integer> validIngredientIds = ingredientDTOs.stream()
                .map(IngredientDTO::getId)
                .filter(id -> id != null && ingredientRepository.findById(id).isPresent())
                .toList();

        dishRepository.updateDishIngredients(dishId, validIngredientIds);

        // Recharger le plat avec ses nouveaux ingrédients
        return toDTO(dishRepository.findById(dishId).get());
    }

    // GET /dishes/{id}/ingredients?ingredientName={i}&ingredientPriceAround={p}
    public List<IngredientDTO> getDishIngredients(Integer dishId, String ingredientName, Double ingredientPriceAround) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish.id=" + dishId + " is not found"));

        List<Ingredients> ingredients = dish.getDishIngredients()
                .stream()
                .map(DishIngredient::getIngredient)
                .toList();

        // Filtre par nom : ilike (contient, insensible à la casse)
        if (ingredientName != null && !ingredientName.isBlank()) {
            String lower = ingredientName.toLowerCase();
            ingredients = ingredients.stream()
                    .filter(i -> i.getName() != null && i.getName().toLowerCase().contains(lower))
                    .toList();
        }

        // Filtre par prix : ± 50 autour de la valeur fournie
        if (ingredientPriceAround != null) {
            double min = ingredientPriceAround - 50;
            double max = ingredientPriceAround + 50;
            ingredients = ingredients.stream()
                    .filter(i -> i.getPrice() != null && i.getPrice() >= min && i.getPrice() <= max)
                    .toList();
        }

        return ingredients.stream()
                .map(i -> new IngredientDTO(i.getId(), i.getName(), i.getCategory(), i.getPrice()))
                .toList();
    }

    private DishDTO toDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setDishType(dish.getDishType());
        dto.setIngredients(
                dish.getDishIngredients().stream()
                        .map(di -> new IngredientDTO(
                                di.getIngredient().getId(),
                                di.getIngredient().getName(),
                                di.getIngredient().getCategory(),
                                di.getIngredient().getPrice()
                        ))
                        .toList()
        );
        return dto;
    }
}