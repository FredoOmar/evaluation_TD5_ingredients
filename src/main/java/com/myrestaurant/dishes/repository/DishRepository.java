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
        String sql = "SELECT id, name, price, dishtype AS dish_type FROM dish";
        List<Dish> dishes = jdbcTemplate.query(sql, (rs, rowNum) -> mapDish(rs));
        dishes.forEach(d -> d.setDishIngredients(findDishIngredients(d)));
        return dishes;
    }

    public Optional<Dish> findById(Integer id) {
        String sql = "SELECT id, name, price, dishtype AS dish_type FROM dish WHERE id = ?";
        List<Dish> results = jdbcTemplate.query(sql, (rs, rowNum) -> mapDish(rs), id);
        if (results.isEmpty()) return Optional.empty();
        Dish dish = results.get(0);
        dish.setDishIngredients(findDishIngredients(dish));
        return Optional.of(dish);
    }

    public void updateDishIngredients(Integer dishId, List<Integer> ingredientIds) {
        jdbcTemplate.update("DELETE FROM dish_ingredients WHERE id_dish = ?", dishId);
        for (Integer ingredientId : ingredientIds) {
            jdbcTemplate.update(
                    "INSERT INTO dish_ingredients (id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, ?, ?)",
                    dishId, ingredientId, 1.0, "KG"
            );
        }
    }

    private List<DishIngredient> findDishIngredients(Dish dish) {
        String sql = "SELECT di.id, di.quantity_required, di.unit, " +
                "i.id AS ing_id, i.name AS ing_name, i.category, i.price " +
                "FROM dish_ingredients di " +
                "JOIN ingredient i ON i.id = di.id_ingredient " +
                "WHERE di.id_dish = ?";
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
            di.setQuantity(rs.getDouble("quantity_required"));
            di.setUnit(Unit.fromString(rs.getString("unit")));
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