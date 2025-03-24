package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestFormTest {

    RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);

    @Test
    public void testCreateRequestForm() {
        assertEquals("123", form.getFormID());
        assertEquals("John Doe", form.getRequestorName());
        assertEquals("US Citizen", form.getRequestorCitizenship());
        assertEquals("Jane Doe", form.getDeceasedPersonName());
        assertTrue(form.isLegible());
    }

    @Test
    public void testCorrectSettersAndGetters() {
        form.setFormID("124");
        form.setRequestorName("Alice Johnson");
        form.setRequestorCitizenship("Non-US Citizen");
        form.setDeceasedPersonName("Bob Johnson");
        form.setLegible(false);

        assertEquals("124", form.getFormID());
        assertEquals("Alice Johnson", form.getRequestorName());
        assertEquals("Non-US Citizen", form.getRequestorCitizenship());
        assertEquals("Bob Johnson", form.getDeceasedPersonName());
        assertFalse(form.isLegible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectSetters() {
        form.setFormID(null);
        form.setRequestorName(null);
        form.setRequestorCitizenship(null);
        form.setDeceasedPersonName(null);
        form.setLegible(false);
    }

    @Test
    public void testValidateForm_Success() {
        form.setRequestorName("John Doe");
        form.setRequestorCitizenship("US Citizen");
        form.setDeceasedPersonName("Jane Doe");
        
        boolean isValid = form.validateForm();
        assertTrue(isValid);
    }

    @Test
    public void testValidateForm_Failure_MissingRequestorName() {
        form.setRequestorName(null);
        
        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }

    @Test
    public void testValidateForm_Failure_NonUSCitizen() {
        form.setRequestorCitizenship("Non-US Citizen");
        
        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }

    @Test
    public void testValidateForm_Failure_NonLegible() {
        form.setLegible(false);
        
        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }
}
