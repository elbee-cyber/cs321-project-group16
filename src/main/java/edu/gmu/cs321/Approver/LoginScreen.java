package edu.gmu.cs321.Approver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        // Create the UI elements for login
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");

        // Handle the login action
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password)) {
                // Redirect to the dashboard after successful login
                showDashboard(primaryStage);
            } else {
                // Show an error message for failed login
                showErrorMessage("Invalid credentials. Please try again.");
            }
        });

        // Layout and Scene
        VBox vbox = new VBox(10, usernameField, passwordField, loginButton, forgotPasswordLink);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(300, 200);

        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Approver Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Validates login credentials.
     *
     * @param username the username entered
     * @param password the password entered
     * @return true if credentials are valid, false otherwise
     */
    private boolean validateLogin(String username, String password) {
        // Example logic to validate credentials (can be replaced with real validation)
        return "approver".equals(username) && "password123".equals(password);
    }

    /**
     * Redirects to the approver dashboard after a successful login.
     *
     * @param primaryStage the current login stage
     */
    private void showDashboard(Stage primaryStage) {
        // Close the current login window
        primaryStage.close();

        // Create and launch the Approver Dashboard
        ApproverDashboard approverDashboard = new ApproverDashboard();
        approverDashboard.start(new Stage());
    }

    /**
     * Displays an error message for invalid login credentials.
     *
     * @param message the error message to display
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        Platform.runLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            Stage primaryStage = new Stage();
            loginScreen.start(primaryStage);
        });
    }
}

