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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    private void updatePreviewBox(VBox previewBox, Entry entryForm) {
        // Clear previous labels if any
        previewBox.getChildren().clear();
        Label requestIDLabel = new Label("Request ID: " + entryForm.getRequestID() + " ("+ entryForm.getRequestStatus() + ")");
        requestIDLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Label requestorNameLabel = new Label("- Requestor Name: " + entryForm.getRequestorName());
        requestorNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        Label requestorCitizenshipLabel = new Label("- Requestor Citizenship: " + entryForm.getRequestorCitizenship());
        requestorCitizenshipLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        Label deceasedNameLabel = new Label("- Deceased Name: " + entryForm.getDeceasedName());
        deceasedNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        Label submissionDateLabel = new Label("- Submission Date: " + entryForm.getSubmissionDate());
        submissionDateLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        Label rejectedLabel = new Label("REQUEST HAS BEEN REJECTED");
        rejectedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF0000; -fx-font-weight: bold;");
        Label rejectionReasonLabel = new Label("- Rejection Reason: " + entryForm.getRejectionReason());
        rejectionReasonLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        if (entryForm.getRequestStatus().equals("Rejected")) {
            previewBox.getChildren().addAll(rejectedLabel, rejectionReasonLabel);
        } else {
            previewBox.getChildren().addAll(requestIDLabel, requestorNameLabel, requestorCitizenshipLabel, deceasedNameLabel, submissionDateLabel);
        }
    }

    // Method to refresh the ListView with updated data from the database
    private void refreshQueue(ListView<Entry> queue, ObservableList<Entry> entries, String selectedFilter, String selectedSort) {
        entries.clear(); // Clear the current entries
        try {
            db.connect();
            String query;
            if (selectedFilter.equals("All")) {
                query = "SELECT * FROM dataqueue ORDER BY " + (selectedSort.equals("Sort by Request ID") ? "requestID" : "submissionDate");
            } else {
                query = "SELECT * FROM dataqueue WHERE requestStatus = '" + selectedFilter + "' ORDER BY " + (selectedSort.equals("Sort by Request ID") ? "requestID" : "submissionDate");
            }
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                String requestID = rs.getString("requestID");
                String requestorName = rs.getString("requestorName");
                String requestorCitizenship = rs.getString("requestorCitizenship");
                String deceasedName = rs.getString("deceasedName");
                String requestStatus = rs.getString("requestStatus");
                String submissionDate = rs.getString("submissionDate");
                String rejectionReason = rs.getString("rejectionReason"); // Get the rejection reason from the database

                Entry entry = new Entry(requestID, requestorName, requestorCitizenship, deceasedName, requestStatus, submissionDate, rejectionReason);
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        queue.setItems(entries); // Update the ListView with new entries
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

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(30);
        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(30);
        ColumnConstraints thirdColumn = new ColumnConstraints();
        thirdColumn.setPercentWidth(30);
        ColumnConstraints fourthColumn = new ColumnConstraints();
        fourthColumn.setPercentWidth(10);
        grid.getColumnConstraints().addAll(firstColumn, secondColumn, thirdColumn, fourthColumn);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(90);

        Label header = new Label("Data Entry Dashboard");
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        grid.add(header, 0, 0, 3, 1);

        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Pending Data Entry", "Pending Review", "Rejected");
        filterComboBox.setValue("All"); // Set default value
        filterComboBox.setPromptText("Filter by Status");
        String filter = filterComboBox.getValue(); // Get the selected filter value

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Sort by Request ID", "Sort by Submission Date");
        sortComboBox.setValue("Sort by Request ID"); // Set default value
        sortComboBox.setPromptText("Sort by");
        String sort = sortComboBox.getValue(); // Get the selected sort value

        ListView<Entry> queue = new ListView<>();
        ObservableList<Entry> entries = FXCollections.observableArrayList();

        // Populate the ListView with data from the database
        refreshQueue(queue, entries, filter, sort);

        grid.add(queue, 0, 1, 2, 1);

        // Vbox to hold the preview of the selected entry
        Rectangle previewRectangle = new Rectangle(300, 500);
        previewRectangle.setFill(Color.LIGHTGRAY);
        VBox previewBox = new VBox(10);
        previewBox.setAlignment(Pos.TOP_LEFT);

        grid.add(previewRectangle, 2, 1, 2, 1);
        grid.add(previewBox, 2, 1, 2, 1);
        
        queue.setOnMouseClicked(e -> {
            Entry entryForm = queue.getSelectionModel().getSelectedItem();
            if (entryForm == null) {
                return; // No entry selected, do nothing
            }

            updatePreviewBox(previewBox, entryForm);

            if (e.getClickCount() == 2) {
                // Open the entry form in a new window
                try {
                    Stage entryStage = new Stage();
                    entryForm.start(entryStage);
                    entryStage.setOnHiding(event -> {
                        refreshQueue(queue, entries, filter, sort); // Refresh the ListView when the entry form is closed
                        updatePreviewBox(previewBox, entryForm);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        filterComboBox.setOnAction(e -> {
            String selectedFilter = filterComboBox.getValue();
            String selectedSort = sortComboBox.getValue(); // Get the selected sort value
            refreshQueue(queue, entries, selectedFilter, selectedSort); // Refresh the ListView with filtered entries
        });

        sortComboBox.setOnAction(e -> {
            String selectedSort = sortComboBox.getValue();
            String selectedFilter = filterComboBox.getValue(); // Get the selected filter value
            refreshQueue(queue, entries, selectedFilter, selectedSort); // Refresh the ListView with sorted entries
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
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(filterComboBox, sortComboBox, logout);
        grid.add(buttonBox, 1, 0, 3, 1);

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
