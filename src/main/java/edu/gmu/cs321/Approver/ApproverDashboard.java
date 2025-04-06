package edu.gmu.cs321.Approver;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Approver Dashboard where an approver can view records,
 * approve or reject them with feedback.
 */
public class ApproverDashboard extends Application {

    private List<ImmigrationRequest> immigrationRequests;

    /**
     * Starts the application and sets up the UI for the Approver Dashboard.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize some placeholder immigration requests
        immigrationRequests = new ArrayList<>();
        immigrationRequests.add(new ImmigrationRequest("1", "John Doe", "USA", "Jane Doe", true, "Pending", "2025-04-01"));
        immigrationRequests.add(new ImmigrationRequest("2", "Alice Smith", "Canada", "Bob Smith", true, "Pending", "2025-03-29"));
        immigrationRequests.add(new ImmigrationRequest("3", "Carlos Lopez", "Mexico", "Maria Lopez", false, "Pending", "2025-04-03"));

        // Create the ListView to display the records
        ListView<ImmigrationRequest> recordListView = new ListView<>();
        recordListView.getItems().addAll(immigrationRequests);

        // Set the cell factory to display the key details of each request
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
                selectedRecord.setRequestStatus("Approved");
                showConfirmation("Approved", selectedRecord);
            } else {
                showErrorMessage("Please select a record to approve.");
            }
        });

        // Handle reject button click
        rejectButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                showRejectionDialog(selectedRecord);
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
     * Displays a dialog to capture the rejection reason.
     *
     * @param record the record being rejected
     */
    private void showRejectionDialog(ImmigrationRequest record) {
        TextInputDialog rejectionDialog = new TextInputDialog();
        rejectionDialog.setTitle("Rejection Reason");
        rejectionDialog.setHeaderText("Please provide a reason for rejection.");
        rejectionDialog.setContentText("Reason:");

        rejectionDialog.showAndWait().ifPresent(reason -> {
            record.setRequestStatus("Rejected");
            showConfirmation("Rejected", record);
        });
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

    /**
     * The main entry point for launching the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
