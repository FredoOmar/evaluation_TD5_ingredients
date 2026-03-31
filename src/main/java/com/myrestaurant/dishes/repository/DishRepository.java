package com.myrestaurant.dishes.repository;

import com.myrestaurant.dishes.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DishRepository {

    private final JdbcTemplate jdbcTemplate;

    public DishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dish> findAll() {
        String sql = "SELECT id, name, price, dish_type FROM dish";
        List<Dish> dishes = jdbcTemplate.query(sql, (rs, rowNum) -> mapDish(rs));
        dishes.forEach(d -> d.setDishIngredients(findDishIngredients(d)));
        return dishes;
    }

    public Optional<Dish> findById(Integer id) {
        String sql = "SELECT id, name, price, dish_type FROM dish WHERE id = ?";
        List<Dish> results = jdbcTemplate.query(sql, (rs, rowNum) -> mapDish(rs), id);
        if (results.isEmpty()) return Optional.empty();
        Dish dish = results.get(0);
        dish.setDishIngredients(findDishIngredients(dish));
        return Optional.of(dish);
    }

    /**
     * Remplace tous les liens dish_ingredient pour un plat donné.
     * Utilisé par PUT /dishes/{id}/ingredients
     */
    public void updateDishIngredients(Integer dishId, List<Integer> ingredientIds) {
        // Supprimer les anciens liens
        jdbcTemplate.update("DELETE FROM dish_ingredient WHERE dish_id = ?", dishId);
        // Insérer les nouveaux liens (uniquement les IDs existants en BDD)
        for (Integer ingredientId : ingredientIds) {
            jdbcTemplate.update(
                    "INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)",
                    dishId, ingredientId, 1.0, "PCS"
            );
        }
    }

    private List<DishIngredient> findDishIngredients(Dish dish) {
        String sql = "SELECT di.id, di.quantity, di.unit, " +
                "i.id AS ing_id, i.name AS ing_name, i.category, i.price " +
                "FROM dish_ingredient di " +
                "JOIN ingredients i ON i.id = di.ingredient_id " +
                "WHERE di.dish_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredients ingredient = new Ingredients();
            ingredient.setId(rs.getInt("ing_id"));
            ingredient.setName(rs.getString("ing_name"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ingredient.setPrice(rs.getDouble("price"));

            DishIngredient di = new DishIngredient();
            di.setId(rs.getInt("id"));
            di.setDish(dish);
            di.setIngredient(ingredient);
            di.setQuantity(rs.getDouble("quantity"));
            di.setUnit(Unit.valueOf(rs.getString("unit")));
            return di;
        }, dish.getId());
    }

    private Dish mapDish(ResultSet rs) throws SQLException {
        Dish d = new Dish();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setPrice(rs.getDouble("price"));
        d.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
        return d;
    }
}