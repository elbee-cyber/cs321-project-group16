package edu.gmu.cs321;

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
            /**
             * Drop all tables to start fresh
             * FOREIGN_KEY_CHECKS to cascade constraints
             */
            "SET FOREIGN_KEY_CHECKS = 0",
            "DROP TABLE IF EXISTS rejectedpapers",
            "DROP TABLE IF EXISTS reviewqueue",
            "DROP TABLE IF EXISTS reviewpapers",
            "DROP TABLE IF EXISTS users",
            "SET FOREIGN_KEY_CHECKS = 1",

            /**
             * General User Info Tables
             */
            // Users contains userid, username, password, role
            "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec')",

            /**
             * Reviewer Tables and Test Data
             */
            // Reviewpapers contains paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason
            "CREATE TABLE IF NOT EXISTS reviewpapers (paper_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, ssn VARCHAR(255) UNIQUE NOT NULL, address VARCHAR(255) NOT NULL, cell VARCHAR(255) UNIQUE NOT NULL, email VARCHAR(255) UNIQUE NOT NULL, deceasedname VARCHAR(255), deceasedDOB DATE, deceasedSSN VARCHAR(255), deceasedrela VARCHAR(255), reason VARCHAR(255))",

            // Reviewqueue contains queue_id, paper_id REF Reviewpapers, status, date
            "CREATE TABLE IF NOT EXISTS reviewqueue (queue_id INT PRIMARY KEY AUTO_INCREMENT, paper_id INT NOT NULL, status VARCHAR(255) NOT NULL, date DATE NOT NULL, FOREIGN KEY (paper_id) REFERENCES reviewpapers(paper_id))",

            // Rejectedpapers contains paper_id REF Reviewpapers, comment
            "CREATE TABLE IF NOT EXISTS rejectedpapers (queue_id INT NOT NULL, comment VARCHAR(255) NOT NULL, FOREIGN KEY (queue_id) REFERENCES reviewqueue(queue_id))",

            // Example data for reviewpapers
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (3, 'Alice Johnson', '111-22-3333', '789 Oak St', '555-7890', 'alicejohnson@example.com', 'John Doe', '1950-01-01', '123-45-6789', 'Father', 'Requesting records for immigration purposes')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (4, 'Bob Brown', '222-33-4444', '321 Pine St', '555-8901', 'bobbrown@example.com', 'Jane Smith', '1960-02-02', '234-56-7890', 'Mother', 'Requesting records for legal documentation')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (5, 'Charlie Davis', '333-44-5555', '654 Maple St', '555-9012', 'charliedavis@example.com', 'Robert Brown', '1945-03-03', '345-67-8901', 'Uncle', 'Requesting records for family history research')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (6, 'Diana Evans', '444-55-6666', '987 Birch St', '555-0123', 'dianaevans@example.com', 'Emily White', '1970-04-04', '456-78-9012', 'Aunt', 'Requesting records for estate settlement')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (7, 'Ethan Foster', '555-66-7777', '123 Cedar St', '555-1235', 'ethanfoster@example.com', 'Michael Green', '1980-05-05', '567-89-0123', 'Brother', 'Requesting records for citizenship application')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (8, 'Fiona Green', '666-77-8888', '456 Walnut St', '555-5679', 'fionagreen@example.com', 'Sarah Black', '1990-06-06', '678-90-1234', 'Sister', 'Requesting records for visa application')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (9, 'George Harris', '777-88-9999', '789 Spruce St', '555-7891', 'georgeharris@example.com', 'William Gray', '1935-07-07', '789-01-2345', 'Grandfather', 'Requesting records for genealogy purposes')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (10, 'Hannah Irving', '888-99-0000', '321 Chestnut St', '555-8902', 'hannahirving@example.com', 'Laura Blue', '1940-08-08', '890-12-3456', 'Grandmother', 'Requesting records for inheritance claim')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (11, 'Ian Jackson', '999-00-1111', '654 Willow St', '555-9013', 'ianjackson@example.com', 'Thomas Red', '1955-09-09', '901-23-4567', 'Cousin', 'Requesting records for academic purposes')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (12, 'Julia King', '000-11-2222', '987 Redwood St', '555-0124', 'juliaking@example.com', 'Anna Yellow', '1965-10-10', '012-34-5678', 'Niece', 'Requesting records for medical history')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (13, 'Kevin Lewis', '111-22-3334', '123 Aspen St', '555-1236', 'kevinlewis@example.com', 'James Purple', '1975-11-11', '123-45-6789', 'Nephew', 'Requesting records for legal proceedings')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (14, 'Laura Martinez', '222-33-4445', '456 Poplar St', '555-5670', 'lauramartinez@example.com', 'Sophia Orange', '1985-12-12', '234-56-7890', 'Friend', 'Requesting records for personal documentation')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (15, 'Michael Nelson', '333-55-6666', '789 Elm St', '555-6789', 'michaelnelson@example.com', 'Henry Pink', '1995-01-13', '345-67-8901', 'Colleague', 'Requesting records for work-related purposes')",
            "INSERT IGNORE INTO reviewpapers (paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason) VALUES (16, 'Nina Olson', '444-66-7777', '321 Birchwood St', '555-7892', 'ninaolson@example.com', 'Olivia Brown', '2000-02-14', '456-78-9012', 'Neighbor', 'Requesting records for community project')",

            // Example data for reviewqueue
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (3, 3, 'unreviewed', '2023-01-03')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (4, 4, 'unreviewed', '2023-01-04')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (5, 5, 'unreviewed', '2023-01-05')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (6, 6, 'unreviewed', '2023-01-06')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (7, 7, 'unreviewed', '2023-01-07')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (8, 8, 'unreviewed', '2023-01-08')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (9, 9, 'unreviewed', '2023-01-09')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (10, 10, 'unreviewed', '2023-01-10')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (11, 11, 'unreviewed', '2023-01-11')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (12, 12, 'unreviewed', '2023-01-12')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (13, 13, 'unreviewed', '2023-01-13')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (14, 14, 'unreviewed', '2023-01-14')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (15, 15, 'reviewed', '2023-01-15')",
            "INSERT IGNORE INTO reviewqueue (queue_id, paper_id, status, date) VALUES (16, 16, 'reviewed', '2023-01-16')"
        };
        for (String query : queries){
            stmt.addBatch(query);
        }
        stmt.executeBatch();
        stmt.close();
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
        System.out.println(pstmt.toString());

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
}
