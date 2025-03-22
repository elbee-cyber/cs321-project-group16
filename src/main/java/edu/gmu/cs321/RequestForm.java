package edu.gmu.cs321;

public class RequestForm {
    private String formID;
    private String requestorName;
    private String requestorCitizenship;
    private String deceasedPersonName;
    private boolean isComplete;
    private boolean isLegible;

    public RequestForm(String formID, String requestorName, String requestorCitizenship, String deceasedPersonName, boolean isLegible) {
        this.formID = formID;
        this.requestorName = requestorName;
        this.requestorCitizenship = requestorCitizenship;
        this.deceasedPersonName = deceasedPersonName;
        this.isComplete = false; // Initially, assume incomplete
        this.isLegible = isLegible;
    }

    // Getters and Setters for formID
    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    // Getters and Setters for requestorName
    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    // Getters and Setters for requestorCitizenship
    public String getRequestorCitizenship() {
        return requestorCitizenship;
    }

    public void setRequestorCitizenship(String requestorCitizenship) {
        this.requestorCitizenship = requestorCitizenship;
    }

    // Getters and Setters for deceasedPersonName
    public String getDeceasedPersonName() {
        return deceasedPersonName;
    }

    public void setDeceasedPersonName(String deceasedPersonName) {
        this.deceasedPersonName = deceasedPersonName;
    }

    // Getters and Setters for isComplete
    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    // Getters and Setters for isLegible
    public boolean isLegible() {
        return isLegible;
    }

    public void setLegible(boolean isLegible) {
        this.isLegible = isLegible;
    }

    // Skeleton for validateForm method (returns a boolean value)
    public boolean validateForm() {
        return false;
    }
   
}


   