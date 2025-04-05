package edu.gmu.cs321.DataEntry;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;

public class LoginScreen extends Application {
    
    String selectedRole = "";

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

        Text header = new Text("Welcome to Data Entry");
        header.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(header, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        userName.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        pw.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 3);

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
           
            Label remindRole = new Label("Role:");
            remindRole.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
            passwordGrid.add(remindRole, 0, 1);
            final ComboBox<String> roles = new ComboBox<>();
            roles.getItems().addAll("Data Entry", "Review", "Approval");
            roles.setPromptText("Select Role");
            passwordGrid.add(roles, 1, 1);
            roles.setOnAction(event -> {
                if (remindUserText.getText().equals("admin") && roles.getValue().equals("Data Entry")) {
                    reminderText.setText("Your password is: data");
                } else {
                    reminderText.setText("No password found for this user/role combination.");
                }
                Button okButton = new Button("OK");
                okButton.setOnAction(event1 -> passwordStage.close());
                passwordGrid.add(okButton, 1, 3);
                GridPane.setHalignment(okButton, HPos.RIGHT);
            });

            Scene passwordReminder = new Scene(passwordGrid, 300, 200);
            passwordStage.setScene(passwordReminder);
            passwordStage.show();
        });
        grid.add(forgotPassword, 0, 4);

        Label defaultLogin = new Label("Default Login: admin/data");
        defaultLogin.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        grid.add(defaultLogin, 1, 5);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        grid.setColumnSpan(actiontarget, 2);
        grid.setHalignment(actiontarget, HPos.RIGHT);
        actiontarget.setId("actiontarget");
        actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));

        loginButton.setOnAction(e -> {
            // Add your login logic here
            String username = userTextField.getText();
            String password = pwBox.getText();
            if (username.equals("admin") && password.equals("data")) {
                actiontarget.setText("Login successful!");
                // Proceed to the next screen or functionality
                DataDashboard dashboard = new DataDashboard(username, selectedRole);
                dashboard.start(primaryStage);
            } else {
                actiontarget.setText("Invalid credentials.");
            }
        });

        Scene scene = new Scene(grid, 400, 220);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
