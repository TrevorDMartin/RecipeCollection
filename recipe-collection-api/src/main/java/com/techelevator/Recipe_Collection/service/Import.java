package com.techelevator.Recipe_Collection.service;

import com.techelevator.Recipe_Collection.model.Ingredient;
import com.techelevator.Recipe_Collection.model.Recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Import {

    public static List<Recipe> importInventory() throws FileNotFoundException {
        List<Recipe> recipeList = new ArrayList<>();
        File folder = new File("ExampleFiles/");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for(File file : listOfFiles) {
            recipeList.add(buildRecipe(file));
        }

        return recipeList;
    }

    private static Recipe buildRecipe(File inventoryFile) throws FileNotFoundException {
        Recipe recipe = new Recipe();
        List<Ingredient> ingredientList = new ArrayList<>();

        try (Scanner scanner = new Scanner(inventoryFile)){
            if(scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] itemArray = nextLine.split("\\|");

                // index 0 - recipeName | index 1 - recipePrep
                recipe.setName(itemArray[0]);
                recipe.setPreparation(itemArray[1]);
            }

            while (scanner.hasNextLine()){
                Ingredient ingredient = new Ingredient();

                String nextLine = scanner.nextLine();
                String[] itemArray = nextLine.split("\\|");
                int size = itemArray.length;

                //index 0 - ingredientName | index 1 - amount | index 2 - unit | index 3 - ingredientPrep
                ingredient.setName(itemArray[0]);

                if (size>1) {
                    ingredient.setAmount(Double.parseDouble(itemArray[1]));
                }
                if (size>2) {
                    ingredient.setUnit(itemArray[2]);
                }
                if (size>3) {
                    ingredient.setPreparation(itemArray[3]);
                }

                ingredientList.add(ingredient);
            }
        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("File provided does not exist, inventory is empty.");
        }

        recipe.setIngredients(ingredientList.toArray(new Ingredient[0]));
        return recipe;
    }
}
