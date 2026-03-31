package com.myrestaurant.dishes.repository;

import com.myrestaurant.dishes.entity.CategoryEnum;
import com.myrestaurant.dishes.entity.Ingredients;
import com.myrestaurant.dishes.entity.MovementTypeEnum;
import com.myrestaurant.dishes.entity.StockMovement;
import com.myrestaurant.dishes.entity.StockValue;
import com.myrestaurant.dishes.entity.Unit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {

    private final JdbcTemplate jdbcTemplate;

    public IngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Ingredients> findAll() {
        String sql = "SELECT id, name, category, price FROM ingredients";
        List<Ingredients> list = jdbcTemplate.query(sql, (rs, rowNum) -> mapIngredient(rs));
        list.forEach(i -> i.setStockMovementList(findStockMovements(i.getId())));
        return list;
    }

    public Optional<Ingredients> findById(Integer id) {
        String sql = "SELECT id, name, category, price FROM ingredients WHERE id = ?";
        List<Ingredients> results = jdbcTemplate.query(sql, (rs, rowNum) -> mapIngredient(rs), id);
        if (results.isEmpty()) return Optional.empty();
        Ingredients ingredient = results.get(0);
        ingredient.setStockMovementList(findStockMovements(id));
        return Optional.of(ingredient);
    }

    private List<StockMovement> findStockMovements(Integer ingredientId) {
        String sql = "SELECT id, ingredient_id, type, quantity, unit, creation_datetime " +
                "FROM stock_movement WHERE ingredient_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            StockMovement sm = new StockMovement();
            sm.setId(rs.getInt("id"));
            sm.setIngredientId(rs.getInt("ingredient_id"));
            sm.setType(MovementTypeEnum.valueOf(rs.getString("type")));
            sm.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
            StockValue sv = new StockValue();
            sv.setQuantity(rs.getDouble("quantity"));
            sv.setUnit(Unit.valueOf(rs.getString("unit")));
            sm.setValue(sv);
            return sm;
        }, ingredientId);
    }

    private Ingredients mapIngredient(ResultSet rs) throws SQLException {
        Ingredients i = new Ingredients();
        i.setId(rs.getInt("id"));
        i.setName(rs.getString("name"));
        i.setCategory(CategoryEnum.valueOf(rs.getString("category")));
        i.setPrice(rs.getDouble("price"));
        return i;
    }
}