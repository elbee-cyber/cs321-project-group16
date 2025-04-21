package edu.gmu.cs321.Approver;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.gmu.cs321.DatabaseQuery;
import edu.gmu.cs321.ImmigrationRequest;



/**
 * This class represents the user interface for the Approver Dashboard in the immigration request system.
 * The approver is able to view requests, approve, reject, or send them back, with the ability to enter comments for rejection or sending back.
 * This class extends Application and is responsible for initializing and displaying the dashboard layout, 
 * managing request records, and interacting with the approver through buttons and form fields.
 * 
 * The main functionalities include:
 * <ul>
 *   <li>Displaying a list of immigration requests.</li>
 *   <li>Allowing the approver to approve, reject, or send back a request.</li>
 *   <li>Validating the comments text field for rejection or sending back.</li>
 *   <li>Displaying dynamic request details based on selection.</li>
 * </ul>
 */
public class ApproverDashboard extends Application {

    private List<ImmigrationRequest> immigrationRequests;
    private TextArea requestDetailsArea;
    private VBox requestDetailsBox;
    private VBox deceasedInfoBox;
    private VBox formattedRequestDetailsBox;
    private TextField commentsField;

    /**
     * This method is the entry point for the JavaFX application.
     * It initializes and sets up the UI components of the Approver Dashboard, including the list of requests, buttons for actions,
     * and text fields to capture comments for rejection or sending back. The method also manages the navigation and layout of the dashboard.
     *
     * @param primaryStage the primary stage for this application, provided by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        immigrationRequests = new ArrayList<>();
        // Adding some sample immigration requests
        immigrationRequests.add(new ImmigrationRequest("1", "John Doe", "USA", "Jane Doe", true, "Pending", "2025-04-01"));
        immigrationRequests.add(new ImmigrationRequest("2", "Alice Smith", "USA", "Bob Smith", true, "Pending", "2025-03-29"));
        immigrationRequests.add(new ImmigrationRequest("3", "Carlos Lopez", "USA", "Maria Lopez", false, "Pending", "2025-04-03"));

        // Create the top bar (Approver: John Doe and Logout button)
        Label approverLabel = new Label("Approver: John Doe");
        Button logoutButton = new Button("Logout");
        HBox topBar = new HBox(10, approverLabel, logoutButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-padding: 10px; -fx-background-color: #cccccc;");

        // Navigation Buttons (Pending, Approved, Rejected, Dashboard)
        Button pendingButton = new Button("Pending");
        Button approvedButton = new Button("Approved");
        Button rejectedButton = new Button("Rejected");
        Button dashboardButton = new Button("Dashboard");
        HBox navigationBar = new HBox(10, pendingButton, approvedButton, rejectedButton, dashboardButton);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setStyle("-fx-padding: 10px; -fx-background-color: #e6e6e6;");

        // ListView to display immigration requests
        ListView<ImmigrationRequest> recordListView = new ListView<>();
        recordListView.getItems().addAll(immigrationRequests);

        // Set the cell factory to display key details of each request
        recordListView.setCellFactory(new Callback<ListView<ImmigrationRequest>, ListCell<ImmigrationRequest>>() {
            @Override
            public ListCell<ImmigrationRequest> call(ListView<ImmigrationRequest> param) {
                return new ListCell<ImmigrationRequest>() {
                    @Override
                    protected void updateItem(ImmigrationRequest item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText("Request ID: " + item.getRequestID() + ", Status: " + item.getRequestStatus() + ", Date: " + item.getSubmissionDate());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        // TextArea to display the details of the selected request
        requestDetailsArea = new TextArea();
        requestDetailsArea.setEditable(false);

        // Button to approve a request
        Button approveButton = new Button("Approve");
        approveButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                selectedRecord.setRequestStatus("Approved");
                requestDetailsArea.setText("Request Approved\n" + formatRequestDetails(selectedRecord));
            } else {
                showErrorMessage("Please select a record to approve.");
            }
        });

        // Button to reject a request
        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                String comments = commentsField.getText();
                if (comments.isEmpty()) {
                    showErrorMessage("Please provide a reason for rejection.");
                } else {
                    selectedRecord.setRequestStatus("Rejected");
                    requestDetailsArea.setText("Request Rejected\n" + formatRequestDetails(selectedRecord));
                    commentsField.clear(); // Clear the comments field after rejection
                }
            } else {
                showErrorMessage("Please select a record to reject.");
            }
        });

        // Button to send back a request
        Button sendBackButton = new Button("Send Back");
        sendBackButton.setOnAction(event -> {
            ImmigrationRequest selectedRecord = recordListView.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                String comments = commentsField.getText();
                if (comments.isEmpty()) {
                    showErrorMessage("Please provide a reason to send the request back.");
                } else {
                    selectedRecord.setRequestStatus("Sent Back");
                    requestDetailsArea.setText("Request Sent Back\n" + formatRequestDetails(selectedRecord));
                    commentsField.clear(); // Clear the comments field after sending back
                }
            } else {
                showErrorMessage("Please select a record to send back.");
            }
        });

        // Set action for record selection to show details in TextArea
        recordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update the request details, deceased info, and formatted request details dynamically
                updateRequestDetails(newValue);
                updateFormattedRequestDetails(newValue);
            }
        });

        // Request Details Section
        requestDetailsBox = new VBox(10);
        requestDetailsBox.setStyle("-fx-padding: 10px;");

        // Deceased Immigrant Information Section
        deceasedInfoBox = new VBox(10);
        deceasedInfoBox.setStyle("-fx-padding: 10px;");

        // Formatted Request Details Section
        formattedRequestDetailsBox = new VBox(10);
        formattedRequestDetailsBox.setStyle("-fx-padding: 10px;");

        // Comments TextField
        commentsField = new TextField();
        commentsField.setPromptText("Enter comments...");

        // Action Panel (Approve, Reject, Send Back)
        HBox actionPanel = new HBox(10);
        actionPanel.getChildren().addAll(approveButton, rejectButton, sendBackButton, commentsField);
        actionPanel.setAlignment(Pos.CENTER);

        // Submit Decision Button
        Button submitButton = new Button("Submit Decision");

        // Status Timeline
        Label statusTimeline = new Label("Status Timeline: Submitted → Data Entry → Review → Approval");
        statusTimeline.setStyle("-fx-padding: 10px;");

        // Layout for the main content
        VBox mainContent = new VBox(20, requestDetailsBox, deceasedInfoBox, formattedRequestDetailsBox, actionPanel, submitButton, statusTimeline);
        mainContent.setStyle("-fx-padding: 20px;");

        // Combine everything into a VBox for the entire dashboard
        VBox dashboardLayout = new VBox(10, topBar, navigationBar, recordListView, requestDetailsArea, mainContent);
        dashboardLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Set up the scene and stage
        Scene scene = new Scene(dashboardLayout, 800, 600);
        
        // Set the title of the application window
        primaryStage.setTitle("Approver Dashboard");
        
        // Set the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Updates the request details dynamically in the UI.
     *
     * @param request the selected immigration request
     */
    private void updateRequestDetails(ImmigrationRequest request) {
        // Update request details dynamically
        requestDetailsBox.getChildren().clear();
        requestDetailsBox.getChildren().addAll(
                new Label("REQUEST DETAILS"),
                new Label("Request ID: " + request.getRequestID()),
                new Label("Requester: " + request.getRequestorName()),
                new Label("Status: " + request.getRequestStatus()),
                new Label("Submitted: " + request.getSubmissionDate())
        );

        // Update deceased immigrant information dynamically
        deceasedInfoBox.getChildren().clear();
        deceasedInfoBox.getChildren().addAll(
                new Label("Deceased Immigrant Information"),
                new Label("Name: " + request.getDeceasedPersonName()),
                new Label("Date of Birth: " + "Date info here"), // Update accordingly
                new Label("Country: " + request.getRequestorCitizenship()),
                new Label("Date of Death: " + "Date info here") // Update accordingly
        );
    }

    /**
     * Updates the formatted request details dynamically in the UI.
     *
     * @param request the selected immigration request
     */
    private void updateFormattedRequestDetails(ImmigrationRequest request) {
        // Update the formatted request details dynamically
        formattedRequestDetailsBox.getChildren().clear();
        formattedRequestDetailsBox.getChildren().addAll(
                new Label("Formatted Request Details"),
                new Label(formatRequestDetails(request))
        );
    }

    /**
     * Helper method to format the request details into a string.
     *
     * @param request the selected immigration request
     * @return a formatted string with request details
     */
    private String formatRequestDetails(ImmigrationRequest request) {
        return "Request ID: " + request.getRequestID() + "\n" +
               "Requestor Name: " + request.getRequestorName() + "\n" +
               "Citizenship: " + request.getRequestorCitizenship() + "\n" +
               "Deceased Name: " + request.getDeceasedPersonName() + "\n" +
               "Legible: " + (request.isLegible() ? "Yes" : "No") + "\n" +
               "Status: " + request.getRequestStatus() + "\n" +
               "Submission Date: " + request.getSubmissionDate();
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
     * The main method that launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}