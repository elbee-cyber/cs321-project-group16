package edu.gmu.cs321.Reviewer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cs321.Workflow;

import edu.gmu.cs321.DatabaseQuery;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
    // Database connection
    private DatabaseQuery db;

    // Workflow instance
    private Workflow workflow;

    // Current user data + DB conn (passed from login)
    private Integer currentUserId;
    private String currentUserRole;
    private String currentUser;

    // Queue items currently displayed in top bar
    private List<Integer> queue_ids;
    private int queueLBound = 0;
    private int queueHBound = 5;

    // Selected top bar item
    private Integer selectedFormID = 0;

    // Used to record indexes for arrow navigation
    private Integer leftMostItem = 0;
    private Integer rightMostItem = -1;

    // Selected top bar item sort
    private String topbarSortDate = "created_at DESC";

    // Topbar buttons
    private Button leftArrowButton = new Button("<");
    private Button rightArrowButton = new Button(">");
    private ComboBox<String> dateFilter = new ComboBox<>();

    // Form view items
    private TextField nameField = new TextField("Placeholder");
    private TextArea addressArea = new TextArea("Placeholder");
    private TextField ssnField = new TextField("Placeholder");
    private TextField cellField = new TextField("Placeholder");
    private TextField emailField = new TextField("Placeholder");
    private TextField deceasedNameField = new TextField("Placeholder");
    private TextField deceasedDOBField = new TextField("Placeholder");
    private TextField deceasedSSNField = new TextField("Placeholder");
    private TextField deceasedRelaField = new TextField("Placeholder");
    private TextArea reasonReqArea = new TextArea("Placeholder");

    // Global UI elements
    private TextArea commentsArea = new TextArea();
    private Button approveButton = new Button("Forward to Approval");
    Label paperidLabel = new Label();

    // Review checkboxes for each field
    private CheckBox nameCheckBox = new CheckBox();
    private CheckBox addressCheckBox = new CheckBox();
    private CheckBox ssnCheckBox = new CheckBox();
    private CheckBox cellCheckBox = new CheckBox();
    private CheckBox emailCheckBox = new CheckBox();
    private CheckBox deceasedNameCheckBox = new CheckBox();
    private CheckBox deceasedDOBCheckBox = new CheckBox();
    private CheckBox deceasedSSNCheckBox = new CheckBox();
    private CheckBox deceasedRelaCheckBox = new CheckBox();
    private CheckBox reasonReqCheckBox = new CheckBox();

    // Keeps track of number of checked items
    private int checkedCount = 0;

    /**
     * Constructor for the Dashboard class.
     */
    public Dashboard(Integer currentUserId, String currentUserRole, String currentUser, DatabaseQuery db){
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
        this.currentUser = currentUser;
        this.db = db;
        this.workflow = db.getWorkflow();
        this.queue_ids = new ArrayList<>();
    }

    /**
     * Gets the queue data from the database based on the current filters and navigation buttons.
     * @param clickedLeft  Indicates if the left arrow button was clicked.
     * @param clickedRight Indicates if the right arrow button was clicked.
     * @return An array of strings representing the queue data (dates).
     */
    private String[] getQueueData(Boolean clickedLeft, Boolean clickedRight) {
        String[] results = new String[5];

        String query = "SELECT form_id, created_at FROM workflow_records WHERE next_step='Review' ORDER BY " + this.topbarSortDate;
        if (clickedLeft) {
            query = "SELECT form_id, created_at FROM workflow_records WHERE next_step='Review' AND form_id < " + this.leftMostItem + " ORDER BY " + this.topbarSortDate;
        } else if (clickedRight) {
            query = "SELECT form_id, created_at FROM workflow_records WHERE next_step='Review' AND form_id > " + this.rightMostItem + " ORDER BY " + this.topbarSortDate;
        }

        // Execute the query and populate the results array
        try {
            ResultSet rs = db.executeQuery(query);

            // Check if the result set is empty
            if (rs == null){
                return getQueueData(false, false);
            }
            this.leftMostItem = this.queue_ids.get(this.queueLBound);
            this.rightMostItem = this.queue_ids.get(this.queueHBound);
            int i = 1;
            while (rs.next()){
                results[i-1] = rs.getTimestamp("created_at").toString();
                this.queue_ids.add(this.workflow.GetNextWFItem("Review"));
                // Set the leftMostItem and rightMostItem based on the current index
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
                Integer queueId = this.queue_ids.get(i);
                dateButton.setOnAction(e -> {
                    this.selectedFormID = queueId;
                    selectQueueItem();
                });
                dateBar.getChildren().add(dateButton);
                i++;
            }
        }
        dateBar.getChildren().add(0, this.leftArrowButton);
        dateBar.getChildren().add(this.rightArrowButton);
        dateBar.getChildren().add(this.dateFilter);
    }

    /**
     * Selects the queue item based on the selected queue ID and populates the form fields with the corresponding data.
     * This method is called when a queue item is selected from the top bar.
     */
    private void selectQueueItem() {
        this.paperidLabel.setText("Reviewing Form ID: " + this.selectedFormID + "           ");
        clearCheckedItems(); // Clear previous selections

        // Update navigation keys if we are resting on a bound
        int minQueueId = getBoundedQueueId(0);
        System.out.println("Min Queue ID: " + minQueueId);
        System.out.println("Left Most Item: " + this.leftMostItem);
        System.out.println("Right Most Item: " + this.rightMostItem);
        System.out.println("Max Queue ID: " + getBoundedQueueId(1));
        if (this.leftMostItem == minQueueId) {
            this.leftArrowButton.setDisable(true);
        } else {
            this.leftArrowButton.setDisable(false);
        }
        int maxQueueId = getBoundedQueueId(1) ;
        if (this.rightMostItem == maxQueueId) {
            this.rightArrowButton.setDisable(true);
        } else {
            this.rightArrowButton.setDisable(false);
        }
        try {
            // Query to get the reviewpaper details based on the selected queue item
            String query = "SELECT * FROM requestData WHERE formID=" + this.selectedFormID;

            ResultSet rs = db.executeQuery(query);
            if (rs.next()) {
                // Populate the form fields with the retrieved data
                // paper_id, name, ssn, address, cell, email, deceasedname, deceasedDOB, deceasedSSN, deceasedrela, reason
                this.nameField.setText(rs.getString("requestorName"));
                this.addressArea.setText(rs.getString("requestorAddress"));
                this.ssnField.setText(rs.getString("requestorSSN"));
                this.cellField.setText(rs.getString("requestorCell"));
                this.emailField.setText(rs.getString("requestorEmail"));
                this.deceasedNameField.setText(rs.getString("deceasedName"));
                this.deceasedDOBField.setText(rs.getString("deceasedDOB"));
                this.deceasedSSNField.setText(rs.getString("deceasedSSN"));
                this.deceasedRelaField.setText(rs.getString("deceasedRelationship"));
                this.reasonReqArea.setText(rs.getString("requestReason"));
                this.commentsArea.clear();
            } else {
                System.out.println("No data found for the selected queue item.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Clears the checked items and resets the form fields.
     */
    private void clearCheckedItems() {
        this.nameCheckBox.setSelected(false);
        this.addressCheckBox.setSelected(false);
        this.ssnCheckBox.setSelected(false);
        this.cellCheckBox.setSelected(false);
        this.emailCheckBox.setSelected(false);
        this.deceasedNameCheckBox.setSelected(false);
        this.deceasedDOBCheckBox.setSelected(false);
        this.deceasedSSNCheckBox.setSelected(false);
        this.deceasedRelaCheckBox.setSelected(false);
        this.reasonReqCheckBox.setSelected(false);
        this.approveButton.setDisable(true);
        this.checkedCount = 0;
        this.nameField.setDisable(false);
        this.addressArea.setDisable(false);
        this.ssnField.setDisable(false);
        this.cellField.setDisable(false);
        this.emailField.setDisable(false);
        this.deceasedNameField.setDisable(false);
        this.deceasedDOBField.setDisable(false);
        this.deceasedSSNField.setDisable(false);
        this.deceasedRelaField.setDisable(false);
        this.reasonReqArea.setDisable(false);
        this.commentsArea.clear();
    }

    
    private int getBoundedQueueId(int isMax) {
        int bound = -1;
        String query;
        if (isMax == 1){
            query = "SELECT MAX(form_id) AS bound FROM workflow_records";
        }else{
            query = "SELECT MIN(form_id) AS bound FROM workflow_records";
        }
        try {
            ResultSet rs = db.executeQuery(query);
            if (rs.next()) {
                bound = rs.getInt("bound");
                if (isMax == 1){
                    bound += 5;
                }else{
                    bound -= 5;
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving max queue_id: " + e.getMessage());
        }
        return bound;
    }

    /**
     * Starts the Reviewer Dashboard application.
     * @param primaryStage The primary stage for this application.
     */
    public void startScreen(Stage primaryStage) {
        primaryStage.setTitle("Review Dashboard");

        // Create the top bar with user+paper info and logout button
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
        topBar.getChildren().addAll(spacer, this.paperidLabel, userLabel, logoutButton);
        topBar.setPadding(new Insets(5));

        // Create the date bar with navigation buttons and filters
        HBox dateBar = new HBox(5);

        // Left and right arrow buttons for navigation
        this.leftArrowButton.setDisable(true); // Disable left arrow initially
        this.leftArrowButton.setOnAction(e -> {
            updateQueueData(dateBar, true, false);
        });
        this.rightArrowButton.setOnAction(e -> {
            updateQueueData(dateBar, false, true);
        });

        // Date filter
        this.dateFilter.getItems().addAll("Date (DESC)", "Date (ASC)");
        this.dateFilter.setValue("Date (DESC)");
        this.dateFilter.setOnAction(e -> {
            String selectedFilter = this.dateFilter.getValue();
            if (selectedFilter.equals("Date (ASC)")) {
                this.topbarSortDate = "date ASC";
            } else if (selectedFilter.equals("Date (DESC)")) {
                this.topbarSortDate = "date DESC";
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

        // Personal information section with checkboxes
        personalInfo.add(new Label("Name:"), 0, 0);
        personalInfo.add(this.nameField, 1, 0);
        personalInfo.add(this.nameCheckBox, 2, 0);
        this.nameCheckBox.setOnAction(e -> {
            if (this.nameCheckBox.isSelected()) {
                this.nameField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.nameField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        personalInfo.add(new Label("Address:"), 0, 1);
        this.addressArea.setPrefRowCount(3);
        personalInfo.add(this.addressArea, 1, 1);
        personalInfo.add(this.addressCheckBox, 2, 1);
        this.addressCheckBox.setOnAction(e -> {
            if (this.addressCheckBox.isSelected()) {
                this.addressArea.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.addressArea.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        personalInfo.add(new Label("SSN:"), 3, 0);
        personalInfo.add(this.ssnField, 4, 0);
        personalInfo.add(this.ssnCheckBox, 5, 0);
        this.ssnCheckBox.setOnAction(e -> {
            if (this.ssnCheckBox.isSelected()) {
                this.ssnField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.ssnField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        personalInfo.add(new Label("Cell:"), 3, 1);
        personalInfo.add(this.cellField, 4, 1);
        personalInfo.add(this.cellCheckBox, 5, 1);
        this.cellCheckBox.setOnAction(e -> {
            if (this.cellCheckBox.isSelected()) {
                this.cellField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.cellField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        personalInfo.add(new Label("Email:"), 3, 2);
        personalInfo.add(this.emailField, 4, 2);
        personalInfo.add(this.emailCheckBox, 5, 2);
        this.emailCheckBox.setOnAction(e -> {
            if (this.emailCheckBox.isSelected()) {
                this.emailField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.emailField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        // Deceased information section with checkboxes
        Label deceasedInfoLabel = new Label("Deceased Information");
        deceasedInfoLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        GridPane deceasedInfo = new GridPane();
        deceasedInfo.setHgap(10);
        deceasedInfo.setVgap(10);
        deceasedInfo.setPadding(new Insets(10));

        deceasedInfo.add(new Label("Name of Deceased:"), 0, 0);
        deceasedInfo.add(this.deceasedNameField, 1, 0);
        deceasedInfo.add(this.deceasedNameCheckBox, 2, 0);
        this.deceasedNameCheckBox.setOnAction(e -> {
            if (this.deceasedNameCheckBox.isSelected()) {
                this.deceasedNameField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.deceasedNameField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        deceasedInfo.add(new Label("Deceased Date of Birth:"), 3, 0);
        deceasedInfo.add(this.deceasedDOBField, 4, 0);
        deceasedInfo.add(this.deceasedDOBCheckBox, 5, 0);
        this.deceasedDOBCheckBox.setOnAction(e -> {
            if (this.deceasedDOBCheckBox.isSelected()) {
                this.deceasedDOBField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.deceasedDOBField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        deceasedInfo.add(new Label("Deceased SSN:"), 3, 1);
        deceasedInfo.add(this.deceasedSSNField, 4, 1);
        deceasedInfo.add(this.deceasedSSNCheckBox, 5, 1);
        this.deceasedSSNCheckBox.setOnAction(e -> {
            if (this.deceasedSSNCheckBox.isSelected()) {
                this.deceasedSSNField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.deceasedSSNField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        deceasedInfo.add(new Label("Relationship to Requestor:"), 3, 2);
        deceasedInfo.add(this.deceasedRelaField, 4, 2);
        deceasedInfo.add(this.deceasedRelaCheckBox, 5, 2);
        this.deceasedRelaCheckBox.setOnAction(e -> {
            if (this.deceasedRelaCheckBox.isSelected()) {
                this.deceasedRelaField.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.deceasedRelaField.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

        // Reason for request section with checkboxes
        Label reasonForRequestLabel = new Label("Reason for Request");
        reasonForRequestLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");
        GridPane reasonForRequest = new GridPane();
        reasonForRequest.setHgap(10);
        reasonForRequest.setVgap(10);
        reasonForRequest.setPadding(new Insets(10));
        reasonForRequest.add(new Label("Request Reason:"), 0, 0);
        this.reasonReqArea.setPrefRowCount(3);
        reasonForRequest.add(this.reasonReqArea, 1, 1);
        reasonForRequest.add(this.reasonReqCheckBox, 2, 1);
        this.reasonReqCheckBox.setOnAction(e -> {
            if (this.reasonReqCheckBox.isSelected()) {
                this.reasonReqArea.setDisable(true);
                this.checkedCount++;
                if (this.checkedCount == 10){
                    approveButton.setDisable(false);
                }
            } else {
                this.reasonReqArea.setDisable(false);
                this.checkedCount--;
                approveButton.setDisable(true);
            }
        });

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
        this.approveButton.setDisable(true);
        this.approveButton.setOnAction(e -> {
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
                    // Send back to DataEntry for resubmission
                    this.workflow.AddWFItem(this.selectedFormID,"DataEntry");

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
        this.approveButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Forward to Approval");
            confirmAlert.setContentText("Are you sure you want to forward this paper for approval?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Update the selected queue item to have a status of 'Forwarded'
                        this.workflow.AddWFItem(this.selectedFormID,"Approve");

                        // Show success alert
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Approval Successful");
                        successAlert.setHeaderText("Approval Successful");
                        successAlert.setContentText("The paper has been forwarded for approval.");
                        successAlert.showAndWait();

                        // Refresh the queue data
                        updateQueueData(dateBar, false, false);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
            });
        });

        actions.getChildren().addAll(approveButton, rejectButton);

        // Review status label
        Label reviewStatus = new Label("All items must be corrected and/or marked as reviewed before forwarding.");
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