package edu.gmu.cs321.Reviewer;

import java.sql.ResultSet;

import edu.gmu.cs321.DatabaseQuery;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Reviewer Dashboard instance with the specified user details and database connection.
 *
 * @param currentUserId   The ID of the currently logged-in user.
 * @param currentUserRole The role of the currently logged-in user (e.g., "Reviewer").
 * @param currentUser     The name of the currently logged-in user.
 * @param db              The database query object used for executing queries.
 */
public class Dashboard {
    // Current user data + DB conn (passed from login)
    private Integer currentUserId;
    private String currentUserRole;
    private String currentUser;
    private DatabaseQuery db;

    // Queue items currently displayed in top bar
    private Integer[] queue_ids;

    // Selected top bar item
    private Integer selectedQueueId = 0;

    // Used to record indexes for arrow navigation
    private Integer leftMostItem = 0;
    private Integer rightMostItem = -1;

    // Selected top bar item sort
    private String topbarSortDate = "date ASC";
    private String topbarSortStatus = "status = 'unreviewed'";

    // Topbar buttons
    Button leftArrowButton = new Button("<");
    Button rightArrowButton = new Button(">");
    ComboBox<String> dateFilter = new ComboBox<>();
    ComboBox<String> statusFilter = new ComboBox<>();

    // Form view items
    TextField nameField = new TextField("Placeholder");
    TextArea addressArea = new TextArea("Placeholder");
    TextField ssnField = new TextField("Placeholder");
    TextField cellField = new TextField("Placeholder");
    TextField emailField = new TextField("Placeholder");
    TextField deceasedNameField = new TextField("Placeholder");
    TextField deceasedDOBField = new TextField("Placeholder");
    TextField deceasedSSNField = new TextField("Placeholder");
    TextField deceasedRelaField = new TextField("Placeholder");
    TextArea reasonReqArea = new TextArea("Placeholder");

    // Comments area
    TextArea commentsArea = new TextArea();

    /**
     * Constructor for the Dashboard class.
     */
    public Dashboard(Integer currentUserId, String currentUserRole, String currentUser, DatabaseQuery db){
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
        this.currentUser = currentUser;
        this.db = db;
    }

