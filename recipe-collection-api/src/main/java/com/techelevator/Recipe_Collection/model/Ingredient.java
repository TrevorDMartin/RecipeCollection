package com.techelevator.Recipe_Collection.model;

import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;





public class Ingredient {

    // Instance Variables
    private long ingredientId;
    @NotBlank(message = "Ingredient name is required")
    private String name;
    private String unit;
    private Double amount;
    private String preparation;

    // Constructors
    public Ingredient() {}


    public Ingredient(long ingredientId, String name, String unit, Double amount, String preparation) {
        this.ingredientId = ingredientId;
        this.name = WordUtils.capitalizeFully(name);
        this.unit = unit;
        this.amount = amount;
        this.preparation = preparation;
    }

    // Getters
    public Long getIngredientId() {
        return ingredientId;
    }
    public String getName() {
        return name;
    }
    public String getUnit() {
        return unit;
    }
    public Double getAmount() {
        return amount;
    }
    public String getPreparation() {
        return preparation;
    }

    // Setters
    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    // Methods
    @Override
    public String toString() {
        return name;
    }
}
