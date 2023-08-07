package mealplanner;

import mealplanner.DAO.PlanDAO;
import mealplanner.DAO.RecipeDAO;
import mealplanner.Model.Plan;
import mealplanner.Model.Recipe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

public class Controller {
    static final List<String> weekdays = new ArrayList<>(
            Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    public static RecipeDAO recipeDAO;
    public static PlanDAO planDAO;

    public static void initRecipe() {
        try {
            recipeDAO = new RecipeDAO();
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }


    public static boolean addRecipe(String category, String name, String ingredients) {
        List<String> ingredientsList = new ArrayList<>();
        for (String ingredient : ingredients.split(",")) {
            ingredient = ingredient.strip();
            ingredientsList.add(ingredient);
        }
        Recipe newRecipe = new Recipe(category, name, ingredientsList);

        try {
            recipeDAO.add(newRecipe);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }

        return false;
    }

    public static List<Recipe> getRecipes(String category) {
        List<Recipe> recipes = recipeDAO.findByCategory(category);
        return recipes;
    }

    public static List<String> printAndGetRecipeNames(String category) {
        List<Recipe> recipes = Controller.getRecipes(category);
        List<String> names = new ArrayList<>();
        for (Recipe r : recipes) {
            names.add(r.getName());
        }

        Collections.sort(names);

        for (String n : names) {
            System.out.println(n);
        }
        return names;
    }

    public static void initPlan() {
        try {
            planDAO = new PlanDAO();
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void addPlan(String weekday, String category, String meal) {
        int mealId = recipeDAO.getMealId(meal);
        planDAO.add(weekday, meal, category, mealId);
    }

    public static void printPlan() {
        for (String weekday : weekdays) {
            System.out.println(weekday);

            List<Plan> plan = planDAO.getPlanByWeekday(weekday);
            for (Plan p : plan) {
                System.out.println(p.getMealCategory().substring(0, 1).toUpperCase()
                        + p.getMealCategory().substring(1)
                        + ": " + p.getMealName());
            }
            System.out.println();
        }
    }

    public static void savePlan(String fileName) {
        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            List<Plan> plan = planDAO.getPlan();

            Map<String, Integer> map = new HashMap<>();

            for (Plan planEntry : plan) {
                List<String> ingredients = recipeDAO.getIngredient(planEntry.getMealId());
                countIngredients(ingredients, map);
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 1) {
                    printWriter.println(entry.getKey());
                } else {
                    printWriter.println("%s x%d".formatted(entry.getKey(), entry.getValue()));
                }
            }

            printWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPlanEmpty() {
        return planDAO.isEmpty();
    }

    private static void countIngredients(List<String> ingredients, Map<String, Integer> map) {
        for (String ingredient : ingredients) {
            if (map.containsKey(ingredient)) {
                map.put(ingredient, map.get(ingredient) + 1);
            } else {
                map.put(ingredient, 1);
            }
        }
    }

}
