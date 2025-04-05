package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseQuery {
    private final String url = "jdbc:mysql://localhost:3306/cs321";
    private final String user = "root";
    private final String pass = "Quang$2001";
    private Connection connection;

    public Connection connect() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, pass);
        return this.connection;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    public ResultSet executePQuery(String query, Object... col_values) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(query);
        int index = 1;
        for (Object value : col_values) {
            pstmt.setObject(index, value);
            index++;
        }
        System.out.println(pstmt.toString());

        ResultSet resultSet = pstmt.executeQuery();

        return resultSet;
    }

    public int executeUpdate(String query, Object... parameters) throws SQLException {
        return 0;
    }
}
