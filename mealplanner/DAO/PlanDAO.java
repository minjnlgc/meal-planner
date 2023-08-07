package mealplanner.DAO;

import mealplanner.DbClient;
import mealplanner.Model.Plan;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlanDAO {
    private static final String CREATE_TABLE_plan = """
            CREATE TABLE IF NOT EXISTS plan (
               weekday VARCHAR(40) NOT NULL,
               meal VARCHAR(40) NOT NULL,
               category VARCHAR(40) NOT NULL,
               meal_id INT,
                  FOREIGN KEY(meal_id)
                  REFERENCES meals(meal_id)
                  ON DELETE CASCADE
            );            
            """;
    private static final String INSERT_plan = "INSERT INTO plan (weekday, meal, category, meal_id)" +
            " VALUES ('%s', '%s', '%s', %d);";
    private static final String SELECT_all_plan = "SELECT * FROM plan;";
    private static final String SELECT_all_plan_weekday = "SELECT * FROM plan WHERE weekday = '%s';";
    private static final String CHECK_if_empty = "SELECT COUNT(1) WHERE EXISTS (SELECT * FROM plan);";
    private final DbClient<Plan> dbClient;

    public PlanDAO() throws SQLException {
        this.dbClient = new DbClient<>();
        dbClient.run(CREATE_TABLE_plan);
    }

    public void add(String weekday, String meal, String category, int mealId) {
        try {
            dbClient.run(INSERT_plan.formatted(weekday, meal, category, mealId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Plan> getPlan() {
        List<Plan> plan = new ArrayList<>();
        try {
            Connection connection = dbClient.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_all_plan);

            while (resultSet.next()) {
                String weekday = resultSet.getString("weekday");
                String meal = resultSet.getString("meal");
                String category = resultSet.getString("category");
                int meal_id = resultSet.getInt("meal_id");

                plan.add(new Plan(weekday, category, meal, meal_id));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plan;
    }

    public List<Plan> getPlanByWeekday(String weekday) {
        List<Plan> plan = new ArrayList<>();
        try {
            Connection connection = dbClient.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_all_plan_weekday.formatted(weekday));

            while (resultSet.next()) {
                String meal = resultSet.getString(2);
                String category = resultSet.getString(3);
                plan.add(new Plan(weekday, category, meal));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plan;
    }

    public boolean isEmpty() {
        try {
            Connection connection = dbClient.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(CHECK_if_empty);

            while (resultSet.next()) {
                return resultSet.getInt("count") == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
