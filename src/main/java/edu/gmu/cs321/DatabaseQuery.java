package edu.gmu.cs321;
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
        } catch (Exception e) {
            System.out.println("Error workflow: " + e.getMessage());
        } finally {
            try {
                this.workflow.closeConnection();
            } catch (Exception e) {
                System.out.println("Error closing workflow connection: " + e.getMessage());
            }
        }

        // create database
        Statement stmt = connection.createStatement();
        String queries[] = {

            /**
             * Drop all tables to start fresh
             * FOREIGN_KEY_CHECKS to cascade constraints
             */
            "SET FOREIGN_KEY_CHECKS = 0",
            "DROP TABLE IF EXISTS users",
            "DROP TABLE IF EXISTS salts",
            "DROP TABLE IF EXISTS requestData",
            "SET FOREIGN_KEY_CHECKS = 1",

            /**
             * General User Info Tables
             */
            // Users contains userid, username, password, role
            "CREATE TABLE IF NOT EXISTS salts (userid INT PRIMARY KEY, salt VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO salts (userid, salt) VALUES (1, 'bffc343e7e8d8f9bdbdceca9f1d3d439');",
            "CREATE TABLE IF NOT EXISTS users (userid INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL)",
            "INSERT IGNORE INTO users (userid, role, username, password) VALUES (1, 'reviewer', 'guest', '84983c60f7daadc1cb8698621f802c0d9f9a3c3c295c810748fb048115c186ec'), (2, 'data entry', 'data', 'de28c09a560e498b6fb6ecbc45cd0cde'), (3, 'approver', 'approver', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f');",

            "UPDATE users SET password = 'de28c09a560e498b6fb6ecbc45cd0cde' WHERE userid = 2;",
            "UPDATE users SET username = 'data' WHERE userid = 2;",

            "UPDATE users SET password = '9f3a5e0bdcaa80985b2a03c62203fc1ea761433ac63b0042da4f5f250166c1d0' WHERE userid = 3;",
            "UPDATE users SET username = 'approver' WHERE userid = 3;",

            /**
             * Form data (Our own table for requestData, shared among all roles)
             * - Corresponds to wf.formID
             * - wf contains current status of the form
             */
            "CREATE TABLE IF NOT EXISTS requestData (formID INT PRIMARY KEY AUTO_INCREMENT, requestorName VARCHAR(255) NOT NULL, requestorAddress VARCHAR(255) NOT NULL, requestorSSN VARCHAR(255) NOT NULL, requestorCell VARCHAR(255) NOT NULL, requestorEmail VARCHAR(255) NOT NULL, deceasedName VARCHAR(255) NOT NULL, deceasedDOB VARCHAR(255) NOT NULL, deceasedSSN VARCHAR(255) NOT NULL, deceasedRelationship VARCHAR(255) NOT NULL, requestReason VARCHAR(255) NOT NULL)",

            /**
             * Fake form data for testing - remove later
             * - Corresponds to wf.formID
             * - wf contains current status of the form
             * - Change status in AddWFItem for testing your role
             */
            "INSERT INTO requestData (requestorName, requestorAddress, requestorSSN, requestorCell, requestorEmail, deceasedName, deceasedDOB, deceasedSSN, deceasedRelationship, requestReason) VALUES ('John Doe', '123 Elm St, Springfield, IL', '123-45-6789', '555-123-4567', 'john.doe@example.com', 'Jane Doe', '1950-03-12', '987-65-4321', 'Mother', 'Genealogy research'), ('Mary Johnson', '456 Oak Ave, Centerville, OH', '234-56-7890', '555-234-5678', 'mary.johnson@example.com', 'Robert Johnson', '1945-07-30', '876-54-3210', 'Father', 'Estate settlement'), ('Alex Smith', '789 Pine Rd, Denver, CO', '345-67-8901', '555-345-6789', 'alex.smith@example.com', 'William Smith', '1965-11-22', '765-43-2109', 'Uncle', 'Family records'), ('Laura Brown', '321 Maple St, Austin, TX', '456-78-9012', '555-456-7890', 'laura.brown@example.com', 'Nancy Brown', '1933-05-15', '654-32-1098', 'Grandmother', 'Historical preservation'), ('Kevin Lee', '987 Birch Blvd, Seattle, WA', '567-89-0123', '555-567-8901', 'kevin.lee@example.com', 'Thomas Lee', '1920-01-09', '543-21-0987', 'Grandfather', 'VA benefits inquiry'), ('Rachel Adams', '159 Cedar Way, Portland, OR', '678-90-1234', '555-678-9012', 'rachel.adams@example.com', 'Eleanor Adams', '1955-08-18', '432-10-9876', 'Mother', 'Medical records request'), ('Michael Chen', '753 Walnut Dr, San Jose, CA', '789-01-2345', '555-789-0123', 'michael.chen@example.com', 'Steven Chen', '1949-02-27', '321-09-8765', 'Father', 'Military service history'), ('Emily Garcia', '852 Poplar St, Tampa, FL', '890-12-3456', '555-890-1234', 'emily.garcia@example.com', 'Carmen Garcia', '1970-09-14', '210-98-7654', 'Sister', 'Legal documentation'), ('Brian Thompson', '951 Spruce Ln, Boston, MA', '901-23-4567', '555-901-2345', 'brian.thompson@example.com', 'George Thompson', '1938-06-01', '109-87-6543', 'Grandfather', 'Court order compliance'), ('Sophia Martinez', '147 Chestnut Ave, Phoenix, AZ', '012-34-5678', '555-012-3456', 'sophia.martinez@example.com', 'Isabel Martinez', '1962-12-08', '098-76-5432', 'Aunt', 'Ancestry verification'), ('Nathan Brooks', '301 Fir St, Raleigh, NC', '135-79-2468', '555-111-2222', 'nathan.brooks@example.com', 'Hannah Brooks', '1959-10-03', '234-56-7891', 'Mother', 'Death certificate request'), ('Chloe Nguyen', '204 Cypress Rd, Houston, TX', '246-80-1357', '555-333-4444', 'chloe.nguyen@example.com', 'Minh Nguyen', '1943-04-17', '321-65-4987', 'Father', 'Insurance claim'), ('Dylan Rivera', '90 Hemlock Ln, Miami, FL', '357-91-2468', '555-222-3333', 'dylan.rivera@example.com', 'Carlos Rivera', '1960-06-25', '567-89-0124', 'Uncle', 'Probate court'), ('Isabella Wright', '675 Palm Dr, San Diego, CA', '468-02-5791', '555-444-5555', 'isabella.wright@example.com', 'Margaret Wright', '1952-09-09', '678-90-1235', 'Aunt', 'Academic records'), ('Ethan Patel', '48 Dogwood Ave, Atlanta, GA', '579-13-6802', '555-555-6666', 'ethan.patel@example.com', 'Raj Patel', '1936-12-31', '789-01-2346', 'Grandfather', 'Legal appeal'), ('Olivia Kim', '81 Redwood Blvd, Minneapolis, MN', '680-24-7913', '555-666-7777', 'olivia.kim@example.com', 'Soo Kim', '1948-02-28', '890-12-3457', 'Mother', 'Immigration record request'), ('Liam Hall', '270 Sequoia St, Baltimore, MD', '791-35-8024', '555-777-8888', 'liam.hall@example.com', 'Harold Hall', '1966-05-05', '901-23-4568', 'Father', 'Funeral arrangement records'), ('Ava Scott', '364 Willow Rd, New Orleans, LA', '802-46-9135', '555-888-9999', 'ava.scott@example.com', 'Emma Scott', '1931-11-11', '012-34-5679', 'Grandmother', 'Historical documentation'), ('Noah Green', '512 Ash Ct, Salt Lake City, UT', '913-57-0246', '555-999-0000', 'noah.green@example.com', 'Matthew Green', '1940-07-22', '123-45-6790', 'Grandfather', 'Military burial benefit'), ('Mia Edwards', '678 Cottonwood Pl, Kansas City, MO', '024-68-1357', '555-000-1111', 'mia.edwards@example.com', 'Sarah Edwards', '1975-03-03', '234-56-7892', 'Sister', 'Pension claim')",
        };

        for (String query : queries){
            stmt.addBatch(query);
        }
        for (int i = 0; i < 20; i++){
            this.workflow.AddWFItem(i+1, "Review");
        }

        stmt.executeBatch();
        stmt.close();

        /*
        //populate requestors from living.csv
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/edu/gmu/cs321/living.csv"))) {
            String line;
            String query = "INSERT IGNORE INTO requestors (requestorName, requestorAddress, requestorCitizenship, requestorSSN, requestorCell, requestorEmail) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
 
                for (int i = 0; i < values.length; i++) {
                    pstmt.setString(i + 1, values[i]);
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
            String query = "INSERT IGNORE INTO deceased (deceasedName, deceasedDOB, deceasedSSN) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
 
                for (int i = 0; i < values.length; i++) {
                    pstmt.setString(i + 1, values[i]);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
            */
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
