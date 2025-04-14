package edu.gmu.cs321;

import java.io.*;
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
            // salts
            "CREATE TABLE IF NOT EXISTS salts (userid INT PRIMARY KEY, salt VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO salts (userid, salt) VALUES (1, 'bffc343e7e8d8f9bdbdceca9f1d3d439');",

            "UPDATE users SET password = 'de28c09a560e498b6fb6ecbc45cd0cde' WHERE userid = 2;",
            "UPDATE users SET username = 'data' WHERE userid = 2;",

            // users
            "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec'), (2, 'data entry', 'data', 'de28c09a560e498b6fb6ecbc45cd0cde');",

            // Reviewer
            "CREATE TABLE IF NOT EXISTS reviewpapers (paper_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, ssn VARCHAR(255) NOT NULL, address VARCHAR(255) NOT NULL, cell VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL)",

            "CREATE TABLE IF NOT EXISTS reviewqueue (queue_id INT PRIMARY KEY AUTO_INCREMENT, userid INT NOT NULL, paper_id INT NOT NULL, status VARCHAR(255) NOT NULL, date DATE NOT NULL, FOREIGN KEY (userid) REFERENCES users(userid), FOREIGN KEY (paper_id) REFERENCES reviewpapers(paper_id))",

            //Data Entry
            "CREATE TABLE IF NOT EXISTS dataqueue (requestID INT PRIMARY KEY NOT NULL, requestorName VARCHAR(255) NOT NULL, requestorCitizenship VARCHAR(255) NOT NULL, deceasedName VARCHAR(255) NOT NULL, isLegible BOOL NOT NULL, requestStatus VARCHAR(255) NOT NULL, submissionDate DATE NOT NULL)",
        };
        for (String query : queries){
            stmt.addBatch(query);
        }

        stmt.executeBatch();
        stmt.close();

        //populate data queue from request.csv
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/edu/gmu/cs321/requests.csv"))) {
            String line;
            String query = "INSERT IGNORE INTO dataqueue (requestID, requestorName, requestorCitizenship, deceasedName, isLegible, requestStatus, submissionDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
 
                for (int i = 0; i < values.length; i++) {
                    if (i == 4) {
                        pstmt.setBoolean(i + 1, Boolean.parseBoolean(values[i]));
                    } else {
                        pstmt.setString(i + 1, values[i]);
                    }
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

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
        //System.out.println(pstmt.toString());

        ResultSet resultSet = pstmt.executeQuery();

        return resultSet;
    }

    public int executeUpdate(String query, Object... parameters) throws SQLException {
        return 0;
    }

    public void updateRequestStatus(String requestID, String status) throws SQLException {
        String query = "UPDATE reviewqueue SET status = ? WHERE queue_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, status);
        pstmt.setString(2, requestID);
        pstmt.executeUpdate();
    }

    public ResultSet getAllImmigrationRequests() throws SQLException {
        String query = "SELECT * FROM reviewpapers";
        return executeQuery(query);
    }
}
