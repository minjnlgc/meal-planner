package mealplanner.DAO;

import mealplanner.DbClient;
import mealplanner.Model.Recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private static final String CREATE_TABLE_meals = """
            CREATE TABLE IF NOT EXISTS meals (
                category VARCHAR(40) NOT NULL,
                meal VARCHAR(40) NOT NULL,
                meal_id INT PRIMARY KEY
            );
            """;
    private static final String CREATE_TABLE_ingredients = """
            CREATE TABLE IF NOT EXISTS ingredients (
                ingredient VARCHAR(255) NOT NULL,
                ingredient_id INT PRIMARY KEY,
                meal_id INT NOT NULL
            );
            """;

    private static final String CREATE_SEQUENCE_meals_id = "CREATE SEQUENCE IF NOT EXISTS " +
            "meals_id_sequence START 1 INCREMENT 1 CACHE 1";
    private static final String CREATE_SEQUENCE_ingredients_id = "CREATE SEQUENCE IF NOT EXISTS " +
            "ingredients_id_sequence START 1 INCREMENT 1 CACHE 1";

    private static final String INSERT_meals = "INSERT INTO meals(category, meal, meal_id)" +
            "VALUES ('%s', '%s', NEXTVAL('meals_id_sequence')) RETURNING meal_id";
    private static final String INSERT_ingredients = "INSERT INTO ingredients(ingredient, ingredient_id, meal_id)" +
            "VALUES ('%s', NEXTVAL('ingredients_id_sequence') ,%d)";

    private static final String SELECT_ALL_meals = "SELECT * FROM meals";
    private static final String SELECT_meals_with_category = "SELECT * FROM meals WHERE category = '%s'";
    private static final String SELECT_ingredient = "SELECT ingredient FROM ingredients WHERE meal_id = %d";
    private static final String SELECT_meals_id = "SELECT meal_id FROM meals WHERE meal='%s'";

    private final DbClient<Recipe> dbClient;

    public RecipeDAO() throws SQLException {
        this.dbClient = new DbClient<>();

        this.dbClient.run(CREATE_SEQUENCE_meals_id);
        this.dbClient.run(CREATE_TABLE_meals);
        this.dbClient.run(CREATE_SEQUENCE_ingredients_id);
        this.dbClient.run(CREATE_TABLE_ingredients);
    }

    public List<Recipe> findByCategory(String category) {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = dbClient.getConnection();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_meals_with_category.formatted(category));

            while (resultSet.next()) {
                String meal = resultSet.getString(2);
                int id = resultSet.getInt(3);

                recipes.add(new Recipe(category, meal, id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Recipe recipe : recipes) {
            List<String> ingredients = new ArrayList<>();

            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT_ingredient.formatted(recipe.getId()));

                while (resultSet.next()) {
                    String ingredient = resultSet.getString(1);
                    ingredients.add(ingredient);
                }
                recipe.setIngredients(ingredients);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }

    public int getMealId(String name) {
        Connection connection = dbClient.getConnection();
        int mealId = -1;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_meals_id.formatted(name));

            while (resultSet.next()) {
                mealId = resultSet.getInt("meal_id");
            }

        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }

        return mealId;
    }


    public void add(Recipe recipe) throws SQLException {

        int mealId = dbClient.runWithReturn(String.format(
                INSERT_meals, recipe.getCategory(), recipe.getName()));

        for (String ingredient : recipe.getIngredients()) {
            dbClient.run(String.format(INSERT_ingredients, ingredient, mealId));
        }

    }

    public List<String> getIngredient(int mealId) {
        List<String> ingredients = new ArrayList<>();
        Connection connection = dbClient.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ingredient.formatted(mealId));
            while (resultSet.next()) {
                ingredients.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
        return ingredients;
    }

}
