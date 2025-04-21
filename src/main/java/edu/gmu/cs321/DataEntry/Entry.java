package edu.gmu.cs321.DataEntry;

import java.sql.ResultSet;
import edu.gmu.cs321.DatabaseQuery;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

// This class represents the main entry form for data entry, allowing users to input and manage data related to deceased individuals.
public class Entry extends Application {
    String requestID;
    String requestorID = null;
    String deceasedID = null;
    String requestorName;
    String requestorAddress = null;
    String requestorCity = null;
    String requestorState = null;
    String requestorZip = null;
    String requestorCitizenship;
    String requestorSSN = null;
    String requestorPhone = null;
    String requestorEmail = null;
    String relationship = null;
    String deceasedName;
    String deceasedDOB = null;
    String deceasedSSN = null;
    String requestStatus;
    String submissionDate;
    Boolean isLegible = true;
    String requestReason = null;
    String rejectionReason;

    /**
     * Constructor to initialize the Entry object with the username and role of the user.
     * 
     * @param requestID The ID of the request
     * @param requestorName The name of the requestor
     * @param requestorCitizenship The citizenship of the requestor
     * @param deceasedName The name of the deceased
     * @param requestStatus The status of the request
     * @param submissionDate The date of submission
     */
    public Entry(String requestID, String requestorName, String requestorCitizenship, String deceasedName, String requestStatus, String submissionDate, String rejectionReason) {
        this.requestID = requestID;
        this.requestorName = requestorName;
        this.requestorCitizenship = requestorCitizenship;
        this.deceasedName = deceasedName;
        this.requestStatus = requestStatus;
        this.submissionDate = submissionDate;
        this.rejectionReason = rejectionReason;    
    }

