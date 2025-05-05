package edu.gmu.cs321;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cs321.DatabaseManager;
import com.cs321.Workflow;


/**
 * # README
 * ## SQL Service Config
 * 1. Add a new user workflow_user in SQL Workbench with password 'password' (Server>Users and Privileges>Add Account)
 * 2. Grant all privileges to the workflow_user (Server>Users and Privileges>Administrative Roles>DBA)
 * 3. Add DB 'workflow_db'
 * 
 * Refer here to more details on the workflow: https://fh6q6ixevu.joplinusercontent.com/shares/HAUH81cT9iQ7j2HbuaFLzS
 */

/**
 * This class handles the database connection and queries for the application.
 * It includes methods to connect to the database, execute queries, and update data.
 */
public class DatabaseQuery {
    // Database connection details
    private Connection connection;
    private Workflow workflow;
    private static DatabaseQuery instance = null; // Singleton instance

    //Ensure only one instance of DatabaseQuery is created (Singleton pattern)
    private DatabaseQuery() throws SQLException {
        System.out.println("Connecting to database...");
        connect();
    }

    public static DatabaseQuery getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseQuery();
        }
        return instance;
    }

    /**
     * Establishes a connection to the database and initializes the user and role-specific tables.
     * 
     * @return the established database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection connect() throws SQLException {        

        DatabaseManager db = new DatabaseManager();
        db.initializeDatabase();
        this.connection = db.getConnection();
        try {   
            this.workflow = new Workflow();
            // create database
            Statement stmt = connection.createStatement();
            String queries[] = {

                /**
                 * Drop all tables to start fresh
                 * FOREIGN_KEY_CHECKS to cascade constraints
                 */
                "SET FOREIGN_KEY_CHECKS = 0",
                "DROP TABLE IF EXISTS users",
                "DROP TABLE IF EXISTS dataqueue",
                "DROP TABLE IF EXISTS requestors",
                "DROP TABLE IF EXISTS deceased",
                "DROP TABLE IF EXISTS salts",
                "DROP TABLE IF EXISTS requestData",
                "DROP TABLE IF EXISTS requestQueue",
                "SET FOREIGN_KEY_CHECKS = 1",

                /**
                 * General User Info Tables
                 */
                // Users contains userid, username, password, role
                "CREATE TABLE IF NOT EXISTS salts (userid INT PRIMARY KEY, salt VARCHAR(255) NOT NULL)",
                "INSERT IGNORE INTO salts (userid, salt) VALUES (1, 'bffc343e7e8d8f9bdbdceca9f1d3d439');", //salt for login authentication

                "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
                "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec'), (2, 'data entry', 'data', 'de28c09a560e498b6fb6ecbc45cd0cde'), (3, 'approver', 'approver', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f');",

                /**
                 * Form data (Our own table for requestData, shared among all roles)
                 * - Corresponds to wf.formID
                 * - wf contains current status of the form
                 */
                "CREATE TABLE IF NOT EXISTS requestData (formID INT PRIMARY KEY AUTO_INCREMENT, requestorName VARCHAR(255) NOT NULL, requestorAddress VARCHAR(255) NOT NULL, requestorSSN VARCHAR(255) NOT NULL, requestorCell VARCHAR(255) NOT NULL, requestorEmail VARCHAR(255) NOT NULL, deceasedName VARCHAR(255) NOT NULL, deceasedDOB VARCHAR(255) NOT NULL, deceasedSSN VARCHAR(255) NOT NULL, deceasedRelationship VARCHAR(255) NOT NULL, requestReason VARCHAR(255) NOT NULL, requestStatus VARCHAR(255) NOT NULL, requestDate DATE NOT NULL, isLegible BOOLEAN NOT NULL DEFAULT 1, isCitizen BOOLEAN NOT NULL DEFAULT 1, rejectionReason VARCHAR(255) DEFAULT NULL)",

                "TRUNCATE TABLE workflow_records",

            };

            for (String query : queries){
                stmt.addBatch(query);
            }

            stmt.executeBatch();
            stmt.close();
/*
            //COMMENT OUT WHEN DEMOING
            //populate requestdata from request.csv
            try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/edu/gmu/cs321/requests.csv"))) {
                String line;
                String query = "INSERT IGNORE INTO requestData (requestorName, requestorAddress, requestorSSN, requestorCell, requestorEmail, deceasedName, deceasedDOB, deceasedSSN, deceasedRelationship, requestReason, requestStatus, requestDate, isLegible, isCitizen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                int formID = 0;
                PreparedStatement pstmt = connection.prepareStatement(query);
                br.readLine(); // Skip the header line
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    String[] address = new String[4];
    
                    for (int i = 0; i < values.length; i++) {
                        if (i == 0) {
                            pstmt.setString(1, values[0]);
                        } else if (i > 0 && i < 5) {
                            if (i == 1) {
                                address[0] = values[i];
                            } else if (i == 2) {
                                address[1] = values[i];
                            } else if (i == 3) {
                                address[2] = values[i];
                            } else if (i == 4) {
                                address[3] = values[i];
                            }
                            if (address[3] != null) {
                                pstmt.setString(2, String.join(",", address));
                            }
                        } else if (i == 15 || i == 16) { // Assuming isLegible and isCitizen are at index 12 and 13
                                pstmt.setBoolean(i - 2, Boolean.parseBoolean(values[i]));
                        } else {
                            pstmt.setString(i - 2, values[i]);
                        }
                        if (i == 13) { //requestStatus
                            if (values[i].equals("Pending Review")) {
                                workflow.AddWFItem(formID, "Review");
                            } else if (values[i].equals("Pending Approval")) {
                                workflow.AddWFItem(formID, "Approve");
                            }
                        }
                    }
                    formID++;
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                pstmt.close();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
                */
        } catch (Exception e) {
            System.out.println("Error workflow: " + e.getMessage());
        } finally {
            try {
                this.workflow.closeConnection();
            } catch (Exception e) {
                System.out.println("Error closing workflow connection: " + e.getMessage());
            }
        }
        return this.connection;
    }

    /**
     * Retrieves the common workflow (used amongst all roles).
     * - Call this and use this from your classes
     */
    public Workflow getWorkflow() {
        return this.workflow;
    }

    public void closeConnection() {
        this.workflow.closeConnection();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
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
