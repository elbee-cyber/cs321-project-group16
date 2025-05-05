package edu.gmu.cs321.dataentry;

import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import edu.gmu.cs321.DatabaseQuery;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    int numEntries = 0;

    // Create an instance of DatabaseQuery
    private void initializeDatabase() {
        try {
            db = DatabaseQuery.getInstance(); // Create an instance of DatabaseQuery
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DatabaseQuery instance", e);
        }
    }

    /**
     * Constructor to initialize the DataDashboard object with the username and
     * role of the user.
     * @param username The username of the user
     * @param role The role of the user (e.g., admin, user)
     */
    public DataDashboard(String username, String role) {
        this.username = username;
        this.role = role;
    }

    private void updatePreviewBox(VBox previewBox, Entry entryForm) {
        // Clear previous labels if any
        String style = "-fx-font-size: 16px; -fx-text-fill: #333;";
        previewBox.getChildren().clear();
        Label requestIDLabel = new Label("Request ID: " + entryForm.getRequestID() + " ("+ entryForm.getRequestStatus() + ")");
        requestIDLabel.setStyle(style);
        Label requestorNameLabel = new Label("- Requestor Name: " + entryForm.getRequestorName());
        requestorNameLabel.setStyle(style);
        Label requestorCitizenshipLabel = new Label("- Requestor Citizenship: " + entryForm.getRequestorCitizenship());
        requestorCitizenshipLabel.setStyle(style);
        Label deceasedNameLabel = new Label("- Deceased Name: " + entryForm.getDeceasedName());
        deceasedNameLabel.setStyle(style);
        Label submissionDateLabel = new Label("- Submission Date: " + entryForm.getSubmissionDate());
        submissionDateLabel.setStyle(style);
        Label rejectedLabel = new Label("REQUEST HAS BEEN REJECTED");
        rejectedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FF0000; -fx-font-weight: bold;");
        Label rejectionReasonLabel = new Label("- Rejection Reason: " + entryForm.getRejectionReason());
        rejectionReasonLabel.setStyle(style);
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
            String query;
            if (selectedFilter.equals("All")) {
                query = "SELECT * FROM requestData ORDER BY " + (selectedSort.equals("Sort by Request ID") ? "formID" : "requestDate");
            } else {
                query = "SELECT * FROM requestData WHERE requestStatus = '" + selectedFilter + "' ORDER BY " + (selectedSort.equals("Sort by Request ID") ? "formID" : "requestDate");
            }
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                int requestID = rs.getInt("formID");
                String requestorName = rs.getString("requestorName");
                Boolean requestorCitizenship = rs.getBoolean("isCitizen");
                String deceasedName = rs.getString("deceasedName");
                String requestStatus = rs.getString("requestStatus");
                String submissionDate = rs.getString("requestDate");
                String rejectionReason = rs.getString("rejectionReason"); // Get the rejection reason from the database

                Entry entry = new Entry(requestID, requestorName, requestorCitizenship, deceasedName, requestStatus, submissionDate, rejectionReason);
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Obtain total number of entries in the database
        try {
            ResultSet rs = db.executeQuery("SELECT COUNT(*) AS total FROM requestData");
            if (rs.next()) {
                numEntries = rs.getInt("total");
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
        initializeDatabase(); // Initialize the database connection

        primaryStage.setTitle("Data Entry Dashboard");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();

        GridPane grid = setupGridPane();
        Label header = createHeader();
        grid.add(header, 0, 0, 3, 1);

        ComboBox<String> filterComboBox = createFilterComboBox();
        ComboBox<String> sortComboBox = createSortComboBox();
        ListView<Entry> queue = new ListView<>();
        ObservableList<Entry> entries = FXCollections.observableArrayList();
        refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue());
        grid.add(queue, 0, 1, 2, 1);

        VBox previewBox = setupPreviewBox(grid);
        setupQueueEventHandlers(queue, entries, filterComboBox, sortComboBox, previewBox);

        setupFilterAndSortHandlers(filterComboBox, sortComboBox, queue, entries);

        Button logout = createLogoutButton(primaryStage);
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button addButton = new Button("Add New Entry");
        addButton.setOnAction(_ -> {
            try {
                Stage entryStage = new Stage();
                Entry entryForm = new Entry(++numEntries);
                entryForm.start(entryStage);
                entryStage.setOnHiding(_ -> refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonBox.getChildren().addAll(addButton, filterComboBox, sortComboBox, logout);
        grid.add(buttonBox, 1, 0, 3, 1);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane setupGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));
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
        grid.getRowConstraints().addAll(row1, row2);

        return grid;
    }

    private Label createHeader() {
        Label header = new Label("Data Entry Dashboard");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
        return header;
    }

    private ComboBox<String> createFilterComboBox() {
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Pending Data Entry", "Pending Review", "Pending Approval", "Rejected");
        filterComboBox.setValue("Pending Data Entry");
        filterComboBox.setPromptText("Filter by Status");
        return filterComboBox;
    }

    private ComboBox<String> createSortComboBox() {
        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Sort by Request ID", "Sort by Submission Date");
        sortComboBox.setValue("Sort by Request ID");
        sortComboBox.setPromptText("Sort by");
        return sortComboBox;
    }

    private VBox setupPreviewBox(GridPane grid) {
        Rectangle previewRectangle = new Rectangle(300, 500);
        previewRectangle.setFill(Color.LIGHTGRAY);
        VBox previewBox = new VBox(10);
        previewBox.setAlignment(Pos.TOP_LEFT);
        grid.add(previewRectangle, 2, 1, 2, 1);
        grid.add(previewBox, 2, 1, 2, 1);
        return previewBox;
    }

    private void setupQueueEventHandlers(ListView<Entry> queue, ObservableList<Entry> entries, ComboBox<String> filterComboBox, ComboBox<String> sortComboBox, VBox previewBox) {
        queue.setOnMouseClicked(e -> {
            Entry entryForm = queue.getSelectionModel().getSelectedItem();
            if (entryForm == null) {
                return;
            }

            updatePreviewBox(previewBox, entryForm);

            if (e.getClickCount() == 2) {
                handleDoubleClick(entryForm, queue, entries, filterComboBox, sortComboBox, previewBox);
            }
        });
    }

    private void handleDoubleClick(Entry entryForm, ListView<Entry> queue, ObservableList<Entry> entries, ComboBox<String> filterComboBox, ComboBox<String> sortComboBox, VBox previewBox) {
        if (entryForm.getRequestStatus().equals("Pending Data Entry")) {
            try {
                Stage entryStage = new Stage();
                entryForm.start(entryStage);
                entryStage.setOnHiding(_ -> {
                    refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue());
                    updatePreviewBox(previewBox, entryForm);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (entryForm.getRequestStatus().equals("Rejected")) {
            handleRejectedRequest(entryForm, queue, entries, filterComboBox, sortComboBox);
        }
    }

    private void handleRejectedRequest(Entry entryForm, ListView<Entry> queue, ObservableList<Entry> entries, ComboBox<String> filterComboBox, ComboBox<String> sortComboBox) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rejected Request");
        alert.setHeaderText("Request ID: " + entryForm.getRequestID());
        alert.setContentText("This request has been rejected for the following reason:\n" + entryForm.getRejectionReason());
        ButtonType reverseButton = new ButtonType("Reverse Rejection");
        alert.getDialogPane().setContentText("Do you want to reverse the rejection?");
        alert.getDialogPane().getButtonTypes().setAll(reverseButton, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == reverseButton) {
                try {
                    db.executeUpdate("UPDATE requestData SET requestStatus = 'Pending Data Entry' WHERE formID = " + entryForm.getRequestID());
                    refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setupFilterAndSortHandlers(ComboBox<String> filterComboBox, ComboBox<String> sortComboBox, ListView<Entry> queue, ObservableList<Entry> entries) {
        filterComboBox.setOnAction(_ -> refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue()));

        sortComboBox.setOnAction(_ -> refreshQueue(queue, entries, filterComboBox.getValue(), sortComboBox.getValue()));
    }

    private Button createLogoutButton(Stage primaryStage) {
        Button logout = new Button("Logout");
        logout.setOnAction(_ -> {
            primaryStage.close();
            LoginScreen loginScreen = new LoginScreen();
            try {
                loginScreen.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return logout;
    }

    /**
     * Main method to launch the JavaFX application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
