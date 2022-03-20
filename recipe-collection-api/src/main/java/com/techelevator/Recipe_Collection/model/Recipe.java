package com.techelevator.Recipe_Collection.model;

import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Recipe {

    // Instance Variables
    private long recipeId;
    @NotBlank(message = "Recipe name is required")
    private String name;
    @NotBlank(message = "The field 'preparation' is required.")
    private String preparation;
    @Size(min=1, message = "Recipe must have at least 1 ingredient.")
    Ingredient[] ingredients;

    // Constructors
    public Recipe () {}

    public Recipe(long recipeId, String name, String preparation, Ingredient[] ingredients) {
        this.recipeId = recipeId;
        this.name = WordUtils.capitalizeFully(name);
        this.preparation = preparation;
        this.ingredients = ingredients;
    }

    // Getters
    public Long getRecipeId() {
        return recipeId;
    }
    public String getName() {
        return name;
    }
    public String getPreparation() {
        return preparation;
    }
    public Ingredient[] getIngredients() {
        return ingredients;
    }

    // Setters
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }
    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }
    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    // Methods
    @Override
    public String toString() {
        return name;
    }
}