    /**
     * Gets the queue data from the database based on the current filters and navigation buttons.
     * @param clickedLeft  Indicates if the left arrow button was clicked.
     * @param clickedRight Indicates if the right arrow button was clicked.
     * @return An array of strings representing the queue data (dates).
     */
    private String[] getQueueData(Boolean clickedLeft, Boolean clickedRight) {
        String[] results = new String[10];
        this.queue_ids = new Integer[10];

        // SQL query to get the queue data based on the current filters and navigation buttons
        String query = "SELECT * FROM reviewqueue WHERE " + this.topbarSortStatus + " ORDER BY " + this.topbarSortDate + " LIMIT 10";
        if (clickedLeft) {
            query = "SELECT * FROM reviewqueue WHERE queue_id < " + this.leftMostItem + " AND " + this.topbarSortStatus + " ORDER BY " + this.topbarSortDate + " LIMIT 10";
        } else if (clickedRight) {
            query = "SELECT * FROM reviewqueue WHERE queue_id > " + this.rightMostItem + " AND " + this.topbarSortStatus + " ORDER BY " + this.topbarSortDate + " LIMIT 10";
        }

        // Execute the query and populate the results array
        try {
            ResultSet rs = db.executeQuery(query);
            int i = 1;
            while (rs.next()){
                results[i-1] = rs.getDate("date").toLocalDate().toString();
                this.queue_ids[i-1] = rs.getInt("queue_id");
                // Set the leftMostItem and rightMostItem based on the current index
                if (i == 1) {
                    this.leftMostItem = this.queue_ids[i-1];
                    this.selectedQueueId = this.queue_ids[i-1];
                }
                this.rightMostItem = this.queue_ids[i-1];
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error: " +e.getMessage());
            return null;
        }
        selectQueueItem();
        return results;
    }

    /**
     * Updates the queue data displayed in the top bar based on the current filters and navigation buttons.
     * @param dateBar The HBox containing the date buttons in the top bar.
     *               This will be updated with the new queue data.
     */
    private void updateQueueData(HBox dateBar, Boolean clickedLeft, Boolean clickedRight) {
        String[] queueDates = getQueueData(clickedLeft, clickedRight);
        dateBar.getChildren().clear(); // Clear previous buttons
        int i = 0;
        for(String date: queueDates) {
            if (date != null) {
                Button dateButton = new Button(date);
                Integer queueId = this.queue_ids[i];
                dateButton.setOnAction(e -> {
                    this.selectedQueueId = queueId;
                    selectQueueItem();
                });
                dateBar.getChildren().add(dateButton);
                i++;
            }
        }
        dateBar.getChildren().add(0, this.leftArrowButton);
        dateBar.getChildren().add(this.rightArrowButton);
        dateBar.getChildren().add(this.dateFilter);
        dateBar.getChildren().add(this.statusFilter);
    }

    /**
     * Selects the queue item based on the selected queue ID and populates the form fields with the corresponding data.
     * This method is called when a queue item is selected from the top bar.
     */
    private void selectQueueItem() {
        try {
            // Query to get the reviewpaper details based on the selected queue item
            String query = "SELECT rp.* FROM reviewpapers rp INNER JOIN reviewqueue rq ON rp.paper_id = rq.paper_id WHERE rq.queue_id = " + this.selectedQueueId;

            ResultSet rs = db.executeQuery(query);
            if (rs.next()) {
                // Populate the form fields with the retrieved data
                // paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason
                this.nameField.setText(rs.getString("name"));
                this.addressArea.setText(rs.getString("address"));
                this.ssnField.setText(rs.getString("ssn"));
                this.cellField.setText(rs.getString("cell"));
                this.emailField.setText(rs.getString("email"));
                this.deceasedNameField.setText(rs.getString("deceasedname"));
                this.deceasedDOBField.setText(rs.getString("deceasedDOB"));
                this.deceasedSSNField.setText(rs.getString("deceasedSSN"));
                this.deceasedRelaField.setText(rs.getString("deceasedrela"));
                this.reasonReqArea.setText(rs.getString("reason"));
                this.commentsArea.clear();
            } else {
                System.out.println("No data found for the selected queue item.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Starts the Reviewer Dashboard application.
     * @param primaryStage The primary stage for this application.
     */
    public void startScreen(Stage primaryStage) {
        primaryStage.setTitle("Review Dashboard");

        // Create the top bar with user info and logout button
        HBox topBar = new HBox();
        Label userLabel = new Label("Reviewer: " + this.currentUser + "   ");
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            primaryStage.close(); // Close the dashboard window
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(new Stage()); // Open the login screen in a new window
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, userLabel, logoutButton);
        topBar.setPadding(new Insets(5));

        // Create the date bar with navigation buttons and filters
        HBox dateBar = new HBox(5);
        this.leftArrowButton.setOnAction(e -> {
            updateQueueData(dateBar, true, false);
        });
        this.rightArrowButton.setOnAction(e -> {
            updateQueueData(dateBar, false, true);
        });

        // Date filter
        this.dateFilter.getItems().addAll("Date (ASC)", "Date (DESC)");
        this.dateFilter.setValue("Date (ASC)");
        this.dateFilter.setOnAction(e -> {
            String selectedFilter = this.dateFilter.getValue();
            if (selectedFilter.equals("Date (ASC)")) {
                this.topbarSortDate = "date ASC";
            } else if (selectedFilter.equals("Date (DESC)")) {
                this.topbarSortDate = "date DESC";
            }
            updateQueueData(dateBar, false, false);
        });

        // Status filter
        this.statusFilter.getItems().addAll("Reviewed", "Unreviewed", "All");
        this.statusFilter.setValue("Unreviewed");
        this.statusFilter.setOnAction(e -> {
            String selectedFilter = this.statusFilter.getValue();
            if (selectedFilter.equals("Reviewed")) {
                this.topbarSortStatus = "status = 'reviewed'";
            } else if (selectedFilter.equals("Unreviewed")) {
                this.topbarSortStatus = "status = 'unreviewed'";
            } else if (selectedFilter.equals("All")) {
                this.topbarSortStatus = "1=1"; // no filter
            }
            updateQueueData(dateBar, false, false);
        });

        // Initialize the date bar with the first set of queue data
        updateQueueData(dateBar, false, false);
        dateBar.setPadding(new Insets(5));

        // Form UI elements
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        // Personal information section
        GridPane personalInfo = new GridPane();
        personalInfo.setHgap(10);
        personalInfo.setVgap(10);
        personalInfo.setPadding(new Insets(10));

        personalInfo.add(new Label("Name:"), 0, 0);
        personalInfo.add(this.nameField, 1, 0);

        personalInfo.add(new Label("Address:"), 0, 1);
        this.addressArea.setPrefRowCount(3);
        personalInfo.add(this.addressArea, 1, 1);

        personalInfo.add(new Label("SSN:"), 2, 0);
        personalInfo.add(this.ssnField, 3, 0);

        personalInfo.add(new Label("Cell:"), 2, 1);
        personalInfo.add(this.cellField, 3, 1);

        personalInfo.add(new Label("Email:"), 2, 2);
        personalInfo.add(this.emailField, 3, 2);

        // Deceased information section
        Label deceasedInfoLabel = new Label("Deceased Information");
        deceasedInfoLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        GridPane deceasedInfo = new GridPane();
        deceasedInfo.setHgap(10);
        deceasedInfo.setVgap(10);
        deceasedInfo.setPadding(new Insets(10));

        deceasedInfo.add(new Label("Name of Deceased:"), 0, 0);
        deceasedInfo.add(this.deceasedNameField, 1, 0);

        deceasedInfo.add(new Label("Deceased Date of Birth:"), 2, 0);
        deceasedInfo.add(this.deceasedDOBField, 3, 0);

        deceasedInfo.add(new Label("Deceased SSN:"), 2, 1);
        deceasedInfo.add(this.deceasedSSNField, 3, 1);

        deceasedInfo.add(new Label("Relationship to Requestor:"), 2, 2);
        deceasedInfo.add(this.deceasedRelaField, 3, 2);

        // Reason for request section
        Label reasonForRequestLabel = new Label("Reason for Request");
        reasonForRequestLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");
        GridPane reasonForRequest = new GridPane();
        reasonForRequest.setHgap(10);
        reasonForRequest.setVgap(10);
        reasonForRequest.setPadding(new Insets(10));
        reasonForRequest.add(new Label("Request Reason:"), 0, 0);
        this.reasonReqArea.setPrefRowCount(3);
        reasonForRequest.add(this.reasonReqArea, 1, 1);

        // Action buttons and comments section
        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10));

        // Comments section
        VBox commentsBox = new VBox(5);
        Label commentsLabel = new Label("Comments:");
        this.commentsArea.setPrefRowCount(2);
        commentsBox.getChildren().addAll(commentsLabel, this.commentsArea);
        HBox bottomSection = new HBox(20, actions, commentsBox);
        bottomSection.setPadding(new Insets(10));

        // Approve button remains disabled until all checkmarks are completed
        Button approveButton = new Button("Forward to Approval");
        approveButton.setDisable(true);
        approveButton.setOnAction(e -> {
            // implement later
        });

        // Ensure a comment has been issues before rejecting
        Button rejectButton = new Button("Request Resubmission");
        rejectButton.setOnAction(e -> {
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            if (commentsArea.getText().isEmpty()) {
                // Show alert if comments area is empty
                failAlert.setTitle("Comment Required");
                failAlert.setHeaderText("Comment Required");
                failAlert.setContentText("Please provide a comment before rejecting.");
                failAlert.showAndWait();
            } else {
                try {
                    // Update the selected queue item to have a status of 'rejected'
                    String updateQuery = "UPDATE reviewqueue SET status = 'rejected' WHERE queue_id = " + this.selectedQueueId;
                    db.executeUpdate(updateQuery);

                    // Insert a row into the rejectedpapers table with the selected queue item and comment
                    String query = "INSERT INTO rejectedpapers (queue_id, comment) VALUES (?, ?)";
                    db.executePUpdate(query, this.selectedQueueId, commentsArea.getText());

                    // Show success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Rejection Successful");
                    successAlert.setHeaderText("Rejection Successful");
                    successAlert.setContentText("The paper has been marked as rejected and the comment has been recorded.");
                    successAlert.showAndWait();

                    // Refresh the queue data
                    updateQueueData(dateBar, false, false);
                } catch (Exception ex) {
                    System.out.println("Error: " +ex.getMessage());
                }
            }
        });

        actions.getChildren().addAll(approveButton, rejectButton);

        // Review status label
        Label reviewStatus = new Label("Please review each item carefully.");
        reviewStatus.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0; -fx-font-size: 14px;");

        // Layout the components in a VBox
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, dateBar, personalInfoLabel, personalInfo, deceasedInfoLabel, deceasedInfo, reasonForRequestLabel, reasonForRequest, bottomSection, reviewStatus);
        mainLayout.setPadding(new Insets(10));

        // Set the scene and show the stage
        Scene scene = new Scene(mainLayout);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}