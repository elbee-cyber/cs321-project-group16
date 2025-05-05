package edu.gmu.cs321.Approver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import edu.gmu.cs321.Approver.ApproverDashboard;
import edu.gmu.cs321.DatabaseQuery;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class represents the login screen for the approver, where they can authenticate
 * to access the approver dashboard.
 */
public class LoginScreen extends Application {

    /**
     * Starts the login screen and sets up the UI components.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {

        Label pageTitle = new Label("Approver Login");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px;");

        // Create the UI elements for login
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        
        Button loginButton = new Button("Login");
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");

        // Create a label to show the login status
        Label actiontarget = new Label();

        // Handle the login action
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password, primaryStage, actiontarget)) {
                // Redirect to the dashboard after successful login.
            } else {
                // Show an error message for failed login
                actiontarget.setText("Invalid credentials. Please try again.");
            }
        });

        // Layout for the username and password fields
        HBox usernameBox = new HBox(10, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        
        HBox passwordBox = new HBox(10, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER);

        // Layout and Scene
        VBox vbox = new VBox(15, pageTitle, usernameBox, passwordBox, loginButton, forgotPasswordLink, actiontarget);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(300, 200);

        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Validates the login credentials by checking the provided username and password against the database.
     * 
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @param primaryStage the current login stage
     * @param actiontarget the label for displaying login status
     * @return true if the login credentials are valid (username exists and password matches),
     *         false if the credentials are invalid (incorrect username or password),
     *         or if there is a database error.
     * @throws SQLException if a database access error occurs during the query
     */
    private boolean validateLogin(String username, String password, Stage primaryStage, Label actiontarget) {
        DatabaseQuery dbQuery;
    
        try {
            dbQuery = DatabaseQuery.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Database connection error
        }
    
        try {
    
            // Prepare SQL query to check the username and password
            String query = "SELECT * FROM users WHERE username = ?";
            ResultSet resultSet = dbQuery.executePQuery(query, username);
    
            if (resultSet.next()) {
                // Retrieve the stored password hash from the database
                String storedPasswordHash = resultSet.getString("password");
    
                // Hash the input password and compare it with the stored hash
                if (verifyPassword(password, storedPasswordHash)) {
                    // If login is successful, retrieve the role and redirect to the appropriate dashboard
                    String selectedRole = resultSet.getString("role");
                    actiontarget.setText("Login successful! Role: " + selectedRole);
    
                    
                    String usernameFromDb = resultSet.getString("username");
    
                    // Proceed to the appropriate dashboard based on role
                    if ("approver".equals(selectedRole)) {
                        // For Approver role, create the ApproverDashboard with the required parameters
                        ApproverDashboard approverDashboard = new ApproverDashboard(usernameFromDb, selectedRole, dbQuery);
                        approverDashboard.start(primaryStage);
                        
                        // Set up the scene for the Approver dashboard
                        //Scene approverScene = new Scene(approverDashboard.getRootPane());
                        //primaryStage.setScene(approverScene);
                        //primaryStage.setTitle("Approver Dashboard");
                        //primaryStage.show();
                    } else {
                        // For any other role, handle appropriately or show an error
                        actiontarget.setText("Invalid role associated with the username.");
                    }
    
                    return true;  // Login successful
                } else {
                    actiontarget.setText("Incorrect password.");
                    return false;  // Incorrect password
                }
            } else {
                actiontarget.setText("Username doesn't exist.");
                return false;  // Username doesn't exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Database error
        }
    }
    

    /**
     * Verifies the password by hashing the input password and comparing it to the stored hash.
     * 
     * @param inputPassword the password entered by the user
     * @param storedPasswordHash the password hash stored in the database
     * @return true if the passwords match, false otherwise
     */
    private boolean verifyPassword(String inputPassword, String storedPasswordHash) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput != null && hashedInput.equals(storedPasswordHash);
    }

    /**
     * Hashes the provided password using SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password string or {@code null} if an error occurs
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            // Convert byte array to hexadecimal string
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            System.out.println("Hashed password: " + hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays an error message for invalid login credentials.
     *
     * @param message the error message to display
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The main method to launch the login screen.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Platform.runLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            Stage primaryStage = new Stage();
            loginScreen.start(primaryStage);
        });
    }
}
