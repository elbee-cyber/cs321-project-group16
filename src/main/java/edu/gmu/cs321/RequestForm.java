package edu.gmu.cs321;

/**
 * This class represents a RequestForm used to submit an immigration request.
 * It contains the necessary details like form ID, requestor's name, citizenship status,
 * deceased person's name, and validation status.
 */
public class RequestForm {

    private String formID;
    private String requestorName;
    private String requestorCitizenship;
    private String deceasedPersonName;
    private boolean isComplete;
    private boolean isLegible;

    /**
     * Constructor to initialize a RequestForm object with basic details.
     * 
     * @param formID            Unique identifier for the form.
     * @param requestorName     Name of the person requesting the form.
     * @param requestorCitizenship Citizenship status of the requestor.
     * @param deceasedPersonName Name of the deceased person.
     * @param isLegible         Indicates if the form is legible.
     */
    public RequestForm(String formID, String requestorName, String requestorCitizenship, 
                       String deceasedPersonName, boolean isLegible) {
        this.formID = formID;
        this.requestorName = requestorName;
        this.requestorCitizenship = requestorCitizenship;
        this.deceasedPersonName = deceasedPersonName;
        this.isComplete = false; // Initially, assume incomplete
        this.isLegible = isLegible;
    }

    /**
     * Getter for formID.
     * 
     * @return formID of the request.
     */
    public String getFormID() {
        return formID;
    }

    /**
     * Setter for formID.
     * 
     * @param formID the formID to set.
     */
    public void setFormID(String formID) {
        this.formID = formID;
    }

    /**
     * Getter for requestorName.
     * 
     * @return name of the person requesting the form.
     */
    public String getRequestorName() {
        return requestorName;
    }

    /**
     * Setter for requestorName.
     * 
     * @param requestorName the requestor's name to set.
     */
    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    /**
     * Getter for requestorCitizenship.
     * 
     * @return citizenship status of the requestor.
     */
    public String getRequestorCitizenship() {
        return requestorCitizenship;
    }

    /**
     * Setter for requestorCitizenship.
     * 
     * @param requestorCitizenship the citizenship status to set.
     */
    public void setRequestorCitizenship(String requestorCitizenship) {
        this.requestorCitizenship = requestorCitizenship;
    }

    /**
     * Getter for deceasedPersonName.
     * 
     * @return name of the deceased person.
     */
    public String getDeceasedPersonName() {
        return deceasedPersonName;
    }

    /**
     * Setter for deceasedPersonName.
     * 
     * @param deceasedPersonName the deceased person's name to set.
     */
    public void setDeceasedPersonName(String deceasedPersonName) {
        this.deceasedPersonName = deceasedPersonName;
    }

    /**
     * Getter for isComplete.
     * 
     * @return true if the form is complete, false otherwise.
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Setter for isComplete.
     * 
     * @param isComplete the completion status to set.
     */
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    /**
     * Getter for isLegible.
     * 
     * @return true if the form is legible, false otherwise.
     */
    public boolean isLegible() {
        return isLegible;
    }

    /**
     * Setter for isLegible.
     * 
     * @param isLegible the legibility status to set.
     */
    public void setLegible(boolean isLegible) {
        this.isLegible = isLegible;
    }

    /**
     * Skeleton for the validateForm method.
     * This method will be implemented later to validate the completeness and legibility of the form.
     * 
     * @return false (hardcoded for now as a skeleton).
     */
    public boolean validateForm() {
        return false; // Placeholder logic for now
    }
}
