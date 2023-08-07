package mealplanner.Model;

import java.util.List;
import java.util.stream.Collectors;

public class Recipe {
    private String category;
    private String name;
    private List<String> ingredients;
    private int id;

    public Recipe(String category, String name, List<String> ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Recipe(String category, String name, int id) {
        this.category = category;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    public int getId() {
        return this.id;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void printName() {
        System.out.println("Name: " + this.getName());
    }

    public void printIngredients() {
        String ingredients = "Ingredients:\n" + this.getIngredients().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        System.out.println(ingredients);
    }

    @Override
    public String toString() {
        String line1 = "Category: " + this.getCategory() + "\n";
        String line2 = "Name: " + this.getName() + "\n";

        String line3 = "Ingredients:\n" + this.getIngredients().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return line1+line2+line3;
    }
}