    /**
     * Method to start the JavaFX application and set up the main entry form UI.
     * 
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Entry");
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

        //Connect to the database and obtain necessary information
        DatabaseQuery db = new DatabaseQuery();
        try {
            db.connect();
        } catch (Exception e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }

        try {
            String getRequestor = "SELECT * FROM requestors WHERE requestorName = '" + this.requestorName + "'";
            ResultSet rs = db.executeQuery(getRequestor);
            if (rs.next()) {
                this.requestorID = rs.getString("requestorID");
                this.requestorAddress = rs.getString("requestorAddress");
                this.requestorCity = rs.getString("requestorCity");
                this.requestorState = rs.getString("requestorState");
                this.requestorZip = rs.getString("requestorZip");
                this.requestorCitizenship = rs.getString("requestorCitizenship");
                this.requestorSSN = rs.getString("requestorSSN");
                this.requestorPhone = rs.getString("requestorCell");
                this.requestorEmail = rs.getString("requestorEmail");
            }
            String getDeceased = "SELECT * FROM deceased WHERE deceasedName = '" + this.deceasedName + "'";
            ResultSet rs2 = db.executeQuery(getDeceased);
            if (rs2.next()) {
                this.deceasedID = rs2.getString("deceasedID");
                this.deceasedDOB = rs2.getString("deceasedDOB");
                this.deceasedSSN = rs2.getString("deceasedSSN");
            }
            String getRequest = "SELECT * FROM dataqueue WHERE requestID = '" + this.requestID + "'";
            ResultSet rs3 = db.executeQuery(getRequest);
            if (rs3.next()) {
                this.relationship = rs3.getString("relationship");
                this.requestStatus = rs3.getString("requestStatus");
                this.requestReason = rs3.getString("reason");
                this.isLegible = rs3.getBoolean("isLegible");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving data from the database: " + e.getMessage());
        }
        
        ColumnConstraints leftLabelSizes = new ColumnConstraints();
        leftLabelSizes.setPercentWidth(10);
        ColumnConstraints leftDeceased = new ColumnConstraints();
        leftDeceased.setPercentWidth(5);
        ColumnConstraints leftFields = new ColumnConstraints();
        leftFields.setPercentWidth(35);
        ColumnConstraints rightLabelSizes = new ColumnConstraints();
        rightLabelSizes.setPercentWidth(15);
        ColumnConstraints rightDeceased = new ColumnConstraints();
        rightDeceased.setPercentWidth(5);
        ColumnConstraints endConstraints = new ColumnConstraints();
        endConstraints.setPercentWidth(30);
        grid.getColumnConstraints().addAll(leftLabelSizes, leftDeceased, leftFields, rightLabelSizes, rightDeceased, endConstraints);

        //record ID
        Shape rectangle = new Rectangle(500, 50);
        rectangle.setFill(Color.LIGHTGRAY);
        grid.add(rectangle, 0, 0, 6, 1);
        Text recordID = new Text(" [Record ID] " + requestID);
        recordID.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        grid.add(recordID, 0, 0, 4, 1);

        //save, reject, and submit buttons
        Button saveButton = new Button("Save");
        Button submitButton = new Button("Submit");
        Button rejectButton = new Button("Reject");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_RIGHT);
        hbBtn.getChildren().addAll(rejectButton, saveButton, submitButton);
        grid.add(hbBtn, 4, 0, 2, 1);

        //name
        Label nameLabel = new Label("Name:");
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        nameTextField.setText(requestorName);
        grid.add(nameTextField, 1, 1, 2, 1);

        //address
        Label addressLabel = new Label("Address:");
        addressLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(addressLabel, 0, 2);
        TextField addressFieldOne = new TextField();
        addressFieldOne.setPromptText("Address Line 1");
        addressFieldOne.setText(this.requestorAddress);
        grid.add(addressFieldOne, 1, 2, 2, 1);
        TextField addressFieldTwo = new TextField();
        addressFieldTwo.setPromptText("Address Line 2");
        
        grid.add(addressFieldTwo, 1, 3, 2, 1);
        TextField cityField = new TextField();
        cityField.setPromptText("City");
        cityField.setText(this.requestorCity);
        grid.add(cityField, 1, 4, 2, 1);
        TextField stateField = new TextField();
        stateField.setPromptText("State");
        stateField.setText(this.requestorState);
        grid.add(stateField, 1, 5, 2, 1);
        TextField zipField = new TextField();
        zipField.setPromptText("Zip Code");
        zipField.setText(this.requestorZip);
        grid.add(zipField, 1, 6, 2, 1);

        //ssn
        Label ssnLabel = new Label("SSN:");
        ssnLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(ssnLabel, 3, 1);
        TextField ssnField = new TextField();
        ssnField.setPromptText("xxx-xx-xxxx");
        ssnField.setText(this.requestorSSN);
        grid.add(ssnField, 4, 1, 2, 1);

        //phone number
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(phoneLabel, 3, 2);
        TextField phoneField = new TextField();
        phoneField.setPromptText("xxx-xxx-xxxx");
        phoneField.setText(this.requestorPhone);
        grid.add(phoneField, 4, 2, 2, 1);

        //email
        Label emailLabel = new Label("Email:");
        emailLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(emailLabel, 3, 3);
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setText(this.requestorEmail);
        grid.add(emailField, 4, 3, 2, 1);

        //deceased name
        Label deceasedNameLabel = new Label("Name of Deceased:");
        deceasedNameLabel.setAlignment(Pos.CENTER_RIGHT);
        deceasedNameLabel.setWrapText(true);
        grid.add(deceasedNameLabel, 0, 8, 2, 1);
        TextField deceasedNameField = new TextField();
        deceasedNameField.setText(this.deceasedName);
        deceasedNameField.setPromptText("Deceased Name");
        grid.add(deceasedNameField, 2, 8, 1, 1);

        //deceased dob
        Label deceasedDOB = new Label("Deceased Date of Birth:");
        deceasedDOB.setAlignment(Pos.CENTER_RIGHT);
        deceasedDOB.setWrapText(true);
        grid.add(deceasedDOB, 0, 9, 2, 1);
        TextField deceasedDOBField = new TextField();
        deceasedDOBField.setPromptText("MM/DD/YYYY");
        deceasedDOBField.setText(this.deceasedDOB);
        grid.add(deceasedDOBField, 2, 9, 1, 1);

        //deceased ssn
        Label deceasedSSN = new Label("Deceased SSN:");
        deceasedSSN.setAlignment(Pos.CENTER_RIGHT);
        grid.add(deceasedSSN, 0, 10, 2, 1);
        TextField deceasedSSNField = new TextField();
        deceasedSSNField.setPromptText("xxx-xx-xxxx");
        deceasedSSNField.setText(this.deceasedSSN);
        grid.add(deceasedSSNField, 2, 10, 1, 1);

        //relationship to requestor
        Label relationshipLabel = new Label("Relationship to Requestor:");
        relationshipLabel.setAlignment(Pos.CENTER_RIGHT);
        relationshipLabel.setWrapText(true);
        grid.add(relationshipLabel, 3, 8, 2, 1);
        TextField relationshipField = new TextField();
        relationshipField.setText(this.relationship);
        relationshipField.setPromptText("Relationship to Deceased");
        grid.add(relationshipField, 5, 8, 1, 1);

        //request reason
        Label requestReasonLabel = new Label("Request Reason:");
        requestReasonLabel.setAlignment(Pos.TOP_RIGHT);
        grid.add(requestReasonLabel, 0, 11, 2, 1);
        TextField requestReasonField = new TextField();
        requestReasonField.setPrefHeight(100);
        requestReasonField.setAlignment(Pos.TOP_LEFT);
        requestReasonField.setPromptText("Request Reason");
        requestReasonField.setText(this.requestReason);
        grid.add(requestReasonField, 2, 11, 4, 1);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 3, 0, 1, 1);
        actionTarget.setId("actiontarget");
        actionTarget.setFill(Color.RED);

        Shape notesRectangle = new Rectangle(100, 200);
        notesRectangle.setFill(Color.LIGHTGRAY);
        VBox notes = new VBox(10);
        notes.setAlignment(Pos.CENTER_LEFT);
        Label notesLabel = new Label("Notes:");
        notesLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        notesLabel.setAlignment(Pos.CENTER_LEFT);
        Label citizenshipLabel = new Label("");
        if (!this.requestorCitizenship.equals("citizen")) {
            citizenshipLabel.setText(this.requestorName + " is not a US Citizen.");
        }
        Label legibleLabel = new Label("");
        if (this.isLegible == false) {
            legibleLabel.setText(this.requestorName + "'s request is not legible.");
        }
        notes.getChildren().addAll(notesLabel, citizenshipLabel, legibleLabel);
        grid.add(legibleLabel, 3, 4, 3, 3);
        grid.add(notes, 3, 4, 3, 3);

        //reject button action
        rejectButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reject Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Please provide a reason for rejection:");
            ComboBox<String> rejectionReasonComboBox = new ComboBox<>();
            rejectionReasonComboBox.getItems().addAll("Request is Illegible", "Requestor is NOT a U.S. Citizen", "Other");
            rejectionReasonComboBox.setPromptText("Select Rejection Reason");
            if (!this.requestorCitizenship.equals("citizen")) {
                rejectionReasonComboBox.setValue("Requestor is NOT a U.S. Citizen");
            }
            if (this.isLegible == false) {
                rejectionReasonComboBox.setValue("Request is Illegible");
            }
            TextField rejectionReasonField = new TextField();
            rejectionReasonField.setPrefSize(250, 170);
            rejectionReasonField.setAlignment(Pos.TOP_LEFT);
            rejectionReasonField.setVisible(false); 
            rejectionReasonComboBox.setOnAction(event -> {
                String selectedReason = rejectionReasonComboBox.getValue();
                if (selectedReason == "Other") {
                    rejectionReasonField.setVisible(true);
                    rejectionReasonField.setPromptText("Please specify the reason for rejection.");
                } else {
                    rejectionReasonField.setVisible(false);
                }
            });

            VBox rejectionInfo = new VBox(10);
            rejectionInfo.getChildren().addAll(rejectionReasonComboBox, rejectionReasonField);
            alert.getDialogPane().setContent(rejectionInfo);
            alert.getDialogPane().setMinHeight(100);
            alert.getDialogPane().setMinWidth(300);
            alert.getDialogPane().setPrefSize(300, 200);
            
            alert.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    this.requestStatus = "Rejected";
                    // User clicked OK, proceed with rejection
                    if (rejectionReasonComboBox.getValue().equals("Other")) {
                        if (rejectionReasonField.getText() == null) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Please provide a reason for rejection.");
                            errorAlert.showAndWait();
                        } else {
                            rejectionReason = rejectionReasonField.getText();
                            try {
                                String updateRequests = "UPDATE dataqueue SET requestStatus = 'Rejected', rejectionReason = '" + rejectionReason + "' WHERE requestID = '" + this.requestID + "'";
                                db.executeUpdate(updateRequests);
                            } catch (Exception ex) {
                                System.out.println("Error updating the database: " + ex.getMessage());
                            }
                            actionTarget.setText("Request rejected successfully!");
                            primaryStage.close();
                        }
                    } else {
                        rejectionReason = rejectionReasonComboBox.getValue();
                        try {
                            String updateRequests = "UPDATE dataqueue SET requestStatus = 'Rejected', rejectionReason = '" + this.rejectionReason + "' WHERE requestID = '" + this.requestID + "'";
                            db.executeUpdate(updateRequests);
                        } catch (Exception ex) {
                            System.out.println("Error updating the database: " + ex.getMessage());
                        }
                        actionTarget.setText("Request rejected successfully!");
                        primaryStage.close();
                    }
                } else if (response == ButtonType.CANCEL) {
                    // User clicked Cancel or closed the dialog, do nothing
                    actionTarget.setText("Rejection canceled.");
                } else {
                    // User clicked Cancel or closed the dialog, do nothing
                    actionTarget.setText("Rejection canceled.");
                }
            });
        });

        //save button action
        saveButton.setOnAction(e -> {
            requestorName = nameTextField.getText();
            requestorAddress = addressFieldOne.getText() + " " + addressFieldTwo.getText();
            requestorCity = cityField.getText();
            requestorState = stateField.getText();
            requestorZip = zipField.getText();
            requestorSSN = ssnField.getText();
            requestorPhone = phoneField.getText();
            requestorEmail = emailField.getText();
            deceasedName = deceasedNameField.getText();
            this.deceasedDOB = deceasedDOBField.getText();
            this.deceasedSSN = deceasedSSNField.getText();
            relationship = relationshipField.getText();
            requestReason = requestReasonField.getText();

            try {
                // Update the database with the new values
                String updateRequestor = "UPDATE requestors SET requestorName = '" + requestorName + "', requestorAddress = '" + requestorAddress + "', requestorCity = '" + requestorCity + "', requestorState = '" + requestorState + "', requestorZip = '" + requestorZip + "', requestorSSN = '" + requestorSSN + "', requestorCell = '" + requestorPhone + "', requestorEmail = '" + requestorEmail + "' WHERE requestorID = '" + this.requestorID + "'";
                db.executeUpdate(updateRequestor);

                String updateDeceased = "UPDATE deceased SET deceasedName = '" + deceasedName + "', deceasedDOB = '" + this.deceasedDOB + "', deceasedSSN = '" + this.deceasedSSN + "' WHERE deceasedID = '" + this.deceasedID + "'";
                db.executeUpdate(updateDeceased);

                String updateRequests = "UPDATE dataqueue SET relationship = '" + relationshipField.getText() + "', requestorName = '" + requestorName + "', deceasedName = '" + this.deceasedName + "', reason = '" + requestReason + "' WHERE requestID = '" + this.requestID +"'";
                db.executeUpdate(updateRequests);
                
            } catch (Exception ex) {
                System.out.println("Error updating the database: " + ex.getMessage());
            }
            actionTarget.setText("Saved successfully!");
        });

        //submit button action
        submitButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Submission Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to submit this request for review?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User clicked OK, proceed with submission
                    requestorName = nameTextField.getText();
                    requestorAddress = addressFieldOne.getText() + " " + addressFieldTwo.getText();
                    requestorCity = cityField.getText();
                    requestorState = stateField.getText();
                    requestorZip = zipField.getText();
                    requestorSSN = ssnField.getText();
                    requestorPhone = phoneField.getText();
                    requestorEmail = emailField.getText();
                    deceasedName = deceasedNameField.getText();
                    this.deceasedDOB = deceasedDOBField.getText();
                    this.deceasedSSN = deceasedSSNField.getText();
                    relationship = relationshipField.getText();
                    requestReason = requestReasonField.getText();
                    this.requestStatus = "Pending Review";
                    
                    try {
                        // Update the database with the new values
                        String updateRequestor = "UPDATE requestors SET requestorName = '" + requestorName + "', requestorAddress = '" + requestorAddress + "', requestorCity = '" + requestorCity + "', requestorState = '" + requestorState + "', requestorZip = '" + requestorZip + "', requestorSSN = '" + requestorSSN + "', requestorCell = '" + requestorPhone + "', requestorEmail = '" + requestorEmail + "' WHERE requestorID = '" + this.requestorID + "'";
                        db.executeUpdate(updateRequestor);

                        String updateDeceased = "UPDATE deceased SET deceasedName = '" + deceasedName + "', deceasedDOB = '" + this.deceasedDOB + "', deceasedSSN = '" + this.deceasedSSN + "' WHERE deceasedID = '" + this.deceasedID + "'";
                        db.executeUpdate(updateDeceased);

                        String updateRequests = "UPDATE dataqueue SET relationship = '" + relationshipField.getText() + "', requestorName = '" + requestorName + "', deceasedName = '" + this.deceasedName + "', reason = '" + requestReason + "', requestStatus = '" + this.requestStatus + "' WHERE requestID = '" + this.requestID + "'";
                        db.executeUpdate(updateRequests);

                    } catch (Exception ex) {
                        System.out.println("Error updating the database: " + ex.getMessage());
                    }
                    actionTarget.setText("Submitted successfully!");
                    primaryStage.close();
                } else {
                    // User clicked Cancel or closed the dialog, do nothing
                    actionTarget.setText("Submission pending.");
                }
            });
            
        });

        Scene scene = new Scene(grid, 800, 570);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnHiding(event -> {
            try {
                db.close(); // Ensure the database connection is closed
            } catch (Exception e) {
                System.out.println("Error closing the database connection: " + e.getMessage());
            }
        });
    }
    
    /**
     * Method to get the request ID.
     * 
     * @return the request ID
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Method to get the requestor name.
     * 
     * @return the requestor name
     */
    public String getRequestorName() {
        return requestorName;
    }

    /**
     * Method to get the requestor citizenship.
     * 
     * @return the requestor citizenship
     */
    public String getRequestorCitizenship() {
        return requestorCitizenship;
    }

    /**
     * Method to get the deceased name.
     * 
     * @return the deceased name
     */
    public String getDeceasedName() {
        return deceasedName;
    }

    /**
     * Method to get the request status.
     * 
     * @return the request status
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * Method to get the submission date.
     * 
     * @return the submission date
     */
    public String getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Method to get the rejection reason.
     * 
     * @return the rejection reason
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public String toString() {
        return "[Request " + requestID + "] "+ requestorName +" (" + requestStatus + ") - " + submissionDate;
    }

    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
