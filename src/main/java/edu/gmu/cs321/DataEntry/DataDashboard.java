package edu.gmu.cs321.DataEntry;

import javafx.stage.Stage;
import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class DataDashboard extends Application {
    
    String username;
    String role;
    DatabaseQuery db;

    public DataDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        this.db = new DatabaseQuery();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Entry Dashboard");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        GridPane grid = new GridPane();

        // gridpane layout       
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));
        //grid.setGridLinesVisible(true);
        grid.setPrefSize(800, 600);

        Label header = new Label("Data Entry Dashboard");
        grid.add(header, 0, 0);
        
        Button openEntryButton = new Button("Open Entry Form");
        openEntryButton.setOnAction(e -> {
            // Logic to open the entry form goes here
            Entry entryForm = new Entry(username, role);
            entryForm.start(new Stage());
        });
        grid.add(openEntryButton, 0, 1);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
