package com.techelevator.Recipe_Collection.controller;

import com.techelevator.Recipe_Collection.dao.RecipeDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportController {

    private RecipeDao recipeDao;

    public ImportController(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(path = "/admin/create/database", method = RequestMethod.GET)
    public void buildDatabase() {
        recipeDao.fillDatabase();
    }
}
