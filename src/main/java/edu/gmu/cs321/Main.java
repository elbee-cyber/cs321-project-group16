package edu.gmu.cs321;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


// This is the main class for the application.
public class Main extends Application {
    
    // These boolean variables are used to track the user's role selection.
    private boolean choseDataEntry;
    private boolean choseReviewer;
    private boolean choseApprover;

    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the primary stage and displays the role selection screen.
     * 
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Genealogy Records Request System");

        // Create a label for the title
        Label titleLabel = new Label("Choose your role");

        // Create buttons for each role
        Button approverButton = new Button("Approver");
        Button dataEntryButton = new Button("Data Entry");
        Button reviewerButton = new Button("Reviewer");

        // For each button, change the object's role select appropriately and call the login screen spawner
        approverButton.setOnAction(e -> {
            this.choseApprover = true;
            this.choseDataEntry = false;
            this.choseReviewer = false;
            showLoginScreen(primaryStage);
        });
        dataEntryButton.setOnAction(e -> {
            this.choseApprover = false;
            this.choseDataEntry = true;
            this.choseReviewer = false;
            showLoginScreen(primaryStage);
        });
        reviewerButton.setOnAction(e -> {
            this.choseApprover = false;
            this.choseDataEntry = false;
            this.choseReviewer = true;
            showLoginScreen(primaryStage);
        });

        // Add label and buttons to a vertical layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER); // Center everything
        layout.getChildren().addAll(titleLabel, approverButton, dataEntryButton, reviewerButton);

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method is called when the user selects a role.
     * It closes the current stage and opens the login screen for the selected role.
     * 
     * @param primaryStage the primary stage for this application
     */
    private void showLoginScreen(Stage primaryStage) {
        primaryStage.close();
        if (this.choseDataEntry) {
            edu.gmu.cs321.DataEntry.LoginScreen login = new edu.gmu.cs321.DataEntry.LoginScreen();
            login.start(primaryStage);
        } else if (this.choseReviewer) {
            edu.gmu.cs321.Reviewer.LoginScreen login = new edu.gmu.cs321.Reviewer.LoginScreen();
            login.start(primaryStage);
        } else if (this.choseApprover) {
            edu.gmu.cs321.Approver.LoginScreen login = new edu.gmu.cs321.Approver.LoginScreen();
            login.start(primaryStage);
        }
    }

    /**
     * Launch can only be called with a single UI, so this is the main method that starts the JavaFX application thread.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args); //change the import to try the other login screens.
    }
}