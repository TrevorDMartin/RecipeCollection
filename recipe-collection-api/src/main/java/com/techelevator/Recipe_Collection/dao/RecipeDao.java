package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.model.Recipe;

import java.util.List;

public interface RecipeDao {

    List<Recipe> listAllName();

    Recipe get(long id);

    Recipe create(Recipe recipe);

    void update(Recipe newRecipe, long updateId);

    void delete(long id);

    void fillDatabase();
}
