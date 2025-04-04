package edu.gmu.cs321.Reviewer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import edu.gmu.cs321.DatabaseQuery; 


public class Dashboard {
    Integer currentUserId;
    String currentUserRole;
    String currentUser;
    DatabaseQuery db;

    public Dashboard(Integer currentUserId, String currentUserRole, String currentUser, DatabaseQuery db){
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
        this.currentUser = currentUser;
        this.db = db;
    }

    public void startScreen(Stage primaryStage) {
        
        primaryStage.setTitle("Review Dashboard");

        // gridpane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // scene
        Scene scene = new Scene(grid);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}