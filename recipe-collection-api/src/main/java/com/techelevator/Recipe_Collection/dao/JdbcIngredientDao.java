package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.model.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.*;

@Component
public class JdbcIngredientDao implements IngredientDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcIngredientDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredient = new ArrayList<>();
        String sql = "SELECT ingredient_id, name FROM ingredient;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while (result.next()) {
            ingredient.add(mapRowToIngredient(result));
        }
        return ingredient;
    }

    @Override
    public Ingredient get(long id) {
        String sql = "SELECT name, ingredient_id FROM ingredient WHERE ingredient_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (result.next()) {
            return mapRowToIngredient(result);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Ingredient[] findAllByRecipe(long recipeId) {
        checkRecipe(recipeId);
        List<Ingredient> ingredient = new ArrayList<>();
        String sql = "SELECT ingredient.ingredient_id, name, unit, amount, preparation FROM ingredient " +
                "JOIN recipe_ingredient ON ingredient.ingredient_id = recipe_ingredient.ingredient_id " +
                "WHERE recipe_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, recipeId);
        while (result.next()) {
            ingredient.add(mapRowToIngredientRecipe(result));
        }
        return ingredient.toArray(new Ingredient[0]);
    }

    @Override
    public void createRecipeIngredient(Ingredient[] ingredients, long recipeId) {
        checkRecipe(recipeId);
        if(ingredients!=null && ingredients.length>0) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getIngredientId() == 0) {
                    setIngredientId(ingredient);
                }
                String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit, amount, preparation) VALUES (?, ?, ?, ?, ?);";
                jdbcTemplate.update(sql, recipeId, ingredient.getIngredientId(), ingredient.getUnit(), ingredient.getAmount(), ingredient.getPreparation());
            }
        }
    }

    private void setIngredientId(Ingredient ingredient) {
        String sql = "SELECT ingredient_id FROM ingredient WHERE name = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, ingredient.getName());
        if (result.next()) {
            ingredient.setIngredientId(result.getLong("ingredient_id"));
        } else {
            sql = "INSERT INTO ingredient (name) VALUES (?) RETURNING ingredient_id;";
            ingredient.setIngredientId(jdbcTemplate.queryForObject(sql, long.class, ingredient.getName()));
        }
    }

    @Override
    public void update(Ingredient ingredient, long id) {
        get(id);
        String sql = "UPDATE ingredient SET name = ? WHERE ingredient_id = ?;";
        jdbcTemplate.update(sql, ingredient.getName(), id);
    }

    private Ingredient mapRowToIngredient(SqlRowSet result) {
        Ingredient ingredient = new Ingredient();

        ingredient.setIngredientId(result.getLong("ingredient_id"));
        ingredient.setName(result.getString("name"));

        return ingredient;
    }

    private Ingredient mapRowToIngredientRecipe(SqlRowSet result) {
        Ingredient ingredient = new Ingredient();

        ingredient.setIngredientId(result.getLong("ingredient_id"));
        ingredient.setName(result.getString("name"));
        ingredient.setUnit(result.getString("unit"));
        ingredient.setAmount(result.getDouble("amount"));
        ingredient.setPreparation(result.getString("preparation"));

        return ingredient;
    }

    private void checkRecipe(long id) {
        String sql = "SELECT name FROM recipe WHERE recipe_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (!result.next()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
