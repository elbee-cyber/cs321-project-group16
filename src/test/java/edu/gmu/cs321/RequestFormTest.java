package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestFormTest {

    @Test
    public void testCreateRequestForm() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertEquals("123", form.getFormID());
        assertEquals("John Doe", form.getRequestorName());
        assertEquals("US Citizen", form.getRequestorCitizenship());
        assertEquals("Jane Doe", form.getDeceasedPersonName());
        assertTrue(form.isLegible());
    }

    @Test
    public void testUpdateRequestForm() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        form.setRequestorName("Alice Johnson");
        form.setDeceasedPersonName("Bob Johnson");
        
        assertEquals("Alice Johnson", form.getRequestorName());
        assertEquals("Bob Johnson", form.getDeceasedPersonName());
    }

    @Test
    public void testUpdateFormLegibility() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        form.setLegible(false);
        
        assertFalse(form.isLegible());
    }

    @Test
    public void testGetRequestorName() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertEquals("John Doe", form.getRequestorName());
    }

    @Test
    public void testGetDeceasedPersonName() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertEquals("Jane Doe", form.getDeceasedPersonName());
    }

    @Test
    public void testGetFormID() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertEquals("123", form.getFormID());
    }

    @Test
    public void testGetRequestorCitizenship() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertEquals("US Citizen", form.getRequestorCitizenship());
    }

    @Test
    public void testGetCompleteStatus() {
        RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);
        
        assertFalse(form.isComplete());
        
        form.setComplete(true);
        
        assertTrue(form.isComplete());
    }
}
