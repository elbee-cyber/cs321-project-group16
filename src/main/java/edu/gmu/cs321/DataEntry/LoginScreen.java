package edu.gmu.cs321.DataEntry;

import javafx.stage.Stage;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;

// This class represents the login screen for the data entry application, allowing users to enter their credentials and access the system.
public class LoginScreen extends Application {
    
    String selectedRole = "";

    /**
     * Method to hash the password using PBKDF2 with HmacSHA1.
     * This method generates a random salt and hashes the password using the specified algorithm.
     * 
     * @param password
     * @return hashed password as a string
     */
    private String hashPassword(String password) {
        try {
            // Create Salt
            String hexSalt = "bffc343e7e8d8f9bdbdceca9f1d3d439"; //hardcoded salt
            int length = hexSalt.length();
            byte[] salt = new byte[length / 2];
            for (int i = 0; i < length; i += 2) {
                salt[i / 2] = (byte) ((Character.digit(hexSalt.charAt(i), 16) << 4)
                        + Character.digit(hexSalt.charAt(i + 1), 16));
            }

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to start the JavaFX application and set up the login screen UI.
     * 
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {

        //login screen
        primaryStage.setResizable(false);
        primaryStage.setTitle("Data Entry Login");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Connect to database
        DatabaseQuery db = new DatabaseQuery();
        try {
            db.connect();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setTitle("Database Connection Failed");
            failAlert.setHeaderText("Database Connection Failed");
            failAlert.setContentText("Please restart the application.");
            failAlert.showAndWait();
            return;
        }

        // welcome header
        Text header = new Text("Welcome to Data Entry");
        header.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(header, 0, 0, 2, 1);

        // username
        Label userName = new Label("Username:");
        userName.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
        grid.add(userName, 0, 2);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        // password
        Label pw = new Label("Password:");
        pw.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
        grid.add(pw, 0, 3);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

        //login button
        Button loginButton = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        grid.add(hbBtn, 1, 4);

        //forget password function
        Hyperlink forgotPassword = new Hyperlink("I forgot my password.");
        forgotPassword.setTextFill(Color.BLUEVIOLET);
        forgotPassword.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        forgotPassword.setOnAction((ActionEvent e) -> {
            // New window for password reminder
            Stage passwordStage = new Stage();
            passwordStage.setTitle("Password Reminder");
            GridPane passwordGrid = new GridPane();
            passwordGrid.setAlignment(Pos.CENTER);
            passwordGrid.setHgap(10);
            passwordGrid.setVgap(10);
            passwordGrid.setPadding(new Insets(25, 25, 25, 25));
            final Text reminderText = new Text();
            reminderText.setId("reminderText");
            reminderText.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
            passwordGrid.add(reminderText, 0, 2, 2, 1);
            
            Label remindUser = new Label("Username:");
            remindUser.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
            passwordGrid.add(remindUser, 0, 0);
            TextField remindUserText = new TextField();
            passwordGrid.add(remindUserText, 1, 0);
            /* 
            Label remindRole = new Label("Role:");
            remindRole.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
            passwordGrid.add(remindRole, 0, 1);
           
            final ComboBox<String> roles = new ComboBox<>();
            roles.getItems().addAll("Data Entry", "Review", "Approval");
            roles.setPromptText("Select Role");
            passwordGrid.add(roles, 1, 1);

            roles.setOnAction(event -> {
                // Role Selection Logic
                if (remindUserText.getText().equals("admin") && roles.getValue().equals("Data Entry")) {
                    reminderText.setText("Your password is: data");
                } else {
                    reminderText.setText("No password found for this user/role combination.");
                }
            });
            */
            remindUserText.setOnAction(event -> {
                if (remindUserText.getText().equals("data")/* && roles.getValue().equals("Data Entry")*/) {
                    reminderText.setText("Your password is: admin");
                } else {
                    reminderText.setText("No password found for this user/role combination.");
                }
            });
            Button okButton = new Button("OK");
                okButton.setOnAction(event1 -> passwordStage.close());
                passwordGrid.add(okButton, 1, 3);
                GridPane.setHalignment(okButton, HPos.RIGHT);

            Scene passwordReminder = new Scene(passwordGrid, 300, 200);
            passwordStage.setScene(passwordReminder);
            passwordStage.show();
        });
        grid.add(forgotPassword, 0, 4);

        //default login tooltip
        Label defaultLogin = new Label("Default Login: data/admin");
        defaultLogin.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        grid.add(defaultLogin, 1, 5);

        //login success/failure message
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, HPos.RIGHT);
        actiontarget.setId("actiontarget");
        actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));

        //functionality for login button
        loginButton.setOnAction(e -> {
            // Add your login logic here
            String username = userTextField.getText();
            String password = pwBox.getText();

            String hash = hashPassword(password);
            System.out.println("Hashed Password: " + hash);

            try {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                ResultSet rs = db.executePQuery(query, username, hash);
                if (rs.next()) {
                    selectedRole = rs.getString("role");
                    actiontarget.setText("Login successful! Role: " + selectedRole);
                    // Proceed to the next screen or functionality
                    DataDashboard dashboard = new DataDashboard(username, selectedRole);
                    dashboard.start(primaryStage);
                } else {
                    actiontarget.setText("Invalid credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

/*
            if (username.equals("data") && password.equals("guest")) {
                actiontarget.setText("Login successful!");
                // Proceed to the next screen or functionality
                DataDashboard dashboard = new DataDashboard(username, selectedRole);
                dashboard.start(primaryStage);
            } else {
                actiontarget.setText("Invalid credentials.");
            }
*/
        });

        //keyboard event for login button (ENTER key)
        grid.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        //initialize the screen
        Scene scene = new Scene(grid, 400, 220);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Platform.runLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            Stage primaryStage = new Stage();
            loginScreen.start(primaryStage);
        });
    }
}
