package edu.gmu.cs321;

// This class represents an ImmigrationRequest form, extending the RequestForm class.
public class ImmigrationRequest extends RequestForm {
    private String requestStatus;
    private String submissionDate;

    /**
     * Constructor to initialize an ImmigrationRequest object with necessary details.
     * 
     * @param requestID            Unique identifier for the form.
     * @param requestorName     Name of the person requesting the form.
     * @param requestorCitizenship Citizenship status of the requestor.
     * @param deceasedPersonName Name of the deceased person.
     * @param isLegible         Indicates if the form is legible.
     * @param requestStatus     Status of the request (e.g., pending, approved).
     * @param submissionDate    Date of submission of the request.
     */
    public ImmigrationRequest(String requestID, String requestorName, String requestorCitizenship, String deceasedPersonName, boolean isLegible, String requestStatus, String submissionDate) {
        super(requestID, requestorName, requestorCitizenship, deceasedPersonName, isLegible);
        this.requestStatus = requestStatus;
        this.submissionDate = submissionDate;
    }

    /**
     * Getter for requestID
     * 
     * @return the unique identifier for the form
     */
    public String getRequestID() {
        return super.getFormID();
    }

    /**
     * Getter for requestStatus
     * 
     * @return the status of the request
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * Setter for requestStatus
     * 
     * @throws IllegalArgumentException if requestStatus is null
     * @param requestStatus the status of the request
     */
    public void setRequestStatus(String requestStatus) {
        if (requestStatus == null) {
            throw new IllegalArgumentException();
        }
        this.requestStatus = requestStatus;
    }

    /**
     * Getter for submissionDate
     * 
     * @return the date of submission of the request
     */
    public String getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Setter for submissionDate
     * 
     * @throws IllegalArgumentException if submissionDate is null
     * @param submissionDate the date of submission of the request
     */
    public void setSubmissionDate(String submissionDate) {
        if (submissionDate == null) {
            throw new IllegalArgumentException();
        }
        this.submissionDate = submissionDate;
    }
}
