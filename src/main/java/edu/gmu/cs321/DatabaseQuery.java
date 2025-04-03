package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQuery {
    private static final String url = "jdbc:mysql://localhost:3306/your_database";
    private static final String user = "your_username";
    private static final String pass = "your_pass";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        return null;
    }

    public ResultSet escapedQuery(String query, Object... parameters) throws SQLException {
        return null;
    }

    public int executeUpdate(String query, Object... parameters) throws SQLException {
        return 0;
    }
}
