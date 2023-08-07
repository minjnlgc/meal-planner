package mealplanner.Model;

public class Plan {
    private String weekday;
    private String mealCategory;
    private String mealName;
    private int mealId;

    public Plan(String weekday, String mealCategory, String mealName, int mealId) {
        this.weekday = weekday;
        this.mealCategory = mealCategory;
        this.mealName = mealName;
        this.mealId = mealId;
    }

    public Plan(String weekday, String mealCategory, String mealName) {
        this.weekday = weekday;
        this.mealCategory = mealCategory;
        this.mealName = mealName;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public int getMealId() {
        return mealId;
    }
}
