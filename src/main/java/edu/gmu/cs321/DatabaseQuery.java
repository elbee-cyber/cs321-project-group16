package edu.gmu.cs321;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class handles the database connection and queries for the application.
 * It includes methods to connect to the database, execute queries, and update data.
 */
public class DatabaseQuery {
    // Database connection details
    private final String url = "jdbc:mysql://localhost:3306/cs321";
    private final String user = "root";
    private final String pass = "Quang$2001";
    private Connection connection;

    /**
     * Establishes a connection to the database and initializes the user and role-specific tables.
     * 
     * @return the established database connection
     * @throws SQLException if a database access error occurs
     */
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

            /**
             * Drop all tables to start fresh
             * FOREIGN_KEY_CHECKS to cascade constraints
             */
            "SET FOREIGN_KEY_CHECKS = 0",
            "DROP TABLE IF EXISTS rejectedpapers",
            "DROP TABLE IF EXISTS reviewqueue",
            "DROP TABLE IF EXISTS reviewpapers",
            "DROP TABLE IF EXISTS users",
            "DROP TABLE IF EXISTS dataqueue",
            "DROP TABLE IF EXISTS requestors",
            "DROP TABLE IF EXISTS deceased",
            "SET FOREIGN_KEY_CHECKS = 1",

            /**
             * General User Info Tables
             */
            // Users contains userid, username, password, role
            "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec'), (2, 'data entry', 'data', 'de28c09a560e498b6fb6ecbc45cd0cde');",

            // Reviewer
            "CREATE TABLE IF NOT EXISTS reviewpapers (paper_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, ssn VARCHAR(255) NOT NULL, address VARCHAR(255) NOT NULL, cell VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL)",

            "CREATE TABLE IF NOT EXISTS reviewqueue (queue_id INT PRIMARY KEY AUTO_INCREMENT, userid INT NOT NULL, paper_id INT NOT NULL, status VARCHAR(255) NOT NULL, date DATE NOT NULL, FOREIGN KEY (userid) REFERENCES users(userid), FOREIGN KEY (paper_id) REFERENCES reviewpapers(paper_id))",

            //Data Entry
            "CREATE TABLE IF NOT EXISTS dataqueue (requestID INT PRIMARY KEY NOT NULL, requestorName VARCHAR(255) NOT NULL, requestorCitizenship VARCHAR(255) NOT NULL, deceasedName VARCHAR(255) NOT NULL, isLegible BOOL NOT NULL, requestStatus VARCHAR(255) NOT NULL, submissionDate DATE NOT NULL)",

            //Requestors
            "CREATE TABLE IF NOT EXISTS requestors (requestorID INT PRIMARY KEY AUTO_INCREMENT, requestorName VARCHAR(255) NOT NULL, requestorAddress VARCHAR(255) NOT NULL, requestorCitizenship VARCHAR(255) NOT NULL, requestorSSN VARCHAR(255) NOT NULL, requestorCell VARCHAR(255) NOT NULL, requestorEmail VARCHAR(255) NOT NULL)",

            //Deceased
            "CREATE TABLE IF NOT EXISTS deceased (deceasedID INT PRIMARY KEY AUTO_INCREMENT, deceasedName VARCHAR(255) NOT NULL, deceasedDOB DATE NOT NULL, deceasedSSN VARCHAR(255) NOT NULL)",

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
        //populate requestors from living.csv
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/edu/gmu/cs321/living.csv"))) {
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
        //populate deceased queue from deceased.csv
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/edu/gmu/cs321/deceased.csv"))) {
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

    /**
     * Executes a SQL query and returns the result set.
     * 
     * @param query the SQL query to execute
     * @return the result set of the query
     * @throws SQLException if a database access error occurs
     */
    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Executes a prepared SQL query with parameters and returns the result set.
     * 
     * @param query the SQL query to execute
     * @param col_values the values to set in the prepared statement
     * @return the result set of the query
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Executes a SQL update query.
     * 
     * @param query the SQL update query to execute
     * @throws SQLException if a database access error occurs
     */
    public void executeUpdate(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    /**
     * Executes a prepared SQL update query with parameters.
     * 
     * @param query the SQL update query to execute
     * @param col_values the values to set in the prepared statement
     * @throws SQLException if a database access error occurs
     */
    public void executePUpdate(String query, Object... col_values) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(query);
        int index = 1;
        for (Object value : col_values) {
            pstmt.setObject(index, value);
            index++;
        }
        System.out.println(pstmt.toString());

        pstmt.executeUpdate();
        pstmt.close();
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
