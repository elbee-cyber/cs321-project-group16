package edu.gmu.cs321.Approver;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.gmu.cs321.DatabaseQuery;
import edu.gmu.cs321.ImmigrationRequest;


/**
 * This class represents the Approver Dashboard where an approver can view records,
 * approve or reject them with feedback.
 */
public class ApproverDashboard extends Application {

    private List<ImmigrationRequest> immigrationRequests;
    private DatabaseQuery dbQuery;

    // Current user data (passed from login)
    private Integer currentUserId;
    private String currentUserRole;
    private String currentUser;

    /**
     * Starts the application and sets up the UI for the Approver Dashboard.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize database and lists
        dbQuery = new DatabaseQuery();
        immigrationRequests = new ArrayList<>();
        
        // Simulate current user info (this could be set dynamically after login)
        currentUserId = 1; // Placeholder for the logged-in user ID
        currentUserRole = "approver"; // Placeholder for user role
        currentUser = "guest"; // Placeholder for the username

        // Fetch pending immigration requests for the approver from the database
        fetchPendingRequests();

        // Create ListView to display the records
        ListView<ImmigrationRequest> recordListView = new ListView<>();
        recordListView.getItems().addAll(immigrationRequests);

        // Set the cell factory to display details of each request
        recordListView.setCellFactory(new Callback<ListView<ImmigrationRequest>, ListCell<ImmigrationRequest>>() {
            @Override
            public ListCell<ImmigrationRequest> call(ListView<ImmigrationRequest> param) {
                return new ListCell<ImmigrationRequest>() {
                    @Override
                    protected void updateItem(ImmigrationRequest item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText("Request ID: " + item.getFormID() + ", Status: " + item.getRequestStatus() + ", Date: " + item.getSubmissionDate());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        // Create buttons for approving and rejecting records
        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Reject");

        // Handle approve button click
        approveButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                try {
                    // Update status in the database and locally
                    dbQuery.updateRequestStatus(selectedRecord.getRequestID(), "Approved");
                    selectedRecord.setRequestStatus("Approved");
                    showConfirmation("Approved", selectedRecord);
                } catch (SQLException e) {
                    showErrorMessage("Error approving the record.");
                }
            } else {
                showErrorMessage("Please select a record to approve.");
            }
        });

        // Handle reject button click
        rejectButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                try {
                    // Update status in the database and locally
                    dbQuery.updateRequestStatus(selectedRecord.getRequestID(), "Rejected");
                    selectedRecord.setRequestStatus("Rejected");
                    showConfirmation("Rejected", selectedRecord);
                } catch (SQLException e) {
                    showErrorMessage("Error rejecting the record.");
                }
            } else {
                showErrorMessage("Please select a record to reject.");
            }
        });

        // Layout the components in a VBox
        VBox vbox = new VBox(10, recordListView, approveButton, rejectButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(400, 300);

        // Set the scene and show the stage
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Approver Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Fetches pending immigration requests from the database for the approver.
     */
    private void fetchPendingRequests() {
        try {
            dbQuery.connect();
            ResultSet resultSet = dbQuery.getAllImmigrationRequests();
            while (resultSet.next()) {
                immigrationRequests.add(new ImmigrationRequest(
                        resultSet.getString("request_id"),
                        resultSet.getString("requestor_name"),
                        resultSet.getString("requestor_citizenship"),
                        resultSet.getString("deceased_person_name"),
                        resultSet.getBoolean("is_legible"),
                        resultSet.getString("request_status"),
                        resultSet.getString("submission_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a confirmation dialog when a record is approved or rejected.
     *
     * @param action the action taken (approved or rejected)
     * @param record the record being processed
     */
    private void showConfirmation(String action, ImmigrationRequest record) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(action);
        alert.setHeaderText(null);
        alert.setContentText("Request ID " + record.getFormID() + " has been " + action.toLowerCase() + ".");
        alert.showAndWait();
    }

    /**
     * Displays an error message when no record is selected for approval or rejection.
     *
     * @param message the error message to be displayed
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
