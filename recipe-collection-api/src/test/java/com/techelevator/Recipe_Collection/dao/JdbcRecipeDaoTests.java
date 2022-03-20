package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.dao.mockdatabase.BaseDaoTests;
import com.techelevator.Recipe_Collection.model.Ingredient;
import com.techelevator.Recipe_Collection.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcRecipeDaoTests extends BaseDaoTests {
    // Ingredients for BLUE_CHEESE_RECIPE
    private static final Ingredient BLUE_CHEESE_1 = new Ingredient(1L, "Blue Cheese", "Quart", 1.0, "Crumble the blue cheese by hand.");
    private static final Ingredient MAYO_2 = new Ingredient(2L, "Mayo", "Quart", 2.0, null);
    private static final Ingredient RICE_WINE_VIN_3 = new Ingredient(3L, "Rice Wine Vinegar", "Cup", 1.0, null);
    private static final Ingredient SUGAR_4 = new Ingredient(4L, "Sugar", "Cup", 0.25, null);
    private static final Ingredient SALT_5 = new Ingredient(5L, "Salt", null, null, null);
    private static final Ingredient PEPPER_6 = new Ingredient(6L, "Pepper", null, null, null);
    private static final Ingredient[] RECIPE_1_INGREDIENT_LIST = {BLUE_CHEESE_1, MAYO_2, RICE_WINE_VIN_3, SUGAR_4, SALT_5, PEPPER_6};

    // Ingredients for CHEESE_SAUCE_RECIPE
    private static final Ingredient PEPPER_JACK_7 = new Ingredient(7L, "PepperJack Cheese", "Logs", 2.0, "Shred Cheese using food processor attachment.");
    private static final Ingredient HALF_HALF_8 = new Ingredient(8L, "Half & Half", "Quart", 3.0, null);
    private static final Ingredient RED_BELL_PEPPER_9 = new Ingredient(9L, "Red Bell Pepper", "Each", 4.0, "Small diced.");
    private static final Ingredient YELLOW_ONION_10 = new Ingredient(10L, "Yellow Onion", "Each", 2.0, "Small diced.");
    private static final Ingredient JALAPENO_11 = new Ingredient(11L, "Jalapeno", "Each", 2.0, "Roasted, peeled and small diced.");
    private static final Ingredient[] RECIPE_2_INGREDIENT_LIST = {PEPPER_JACK_7, RED_BELL_PEPPER_9, YELLOW_ONION_10, JALAPENO_11, HALF_HALF_8};

    private static final Ingredient newIngredientVeg = new Ingredient(0L, "Veg", "Each", 2.0, "cut");
    private static final Ingredient newIngredientMeat = new Ingredient(0L, "Meat", "lbs", 3.0, "Roasted");
    private static final Ingredient[] newRecipeList = {newIngredientVeg ,newIngredientMeat};

    private static final Ingredient newIngredientVeg2 = new Ingredient(0L, "Veg2", "Each2", 2.0, "cut");
    private static final Ingredient newIngredientMeat2 = new Ingredient(0L, "Meat2", "lbs2", 3.0, "Roasted");
    private static final Ingredient[] newRecipeList2 = {newIngredientVeg2 ,newIngredientMeat2};

    // Recipes
    private static final Recipe BLUE_CHEESE_DRESSING_1 = new Recipe(1L, "Blue Cheese Dressing", "1. Whisk rice wine vinegar and sugar to dissolve.\n" +
            "2. Combine with blue cheese and mayo.\n3. Add salt and pepper to taste.", RECIPE_1_INGREDIENT_LIST);
    private static final Recipe CHEESE_SAUCE_2 = new Recipe(2L, "Cheese Sauce", "1. Sweat peppers and onions.\n" +
            "2. Add half & half and jalapenos.\n3. Bring to a very low simmer.\n" +
            "4. Add cheese in batches till fully incorporated.\n5. Cook very low until sauce reaches 145 degrees.", RECIPE_2_INGREDIENT_LIST);
    private static final Recipe newRecipe = new Recipe(3L, "New Recipe", "Test Prep", newRecipeList);
    private static Recipe newRecipe2 = new Recipe(0L, "New Recipe2", "Test Prep2", newRecipeList2);


    private JdbcRecipeDao jdbcRecipeDaoTest;

    @Before
    public void setup() {
        JdbcIngredientDao jdbcIngredientDaoTest = new JdbcIngredientDao(dataSource);
        jdbcRecipeDaoTest = new JdbcRecipeDao(dataSource, jdbcIngredientDaoTest);
    }

    @Test
    public void list_all_recipe_name_test() {
        Recipe i0 = new Recipe(1L, "Blue Cheese Dressing", BLUE_CHEESE_DRESSING_1.getPreparation(), null);
        Recipe i1 = new Recipe(2L, "Cheese Sauce", CHEESE_SAUCE_2.getPreparation(), null);

        List<Recipe> list = jdbcRecipeDaoTest.listAllName();

        assertRecipeMatch(i0, list.get(0));
        assertRecipeMatch(i1, list.get(1));
    }

    @Test
    public void get_recipe_test() {
        Recipe actual = jdbcRecipeDaoTest.get(BLUE_CHEESE_DRESSING_1.getRecipeId());
        assertRecipeMatch(BLUE_CHEESE_DRESSING_1, actual);

        actual = jdbcRecipeDaoTest.get(CHEESE_SAUCE_2.getRecipeId());
        assertRecipeMatch(CHEESE_SAUCE_2, actual);
    }
    @Test(expected = ResponseStatusException.class)
    public void get_recipe_exception_test1() {
       jdbcRecipeDaoTest.get(0);
    }
    @Test(expected = ResponseStatusException.class)
    public void get_recipe_exception_test2() {
        jdbcRecipeDaoTest.get(-1);
    }
    @Test(expected = ResponseStatusException.class)
    public void get_recipe_exception_test3() {
        jdbcRecipeDaoTest.get(5);
    }

    @Test
    public void create_recipe_test() {
        assertRecipeMatch(newRecipe, jdbcRecipeDaoTest.create(newRecipe));
    }

    @Test(expected = ResponseStatusException.class)
    public void check_recipe_exception_test3() {
        jdbcRecipeDaoTest.update(new Recipe(3L, "New Recipe", null, newRecipeList),1);
    }

    @Test
    public void update_recipe_test() {
        newRecipe2 = jdbcRecipeDaoTest.create(newRecipe2);
        newRecipe2.setIngredients(RECIPE_1_INGREDIENT_LIST);
        newRecipe2.setName("New Name");
        newRecipe2.setPreparation("New Prep");
        jdbcRecipeDaoTest.update(newRecipe2, newRecipe2.getRecipeId());
        assertRecipeMatch(newRecipe2, jdbcRecipeDaoTest.get(newRecipe2.getRecipeId()));
    }
    @Test(expected = ResponseStatusException.class)
    public void update_recipe_exception_test1() {
        jdbcRecipeDaoTest.update(newRecipe,0);
    }
    @Test(expected = ResponseStatusException.class)
    public void update_recipe_exception_test2() {
        jdbcRecipeDaoTest.update( newRecipe,-1);
    }
    @Test(expected = ResponseStatusException.class)
    public void update_recipe_exception_test3() {
        jdbcRecipeDaoTest.update(newRecipe, 5);
    }

    @Test(expected = ResponseStatusException.class)
    public void delete_recipe() {
        jdbcRecipeDaoTest.delete(1);
        jdbcRecipeDaoTest.delete(1);
    }


    private void assertRecipeMatch(Recipe expected, Recipe actual) {
        Assert.assertEquals(expected.getRecipeId(), actual.getRecipeId());
        Assert.assertEquals(expected.getName(), actual.getName());
        if (expected.getPreparation() != null && actual.getPreparation()!= null) {
            Assert.assertEquals(expected.getPreparation(), actual.getPreparation());
        }
        if (expected.getIngredients() != null && actual.getIngredients()!= null) {
            Assert.assertEquals(expected.getIngredients().length, actual.getIngredients().length);
        }
    }

}
