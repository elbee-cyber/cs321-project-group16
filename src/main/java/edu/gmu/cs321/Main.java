package edu.gmu.cs321;

//import edu.gmu.cs321.DataEntry.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        // Create buttons for each role
        Button approverButton = new Button("Approver");
        Button dataEntryButton = new Button("Data Entry");
        Button reviewerButton = new Button("Reviewer");

        // Set button actions
        approverButton.setOnAction(e -> edu.gmu.cs321.Approver.LoginScreen.main(new String[]{}));
        dataEntryButton.setOnAction(e -> edu.gmu.cs321.DataEntry.LoginScreen.main(new String[]{}));
        reviewerButton.setOnAction(e -> edu.gmu.cs321.Reviewer.LoginScreen.main(new String[]{}));

        // Add buttons to a vertical layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(approverButton, dataEntryButton, reviewerButton);

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 300, 200);
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