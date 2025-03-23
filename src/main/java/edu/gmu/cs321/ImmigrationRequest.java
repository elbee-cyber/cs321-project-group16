package edu.gmu.cs321;

public class ImmigrationRequest extends RequestForm {
    private String requestStatus;
    private String submissionDate;

    public ImmigrationRequest(String formID, String requestorName, String requestorCitizenship, String deceasedPersonName, boolean isLegible, String requestStatus, String submissionDate) {
        super(formID, requestorName, requestorCitizenship, deceasedPersonName, isLegible);
        this.requestStatus = requestStatus;
        this.submissionDate = submissionDate;
    }

    // Getter for requestStatus
    public String getRequestStatus() {
        return requestStatus;
    }

    // Setter for requestStatus
    public void setRequestStatus(String requestStatus) {
        if (requestStatus == null) {
            throw new IllegalArgumentException();
        }
        this.requestStatus = requestStatus;
    }

    // Getter for submissionDate
    public String getSubmissionDate() {
        return submissionDate;
    }

    // Setter for submissionDate
    public void setSubmissionDate(String submissionDate) {
        if (submissionDate == null) {
            throw new IllegalArgumentException();
        }
        this.submissionDate = submissionDate;
    }
}
