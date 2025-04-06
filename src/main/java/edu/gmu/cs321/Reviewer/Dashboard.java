package edu.gmu.cs321.Reviewer;

import java.sql.ResultSet;

import edu.gmu.cs321.DatabaseQuery;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Dashboard {
    private Integer currentUserId;
    private String currentUserRole;
    private String currentUser;
    private DatabaseQuery db;

    // selected forms
    private Integer[] queue_ids;

    public Dashboard(Integer currentUserId, String currentUserRole, String currentUser, DatabaseQuery db){
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
        this.currentUser = currentUser;
        this.db = db;
    }

    // return ResultSet of the oldest 5 unreviewed forms
    private String[] getQueueData() {
        String[] results = new String[5];
        String query = "SELECT * FROM reviewqueue WHERE userid = ? AND status = 'unreviewed' ORDER BY date ASC LIMIT 5";
        try {
            ResultSet rs = db.executePQuery(query, currentUserId);
            int i = 1;
            while (rs.next()){
                results[i-1] = rs.getDate(i).toLocalDate().toString();
                this.queue_ids[i-1] = rs.getInt("queue_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return results;
    }

    // show selected queue item in form view
    private void selectQueueItem(){
        // implement later
    }

    public void startScreen(Stage primaryStage) {
        
        primaryStage.setTitle("Review Dashboard");

        // top bar with queue display
        HBox topBar = new HBox();
        Label userLabel = new Label("Reviewer: " + currentUser + "   ");

        // logout topbar
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

        // date selection topbar
        HBox dateBar = new HBox(5);
        String[] queueDates = getQueueData();

        for(String date: queueDates) {
            if (date != null) {
                Button dateButton = new Button(date);
                dateButton.setOnAction(e -> {
                    // implement later
                });
                dateBar.getChildren().add(dateButton);
            }
        }
        dateBar.setPadding(new Insets(5));

        // personal information section
        GridPane personalInfo = new GridPane();
        personalInfo.setHgap(10);
        personalInfo.setVgap(10);
        personalInfo.setPadding(new Insets(10));

        personalInfo.add(new Label("Name:"), 0, 0);
        personalInfo.add(new TextField("Placeholder"), 1, 0);

        personalInfo.add(new Label("Address:"), 0, 1);
        TextArea addressArea = new TextArea("Placeholder");
        addressArea.setPrefRowCount(3);
        personalInfo.add(addressArea, 1, 1);

        personalInfo.add(new Label("SSN:"), 2, 0);
        personalInfo.add(new TextField("Placeholder"), 3, 0);

        personalInfo.add(new Label("Cell:"), 2, 1);
        personalInfo.add(new TextField("Placeholder"), 3, 1);

        personalInfo.add(new Label("Email:"), 2, 2);
        personalInfo.add(new TextField("Placeholder"), 3, 2);

        // deceased information section
        Label deceasedInfo = new Label("Deceased Information");
        deceasedInfo.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        // reason for request section
        Label reasonForRequest = new Label("Reason for Request");
        reasonForRequest.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        // comments and actions
        HBox actions = new HBox(10);
        actions.setPadding(new Insets(10));
        Button approveButton = new Button("Forward to Approval");
        Button rejectButton = new Button("Request Resubmission");
        actions.getChildren().addAll(approveButton, rejectButton);
        approveButton.setOnAction(e -> {
            // implement later
        });
        rejectButton.setOnAction(e -> {
            // implement later
        });

        VBox commentsBox = new VBox(5);
        Label commentsLabel = new Label("Comments:");
        TextArea commentsArea = new TextArea("\u2022 Invalid social security number");
        commentsArea.setPrefRowCount(2);
        commentsBox.getChildren().addAll(commentsLabel, commentsArea);

        HBox bottomSection = new HBox(20, actions, commentsBox);
        bottomSection.setPadding(new Insets(10));

        // add all layout items
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, dateBar, new Label("Personal Information"), personalInfo,
        deceasedInfo, reasonForRequest, bottomSection);
        mainLayout.setPadding(new Insets(10));

        // scene
        Scene scene = new Scene(mainLayout);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}