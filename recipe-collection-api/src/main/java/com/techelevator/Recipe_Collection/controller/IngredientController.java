package com.techelevator.Recipe_Collection.controller;

import com.techelevator.Recipe_Collection.dao.IngredientDao;
import com.techelevator.Recipe_Collection.model.Ingredient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class IngredientController {

    private IngredientDao ingredientDao;

    public IngredientController (IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    @RequestMapping(path = "/ingredients", method = RequestMethod.GET)
    public List<Ingredient> listIngredients() {
        return ingredientDao.findAll();
    }

    @RequestMapping(path = "/admin/ingredient/{id}", method = RequestMethod.PUT)
    public void updateIngredient(@Valid @RequestBody Ingredient ingredient, @PathVariable long id) {
        ingredientDao.update(ingredient, id);
    }

}
