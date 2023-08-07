package mealplanner;

import mealplanner.Model.Recipe;

import java.util.*;

public class View {
    static final Set<String> validMeals = new HashSet<>(Arrays.asList("breakfast", "lunch", "dinner"));
    static final List<String> weekdays = new ArrayList<>(
            Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

    public static void start() {

        Controller.initRecipe();
        Controller.initPlan();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String action = scanner.nextLine();

            switch (action) {
                case "add":
                    View.add(scanner);
                    break;
                case "show":
                    View.show(scanner);
                    break;
                case "plan":
                    View.plan(scanner);
                    break;
                case "save":
                    View.save(scanner);
                    break;
                case "exit":
                    System.out.println("Bye!");
                    exit = true;
                    break;
            }
        }

    }

    public static void add(Scanner scanner) {

        Set<String> validMeals = new HashSet<>(Arrays.asList("breakfast", "lunch", "dinner"));

        String category = "";

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        category = scanner.nextLine();

        while (!validMeals.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }


        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();

        while (!name.matches("^[a-zA-z]+(\\s[a-zA-Z]+)*$")) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }

        System.out.println("Input the ingredients:");
        String ingredients = scanner.nextLine();

        while (!checkIngredients(ingredients)) {
            System.out.println("Wrong format. Use letters only!");
            ingredients = scanner.nextLine();
        }

        boolean isAddedNewRecipe = Controller.addRecipe(category, name, ingredients);
        if (isAddedNewRecipe) {
            System.out.println("The meal has been added!");
        } else {
            System.out.println("Error!");
        }

    }

    public static void show(Scanner scanner) {

        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();

        while (!View.validMeals.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }

        if (Controller.getRecipes(category).isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + category);
            for (Recipe r : Controller.getRecipes(category)) {
                r.printName();
                r.printIngredients();
            }
        }
    }

    public static void plan(Scanner scanner) {
        Controller.initPlan();
        for (String d : weekdays) {
            System.out.println(d);

            String breakfast_choice = getChoice(scanner, "breakfast", d);
            Controller.addPlan(d, "breakfast", breakfast_choice);

            String lunch_choice = getChoice(scanner, "lunch", d);
            Controller.addPlan(d, "lunch", lunch_choice);

            String dinner_choice = getChoice(scanner, "dinner", d);
            Controller.addPlan(d, "dinner", dinner_choice);

            System.out.printf("Yeah! We planned the meals for %s.\n", d);
            System.out.println();
        }

        Controller.printPlan();
    }

    private static void save(Scanner scanner) {
        if (Controller.checkPlanEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            System.out.println("Input a filename:");
            String fileName = scanner.nextLine();
            Controller.savePlan(fileName);
            System.out.println("Saved!");
        }
    }

    private static boolean checkIngredients(String ingredients) {
        List<String> ingredientsList = Arrays.asList(ingredients.split(","));
        for (String ingredient : ingredientsList) {
            ingredient = ingredient.trim();
            if (!ingredient.matches("[a-zA-Z ]+")) {
                return false;
            }
        }
        return true;
    }

    private static String getChoice(Scanner scanner, String category, String weekday) {
        List<String> mealOptions = Controller.printAndGetRecipeNames(category);
        System.out.printf("Choose the %s for %s from the list above:\n", category, weekday);
        String choice = scanner.nextLine();

        while (!mealOptions.contains(choice)) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            choice = scanner.nextLine();
        }
        return choice;
    }


}
