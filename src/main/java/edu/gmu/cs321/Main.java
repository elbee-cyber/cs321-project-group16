package edu.gmu.cs321;

import edu.gmu.cs321.Approver.LoginScreen;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// This is the main class for the application. It serves as the entry point for the JavaFX application.
public class Main extends Application {
    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the primary stage and displays the role selection screen.
     * 
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Role Selection");

        // Create a label for the heading and instructions
        Label heading = new Label("Genealogy Records Request");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        heading.setAlignment(Pos.CENTER);
        heading.setMaxWidth(Double.MAX_VALUE); // Center the heading
        Label instruction = new Label("Please select your role:");
        instruction.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        // Create buttons for each role
        Button approverButton = new Button("Approver");
        Button dataEntryButton = new Button("Data Entry User");
        Button reviewerButton = new Button("Reviewer");

        // Set button actions
        approverButton.setOnAction(e -> edu.gmu.cs321.Approver.LoginScreen.main(new String[]{}));
        dataEntryButton.setOnAction(e -> edu.gmu.cs321.DataEntry.LoginScreen.main(new String[]{}));
        reviewerButton.setOnAction(e -> edu.gmu.cs321.Reviewer.LoginScreen.main(new String[]{}));

        // Add heading and buttons to a layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(heading, instruction);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f0f0f0;");
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(dataEntryButton, reviewerButton, approverButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().add(buttons);

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method serves as the entry point for the JavaFX application.
     * It launches the LoginScreen class.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args); //change the import to try the other login screens.
    }
}