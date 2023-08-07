package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient<T> {
    private static final String DB_URL = "jdbc:postgresql:meals_db";
    private static final String USER = "postgres";
    private static final String PASS = "1111";
    private Connection connection;

    public DbClient() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);
    }

    public void run(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.executeUpdate(sql);
    }

    public int runWithReturn(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        int num = -1;

        while (resultSet.next()) {
            num = resultSet.getInt(1);
        }

        return num;
    }

    public Connection getConnection() {
        return this.connection;
    }


    public void disconnect() throws SQLException {
        this.connection.close();
    }

}
