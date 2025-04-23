package edu.gmu.cs321.Reviewer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * LoginScreen class represents the login screen for the Reviewer role in the application.
 * It allows users to enter their username and password and finds the user in the database where SHA256(password) = password.
    * If the credentials are valid, it opens the ReviewerDashboard.
    * If the credentials are invalid, it shows an error alert.
 */
public class LoginScreen extends Application {

    /**
     * Sets up the primary stage and displays the login screen.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Screen");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // For failure alerts (just login right now)
        Alert failAlert = new Alert(Alert.AlertType.ERROR);

        // db connection
        DatabaseQuery db;
        try{
            db = DatabaseQuery.getInstance();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
            failAlert.setTitle("Database Connection Failed");
            failAlert.setHeaderText("Database Connection Failed");
            failAlert.setContentText("Please restart the application.");
            failAlert.showAndWait();
            return;
        }

        Label headingLabel = new Label("Reviewer Dashboard");
        headingLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(headingLabel, 0, 0, 2, 1);

        Label userLabel = new Label("Username:");
        grid.add(userLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Label defaultCreds = new Label("Default creds: guest/guest");
        grid.add(defaultCreds, 1, 3);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 4);

        // "Forgot Password" clickable label
        Text forgotPassword = new Text("Forgot Password?");
        forgotPassword.setStyle("-fx-underline: true; -fx-text-fill: blue; -fx-cursor: hand;");
        grid.add(forgotPassword, 1, 5);

        // Forgot Password action
        forgotPassword.setOnMouseClicked(event -> {
            Stage forgotPasswordStage = new Stage();
            forgotPasswordStage.setTitle("Forgot Password");

            GridPane forgotPasswordGrid = new GridPane();
            forgotPasswordGrid.setAlignment(Pos.CENTER);
            forgotPasswordGrid.setHgap(10);
            forgotPasswordGrid.setVgap(10);
            forgotPasswordGrid.setPadding(new Insets(25, 25, 25, 25));

            Label emailLabel = new Label("Enter your email:");
            forgotPasswordGrid.add(emailLabel, 0, 0);

            TextField emailField = new TextField();
            forgotPasswordGrid.add(emailField, 1, 0);

            Button resetButton = new Button("Reset Password");
            forgotPasswordGrid.add(resetButton, 1, 1);

            resetButton.setOnAction(closeEvent -> {
                String email = emailField.getText();

                // !!Implement logic to email password reset!!

                forgotPasswordStage.close();
            });

            Scene forgotPasswordScene = new Scene(forgotPasswordGrid, 300, 200);
            forgotPasswordStage.setScene(forgotPasswordScene);
            forgotPasswordStage.show();
        });

        loginButton.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String hashedPassword;

            Integer currentUserId;
            String currentUserRole;
            String currentUser;

            // Try to hash the input password
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                hashedPassword = "";
                for (byte b : hash) {
                    hashedPassword += String.format("%02x", b);
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                failAlert.setTitle("Error occurred!");
                failAlert.setHeaderText("Error occurred!");
                failAlert.setContentText("Please try again.");
                failAlert.showAndWait();
                return;
            }

            // Confirm that there is a row for username and hash(password) where role = 'reviewer'
            try {
                String query = "SELECT * FROM users WHERE username = ? and password = ? and role = 'reviewer'";
                ResultSet rs = db.executePQuery(query, username, hashedPassword);

                if (!rs.next())
                    throw new java.sql.SQLException();

                currentUserId = rs.getInt("userid");
                currentUserRole = rs.getString("role");
                currentUser = rs.getString("username");
            } catch (java.sql.SQLException e) {
                System.out.println("Error: " + e.getMessage());
                failAlert.setTitle("Invalid credentials!");
                failAlert.setHeaderText("Invalid credentials!");
                failAlert.setContentText("Please try again.");
                failAlert.showAndWait();
                return;
            }

            // Open Dashboard with the current session info
            primaryStage.hide();
            Dashboard dashboard = new Dashboard(currentUserId, currentUserRole, currentUser, db);
            dashboard.startScreen(primaryStage);
        });

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Platform.runLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            Stage primaryStage = new Stage();
            loginScreen.start(primaryStage);
        });
    }
}