package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains unit tests for the ImmigrationRequest class.
 * It tests the creation, setters/getters, and validation methods.
 */
public class ImmigrationRequestTest {
    
    ImmigrationRequest newRequest = new ImmigrationRequest("123", "John Doe", 
                                                            "US Citizen", "Jane Doe", true, "Approved", "2021-10-01");

    /**
     * Tests the correct creation of an ImmigrationRequest object.
     * Verifies that the object is created with the expected field values.
     */
    @Test
    public void createImmigrationRequest_validData_objectCreatedCorrectly() {
        assertEquals("123", newRequest.getFormID());
        assertEquals("John Doe", newRequest.getRequestorName());
        assertEquals("US Citizen", newRequest.getRequestorCitizenship());
        assertEquals("Jane Doe", newRequest.getDeceasedPersonName());
        assertTrue(newRequest.isLegible());
        assertEquals("Approved", newRequest.getRequestStatus());
        assertEquals("2021-10-01", newRequest.getSubmissionDate());
    }

    /**
     * Tests the setters and getters of the ImmigrationRequest class.
     * Verifies that the setters correctly update the object fields and getters return expected values.
     */
    @Test
    public void settersAndGetters_validData_updatedCorrectly() {
        newRequest.setRequestorName("Alice Johnson");
        newRequest.setDeceasedPersonName("Bob Johnson");
        newRequest.setRequestStatus("Denied");
        newRequest.setSubmissionDate("2021-10-02");
        
        assertEquals("Alice Johnson", newRequest.getRequestorName());
        assertEquals("Bob Johnson", newRequest.getDeceasedPersonName());
        assertEquals("Denied", newRequest.getRequestStatus());
        assertEquals("2021-10-02", newRequest.getSubmissionDate());
    }

    /** 
     * Tests the setters with invalid input.
     * Verifies that the setters throw an IllegalArgumentException for invalid values.
     */
    @Test (expected = IllegalArgumentException.class)
    public void setters_invalidData_throwsIllegalArgumentException() {
        newRequest.setRequestorName(null);
        newRequest.setDeceasedPersonName(null);
        newRequest.setRequestStatus(null);
        newRequest.setSubmissionDate(null);
    }
}
