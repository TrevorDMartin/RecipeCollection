package com.techelevator.Recipe_Collection.dao;

import com.techelevator.Recipe_Collection.dao.mockdatabase.BaseDaoTests;
import com.techelevator.Recipe_Collection.model.Ingredient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


public class JdbcIngredientDaoTests extends BaseDaoTests {
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

    private JdbcIngredientDao jdbcIngredientDaoTest;

    @Before
    public void setup() {
        jdbcIngredientDaoTest = new JdbcIngredientDao(dataSource);
    }

    @Test
    public void find_all_test() {
        List<Ingredient> list = jdbcIngredientDaoTest.findAll();

        Assert.assertEquals(11, list.size());
        assertIngredientMatch(BLUE_CHEESE_1, list.get(0));
        assertIngredientMatch(SUGAR_4, list.get(3));
        assertIngredientMatch(RED_BELL_PEPPER_9, list.get(8));
    }

    @Test
    public void find_all_by_recipe_test() {
        Ingredient[] recipe1 = jdbcIngredientDaoTest.findAllByRecipe(1);
        Ingredient[] recipe2 = jdbcIngredientDaoTest.findAllByRecipe(2);
        assertIngredientMatch(MAYO_2, recipe1[1]);
        assertIngredientMatch(SALT_5, recipe1[4]);
        assertIngredientMatch(PEPPER_JACK_7, recipe2[0]);
        assertIngredientMatch(YELLOW_ONION_10, recipe2[3]);
    }
    @Test(expected = ResponseStatusException.class)
    public void find_all_by_recipe_exception_test() {
        jdbcIngredientDaoTest.findAllByRecipe(3);
    }
    @Test(expected = ResponseStatusException.class)
    public void find_all_by_recipe_exception_negative_test() {
        jdbcIngredientDaoTest.findAllByRecipe(-1);
    }

    @Test
    public void create_recipe_ingredient_test() {
        Ingredient[] list = {BLUE_CHEESE_1, MAYO_2};
        jdbcIngredientDaoTest.createRecipeIngredient(list, 2);

        list = jdbcIngredientDaoTest.findAllByRecipe(2);

        assertIngredientMatch(BLUE_CHEESE_1, list[0]);
        assertIngredientMatch(MAYO_2, list[1]);
    }
    @Test(expected = ResponseStatusException.class)
    public void create_recipe_ingredient_exception_test() {
        Ingredient[] list = {BLUE_CHEESE_1, MAYO_2};
        jdbcIngredientDaoTest.createRecipeIngredient(list, 3);
    }
    @Test
    public void create_recipe_ingredient_empty_null_test() {
        Ingredient[] list = {};
        jdbcIngredientDaoTest.createRecipeIngredient(list, 2);

        list = jdbcIngredientDaoTest.findAllByRecipe(2);

        assertIngredientMatch(PEPPER_JACK_7, list[0]);
        assertIngredientMatch(HALF_HALF_8, list[1]);

        jdbcIngredientDaoTest.createRecipeIngredient(null, 2);

        list = jdbcIngredientDaoTest.findAllByRecipe(2);

        assertIngredientMatch(PEPPER_JACK_7, list[0]);
        assertIngredientMatch(HALF_HALF_8, list[1]);
    }

    @Test
    public void set_ingredient_id_test() {
        Ingredient test = new Ingredient(0L, "PepperJack Cheese", null,null,null);
        Ingredient[] t = {test};

        jdbcIngredientDaoTest.createRecipeIngredient(t,1);

        test.setIngredientId(PEPPER_JACK_7.getIngredientId());
        t = jdbcIngredientDaoTest.findAllByRecipe(1);
        assertIngredientMatch(test, t[6]);

        test = new Ingredient(0L, "Test", null,null,null);
        Ingredient[] tt = {test};

        jdbcIngredientDaoTest.createRecipeIngredient(tt,2);

        test.setIngredientId(12L);
        tt = jdbcIngredientDaoTest.findAllByRecipe(2);
        assertIngredientMatch(test, tt[5]);
    }

    @Test
    public void update_ingredient_test() {
        Ingredient pepperJack = new Ingredient(PEPPER_JACK_7.getIngredientId(), "testPepperJack", null, null, null);
        jdbcIngredientDaoTest.update(pepperJack, PEPPER_JACK_7.getIngredientId());

        assertIngredientMatch(pepperJack, jdbcIngredientDaoTest.get(PEPPER_JACK_7.getIngredientId()));
    }
    @Test(expected = ResponseStatusException.class)
    public void get_ingredient_exception_test1() {
        jdbcIngredientDaoTest.get(-1);

    }
    @Test(expected = ResponseStatusException.class)
    public void get_ingredient_exception_test2() {
        jdbcIngredientDaoTest.get(0);

    }
    @Test(expected = ResponseStatusException.class)
    public void get_ingredient_exception_test3() {
        jdbcIngredientDaoTest.get(15);

    }

    private void assertIngredientMatch(Ingredient expected, Ingredient actual) {
        Assert.assertEquals("ID did not match",expected.getIngredientId(), actual.getIngredientId());
        Assert.assertEquals("Name did not match",expected.getName(), actual.getName());
        if (expected.getUnit() != null && actual.getUnit()!= null) {
            Assert.assertEquals("Unit did not match",expected.getUnit(), actual.getUnit());
        }
        if (expected.getAmount() != null && actual.getAmount()!= null) {
            Assert.assertEquals("Amount did not match",expected.getAmount(), actual.getAmount());
        }
        if (expected.getPreparation() != null && actual.getPreparation()!= null) {
            Assert.assertEquals("Preparation did not match",expected.getPreparation(), actual.getPreparation());
        }
    }

}
