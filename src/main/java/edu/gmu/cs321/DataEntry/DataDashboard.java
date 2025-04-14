package edu.gmu.cs321.DataEntry;

import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

//This class represents the main dashboard for data entry, allowing users to view and manage form entries.
public class DataDashboard extends Application {
    
    String username;
    String role;
    DatabaseQuery db;

    /**
     * Constructor to initialize the DataDashboard object with the username and
     * role of the user.
     * @param username The username of the user
     * @param role The role of the user (e.g., admin, user)
     * @param db The database query object to interact with the database
     */
    public DataDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        this.db = new DatabaseQuery();
    }

    /**
     * Method to start the JavaFX application and set up the main dashboard UI.
     * @param primaryStage the primary stage for this application
     */
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
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        grid.add(header, 0, 0);

        ListView<Entry> queue = new ListView<>();
        ObservableList<Entry> entries = FXCollections.observableArrayList();

        // Populate the ListView with data from the database
        try {
            db.connect();
            String query = "SELECT * FROM dataqueue";
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                String requestID = rs.getString("requestID");
                String requestorName = rs.getString("requestorName");
                String requestStatus = rs.getString("requestStatus");

                Entry entry = new Entry(requestID, requestorName, requestStatus);
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        queue.setItems(entries);
        grid.add(queue, 0, 1, 3, 1);
        
        queue.setOnMouseClicked(e -> {
            Entry entryForm = queue.getSelectionModel().getSelectedItem();
            if (entryForm == null) {
                return; // No entry selected, do nothing
            }
            entryForm.start(new Stage());
        });

        Button logout = new Button("Logout");
        logout.setOnAction(e -> {
            primaryStage.close(); // Close the dashboard
            LoginScreen loginScreen = new LoginScreen(); // Create a new login screen instance
            try {
                loginScreen.start(new Stage()); // Start the login screen in a new stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        HBox logoutButtonBox = new HBox(10);
        logoutButtonBox.setAlignment(Pos.CENTER);
        logoutButtonBox.getChildren().add(logout);
        grid.add(logoutButtonBox, 2, 0);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
