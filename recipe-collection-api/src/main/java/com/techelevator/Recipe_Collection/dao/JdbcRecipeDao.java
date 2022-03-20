package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.model.Recipe;
import com.techelevator.Recipe_Collection.service.Import;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class JdbcRecipeDao implements RecipeDao {

    private final JdbcTemplate jdbcTemplate;
    private final IngredientDao ingredientDao;

    public JdbcRecipeDao(DataSource dataSource, IngredientDao ingredientDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.ingredientDao = ingredientDao;
    }

    @Override
    public List<Recipe> listAllName() {
        List<Recipe> recipe = new ArrayList<>();
        String sql = "SELECT recipe_id, name, preparation FROM recipe;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while (result.next()) {
            recipe.add(mapRowToRecipeName(result));
        }
        return recipe;
    }

    @Override
    public Recipe get(long id) {
        Recipe recipe;
        String sql = "SELECT recipe_id, name, preparation FROM recipe WHERE recipe_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (result.next()) {
            recipe = mapRowToRecipeName(result);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        recipe.setIngredients(ingredientDao.findAllByRecipe(id));
        return recipe;
    }

    @Override
    public Recipe create(Recipe recipe) {
        String sql = "BEGIN TRANSACTION;";
        jdbcTemplate.update(sql);

        sql = "INSERT INTO recipe (name, preparation) VALUES (?, ?) RETURNING recipe_id;";
        long id = jdbcTemplate.queryForObject(sql, long.class, recipe.getName(), recipe.getPreparation());

        try {
            ingredientDao.createRecipeIngredient(recipe.getIngredients(), id);
            sql = "COMMIT;";
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            sql = "ROLLBACK";
            jdbcTemplate.update(sql);
            throw e;
        }

        return get(id);
    }

    private void checkRecipe(Recipe recipe) {
        if(recipe.getIngredients()==null || recipe.getName()==null || recipe.getPreparation()==null) {
            throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT);
        }
    }

    @Override
    public void update(Recipe newRecipe, long updateId) {
        checkRecipe(newRecipe);
        get(updateId);
        String sql = "BEGIN TRANSACTION;";
        jdbcTemplate.update(sql);
        sql = "UPDATE recipe SET name = ?, preparation = ? WHERE recipe_id = ?; " +
                "DELETE FROM recipe_ingredient WHERE recipe_id = ?;";
        jdbcTemplate.update(sql, newRecipe.getName(), newRecipe.getPreparation(), updateId, updateId);

        try {
            ingredientDao.createRecipeIngredient(newRecipe.getIngredients(), updateId);
            sql = "COMMIT;";
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            sql = "ROLLBACK";
            jdbcTemplate.update(sql);
            throw e;
        }
    }

    @Override
    public void delete(long id) {
        get(id);
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ?; DELETE FROM recipe WHERE recipe_id = ?; " +
                "WITH cte AS (SELECT i.ingredient_id FROM ingredient AS i " +
                "LEFT JOIN recipe_ingredient AS ri ON " +
                "i.ingredient_id = ri.ingredient_id " +
                "WHERE recipe_id IS NULL) " +
                "DELETE FROM ingredient " +
                "WHERE ingredient_id IN (SELECT ingredient_id FROM cte);";

        jdbcTemplate.update(sql, id, id);
    }

    private Recipe mapRowToRecipeName(SqlRowSet result) {
        Recipe recipe = new Recipe();

        recipe.setRecipeId(result.getLong("recipe_id"));
        recipe.setName(result.getString("name"));
        recipe.setPreparation(result.getString("preparation"));

        return recipe;
    }

    @Override
    public void fillDatabase() {
        createDatabase();

        List<Recipe> recipeList;
        try {
            recipeList = Import.importInventory();
        } catch(FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        for (Recipe recipe : recipeList) {
            create(recipe);
        }
    }

    private void createDatabase() {
        File file = new File("src/test/resources/test-data.sql");
        String sql = "";

        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                sql += scanner.nextLine();
            }
        } catch (FileNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String sqlString = sql;
        jdbcTemplate.update(sqlString);

    }


}
