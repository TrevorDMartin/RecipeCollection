package com.techelevator.Recipe_Collection.controller;

import com.techelevator.Recipe_Collection.dao.IngredientDao;
import com.techelevator.Recipe_Collection.dao.RecipeDao;
import com.techelevator.Recipe_Collection.model.Ingredient;
import com.techelevator.Recipe_Collection.model.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("recipe/")
public class RecipeController {

    private final IngredientDao ingredientDao;
    private final RecipeDao recipeDao;

    public RecipeController(IngredientDao ingredientDao, RecipeDao recipeDao) {
        this.ingredientDao = ingredientDao;
        this.recipeDao = recipeDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Recipe> listRecipeNames() {
        return recipeDao.listAllName();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Recipe getRecipe(@PathVariable long id) {
        return recipeDao.get(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeDao.create(recipe);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public void updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable long id){
        recipeDao.update(recipe, id);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public void deleteRecipe(@PathVariable long id){
        recipeDao.delete(id);
    }
}
