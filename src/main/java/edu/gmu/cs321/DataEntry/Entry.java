package edu.gmu.cs321.DataEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.gmu.cs321.DatabaseQuery;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
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
    public Entry(String requestID, String requestorName, String requestorCitizenship, String deceasedName, String requestStatus, String submissionDate) {
        this.requestID = requestID;
        this.requestorName = requestorName;
        this.requestorCitizenship = requestorCitizenship;
        this.deceasedName = deceasedName;
        this.requestStatus = requestStatus;
        this.submissionDate = submissionDate;
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
        
        // Fetch requestor and deceased information from the database
    try {
        String requestQuery = "SELECT * FROM dataqueue WHERE requestID = '" + requestID + "'";
        ResultSet request = db.executeQuery(requestQuery);
        if (request.next()) {
            this.relationship = request.getString("relationship");
            this.requestReason = request.getString("reason");
        }
        String requestorQuery = "SELECT * FROM requestors WHERE requestorName = '" + requestorName + "'";
        ResultSet requestor = db.executeQuery(requestorQuery);
        if (requestor.next()) {
            this.requestorID = requestor.getString("requestorID");
            this.requestorAddress = requestor.getString("requestorAddress");
            this.requestorCity = requestor.getString("requestorCity");
            this.requestorState = requestor.getString("requestorState");
            this.requestorZip = requestor.getString("requestorZip");
            this.requestorSSN = requestor.getString("requestorSSN");
            this.requestorPhone = requestor.getString("requestorCell");
            this.requestorEmail = requestor.getString("requestorEmail");
        }
        String deceasedQuery = "SELECT * FROM deceased WHERE deceasedName = '" + deceasedName + "'";
        ResultSet deceased = db.executeQuery(deceasedQuery);
        if (deceased.next()) {
            this.deceasedID = deceased.getString("deceasedID");
            this.deceasedDOB = deceased.getString("deceasedDOB");
            this.deceasedSSN = deceased.getString("deceasedSSN");
        }
    } catch (Exception e) {
        System.out.println("Error connecting to the database: " + e.getMessage());
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
        Shape rectangle = new Rectangle(600, 50);
        rectangle.setFill(Color.LIGHTGRAY);
        grid.add(rectangle, 0, 0, 6, 1);
        Text recordID = new Text(" [Record ID] " + requestID);
        recordID.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        grid.add(recordID, 0, 0, 4, 1);

        //save and submit buttons
        Button saveButton = new Button("Save");
        Button submitButton = new Button("Submit");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_RIGHT);
        hbBtn.getChildren().addAll(saveButton, submitButton);
        grid.add(hbBtn, 5, 0, 1, 1);

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
        grid.add(actionTarget, 4, 0, 1, 1);
        actionTarget.setId("actiontarget");
        actionTarget.setFill(Color.RED);

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
            primaryStage.close();
        });

        Scene scene = new Scene(grid, 800, 570);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    @Override
    public String toString() {
        return "[Request " + requestID + "] "+ requestorName +" (" + requestStatus + "}";
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
