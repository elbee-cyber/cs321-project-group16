package edu.gmu.cs321.Reviewer;

import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.ResultSet;
import edu.gmu.cs321.Reviewer.Dashboard;


public class LoginScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Login Screen");

        // gridpane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // for failure alerts (just login right now)
        Alert failAlert = new Alert(Alert.AlertType.ERROR);

        // db connection
        DatabaseQuery db = new DatabaseQuery();
        try{
            db.connect();
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

        // login button action
        loginButton.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String hashedPassword;
            
            Integer currentUserId;
            String currentUserRole;
            String currentUser;

            // try to hash the input password
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                hashedPassword = "";
                for (byte b : hash) {
                    hashedPassword += String.format("%02x", b);
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                failAlert.setTitle("Error occured!");
                failAlert.setHeaderText("Error occured!");
                failAlert.setContentText("Please try again.");
                failAlert.showAndWait();
                return;
            }

            // check that there is a row for username and hash(password)
            try {
                String query = "SELECT * FROM users WHERE username = ? and password = ?";
                ResultSet rs = db.executePQuery(query, username, hashedPassword);

                if(!rs.next())
                    throw new java.sql.SQLException();

                currentUserId = rs.getInt(1);
                currentUserRole = rs.getString(2);
                currentUser = rs.getString(3);

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                failAlert.setTitle("Invalid credentials!");
                failAlert.setHeaderText("Invalid credentials!");
                failAlert.setContentText("Please try again.");
                failAlert.showAndWait();
                return;
            }

            // code to select username from the ResultsSet and open ReviewerDashboard
            primaryStage.hide();
            Dashboard dashboard = new Dashboard(currentUserId, currentUserRole, currentUser, db);
            dashboard.startScreen(primaryStage);
        });

        // scene
        Scene scene = new Scene(grid);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}