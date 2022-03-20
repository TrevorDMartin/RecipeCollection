package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.model.Ingredient;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface IngredientDao {


    List<Ingredient> findAll();

    Ingredient get(long id);

    Ingredient[] findAllByRecipe(long recipeId);

    void createRecipeIngredient(Ingredient[] ingredients, long recipeId);

    void update(Ingredient ingredient, long id);
}
