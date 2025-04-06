package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQuery {
    private final String url = "jdbc:mysql://localhost:3306/cs321";
    private final String user = "root";
    private final String pass = "Quang$2001";
    private Connection connection;

    public Connection connect() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, pass);

        // create database
        Statement stmt = connection.createStatement();
        String queries[] = {
            // users
            "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec');",

            // reviewer
            "CREATE TABLE IF NOT EXISTS reviewpapers (paper_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, ssn VARCHAR(255) NOT NULL, address VARCHAR(255) NOT NULL, cell VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL)",
            "CREATE TABLE IF NOT EXISTS reviewqueue (queue_id INT PRIMARY KEY AUTO_INCREMENT, userid INT NOT NULL, paper_id INT NOT NULL, status VARCHAR(255) NOT NULL, date DATE NOT NULL, FOREIGN KEY (userid) REFERENCES users(userid), FOREIGN KEY (paper_id) REFERENCES reviewpapers(paper_id))",
        };
        for (String query : queries){
            stmt.addBatch(query);
        }

        stmt.executeBatch();
        stmt.close();

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
