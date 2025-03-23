package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImmigrationRequestTest {
    
    ImmigrationRequest newRequest = new ImmigrationRequest("123", "John Doe", "US Citizen", "Jane Doe", true, "Approved", "2021-10-01");

    @Test
    public void testCreateImmigrationRequest() {
        assertEquals("123", newRequest.getFormID());
        assertEquals("John Doe", newRequest.getRequestorName());
        assertEquals("US Citizen", newRequest.getRequestorCitizenship());
        assertEquals("Jane Doe", newRequest.getDeceasedPersonName());
        assertTrue(newRequest.isLegible());
        assertEquals("Approved", newRequest.getRequestStatus());
        assertEquals("2021-10-01", newRequest.getSubmissionDate());
    }

    @Test
    public void testSettersAndGetters() {
        newRequest.setRequestorName("Alice Johnson");
        newRequest.setDeceasedPersonName("Bob Johnson");
        newRequest.setRequestStatus("Denied");
        newRequest.setSubmissionDate("2021-10-02");
        
        assertEquals("Alice Johnson", newRequest.getRequestorName());
        assertEquals("Bob Johnson", newRequest.getDeceasedPersonName());
        assertEquals("Denied", newRequest.getRequestStatus());
        assertEquals("2021-10-02", newRequest.getSubmissionDate());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidParams() {
        newRequest.setRequestorName(null);
        newRequest.setDeceasedPersonName(null);
        newRequest.setRequestStatus(null);
        newRequest.setSubmissionDate(null);
    }
}
