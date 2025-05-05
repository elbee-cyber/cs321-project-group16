package edu.gmu.cs321.Approver;

import edu.gmu.cs321.DatabaseQuery;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import com.cs321.Workflow;
import java.util.Collections;

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
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.Map;
import java.util.LinkedHashMap;
import javafx.scene.control.ListView;

/**
 * This class represents the user interface for the Approver Dashboard in the immigration request system.
 * The approver is able to view requests, approve, reject, or send them back, with the ability to enter comments for rejection or sending back.
 * This class extends Application and is responsible for initializing and displaying the dashboard layout, 
 * managing request records, and interacting with the approver through buttons and form fields.
 *
 *
 * @param currentUserRole The role of the currently logged-in user (e.g., "Approver").
 * @param currentUser     The name of the currently logged-in user.
 * @param dbQuery         The database query object used for executing queries.
 */
public class ApproverDashboard {
    
    private String currentUserRole;
    private String currentUser;
    private DatabaseQuery dbQuery;
    private Workflow workflow;

    // Queue items currently displayed in top bar
    private HashMap<Integer, String> form_item;
    private ArrayList<Integer> form_ids;

    // Selected top bar item
    private Integer selectedQueueId = 0;



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
    private Button approveButton = new Button("Approve");
    Label paperidLabel = new Label();
    ListView<String> formListView = new ListView<>();


    /**
     * Constructor for the ApproverDashboard class.
     */
    public ApproverDashboard(String currentUserRole, String currentUser, DatabaseQuery dbQuery){
        
        this.currentUserRole = currentUserRole;
        this.currentUser = currentUser;
        this.dbQuery = dbQuery;
        this.workflow = dbQuery.getWorkflow();
        this.form_item = new HashMap<Integer, String>();
        this.form_ids = new ArrayList<Integer>();
        
    }

    public Pane getRootPane() {
        VBox root = new VBox(10);
        Label heading = new Label("Approver Dashboard");
    
        updateQueueListView(formListView);
    
        formListView.setOnMouseClicked(e -> {
            String selectedItem = formListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                for (Map.Entry<Integer, String> entry : this.form_item.entrySet()) {
                    if (entry.getValue().equals(selectedItem)) {
                        selectedQueueId = entry.getKey();
                        selectQueueItem();
                        break;
                    }
                }
            }
        });
    
        root.getChildren().addAll(heading, formListView);
        return root;
    }
    
    

    /**
     * Gets the queue data from the database based on the current filters and navigation buttons.
     * @return An array of strings representing the queue data (dates).
     */
    private Map<Integer, String> getQueueDataMap() {
        Map<Integer, String> dataMap = new LinkedHashMap<>();
        String query = "SELECT form_id, created_at FROM workflow_records WHERE next_step='Approve' ORDER BY created_at DESC";
        this.form_ids.clear();
        this.form_item.clear();
    
        try {
            ResultSet rs = dbQuery.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("form_id");
                String displayText = "Form ID: " + id + " | Created: " + rs.getTimestamp("created_at").toString();
                dataMap.put(id, displayText);
                this.form_item.put(id, displayText); // update your internal maps too
                this.form_ids.add(id);
            }
        } catch (Exception e) {
            System.out.println("Error Date set: " + e.getMessage());
        }
    
        return dataMap;
    }
    


    private void updateQueueListView(ListView<String> listView) {
        Map<Integer, String> queueDataMap = getQueueDataMap();
        listView.getItems().setAll(queueDataMap.values());
    }
    
    

    /**
     * Selects the queue item based on the selected queue ID and populates the form fields with the corresponding data.
     * This method is called when a queue item is selected from the top bar.
     */
    private void selectQueueItem() {
        this.paperidLabel.setText("Reviewing Paper ID: " + this.selectedQueueId + "           ");
    
    
        try {
            String query = "SELECT * FROM requestData WHERE formID=" + this.selectedQueueId;
            ResultSet rs = dbQuery.executeQuery(query);
            
            if (rs.next()) {
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
            System.out.println("Error requestData: " + e.getMessage());
        }
    }
    

    

    public void start(Stage primaryStage) {
        // Root layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label header = new Label("Approver Dashboard");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label userLabel = new Label("Approver: " + this.currentUser + "   ");
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            primaryStage.close(); // Close the dashboard window
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(new Stage()); // Open the login screen in a new window
        });

        // ListView to show submitted forms
        //ListView<this.form_item> formListView = new ListView<>();
        formListView.setPrefHeight(400);
        
        // Fetch forms to be reviewed by Approver
        Map<Integer, String> pendingForms = getQueueDataMap(); 
        formListView.getItems().addAll(pendingForms.values());
        formListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                for (Integer key : pendingForms.keySet()) {
                    this.selectedQueueId = key; //get the form ID from the Form object
                    selectQueueItem(); // Load form details into fields
                }
            }
        });
        

        // Buttons and actions
        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Send Back For Review");
        TextField commentField = new TextField();
        commentField.setPromptText("Add rejection comment...");

        HBox actions = new HBox(10, approveButton, rejectButton);
        actions.setAlignment(Pos.CENTER);

        // Handle approval
        this.approveButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Forward to Supervisor");
            confirmAlert.setContentText("Are you sure you want to forward this form to Supervisor?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Update the selected queue item to have a status of 'Forwarded'
                        this.workflow.AddWFItem(this.selectedQueueId, "Approve");
                        this.form_item.entrySet().removeIf(entry -> entry.getKey().equals(this.selectedQueueId));
                        this.form_ids.removeIf(id -> id.equals(this.selectedQueueId));

                        // Update the corresponding request with Approver edits
                        String query = "UPDATE requestData SET requestorName=?, requestorAddress=?, requestorSSN=?, requestorCell=?, requestorEmail=?, deceasedName=?, deceasedDOB=?, deceasedSSN=?, deceasedRelationship=?, requestReason=? WHERE formID=" + this.selectedQueueId.toString();
                        dbQuery.executePUpdate(query, this.nameField.getText(), this.addressArea.getText(), this.ssnField.getText(), this.cellField.getText(), this.emailField.getText(), this.deceasedNameField.getText(), this.deceasedDOBField.getText(), this.deceasedSSNField.getText(), this.deceasedRelaField.getText(), this.reasonReqArea.getText());

        

                        // Show success alert
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Approval Successful");
                        successAlert.setHeaderText("Approval Successful");
                        successAlert.setContentText("The paper has been forwarded to Supervisor.");
                        successAlert.showAndWait();
                    } catch (Exception ex) {
                        System.out.println("Error Approve: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        });

        // Handle rejection
        // Ensure a comment has been issued before rejecting
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
                    // Remove item
                    this.form_item.entrySet().removeIf(entry -> entry.getKey().equals(this.selectedQueueId));
                    this.form_ids.removeIf(id -> id.equals(this.selectedQueueId));


                    // Move form back to Reviewer queue
                    String updateQuery = "UPDATE workflow_records SET next_step = 'Review', updated_at = NOW() WHERE form_id = ?";
                    dbQuery.executePUpdate(updateQuery, this.selectedQueueId); 

                    
                   
                    
                    // Show success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Rejection Successful");
                    successAlert.setHeaderText("Rejection Successful");
                    successAlert.setContentText("The paper has been marked as rejected and sent back to Reviewer for review.");
                    successAlert.showAndWait();
                } catch (Exception ex) {
                    System.out.println("Error Reject: " +ex.getMessage());
                }
            }
        });


        root.getChildren().addAll(header, formListView, commentField, actions);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Approver Dashboard");
        primaryStage.show();
    }

    
    
}