package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains unit tests for the RequestForm class.
 * It tests the creation, setters/getters, and validation methods.
 */
public class RequestFormTest {

    private RequestForm form = new RequestForm("123", "John Doe", "US Citizen", "Jane Doe", true);

    /**
     * Tests the correct creation of a RequestForm object.
     * Verifies that the object is created with the expected field values.
     */
    @Test
    public void createRequestForm_validData_objectCreatedCorrectly() {
        assertEquals("123", form.getFormID());
        assertEquals("John Doe", form.getRequestorName());
        assertEquals("US Citizen", form.getRequestorCitizenship());
        assertEquals("Jane Doe", form.getDeceasedPersonName());
        assertTrue(form.isLegible());
    }

    /**
     * Tests the setters and getters of the RequestForm class.
     * Verifies that the setters correctly update the object fields and getters return expected values.
     */
    @Test
    public void settersAndGetters_validData_updatedCorrectly() {
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

    /**
     * Tests the setters with invalid input.
     * Verifies that the setters throw an IllegalArgumentException for invalid values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setters_invalidData_throwsIllegalArgumentException() {
        form.setFormID(null);
        form.setRequestorName(null);
        form.setRequestorCitizenship(null);
        form.setDeceasedPersonName(null);
        form.setLegible(false);
    }

    /**
     * Tests the validateForm method with valid data.
     * Verifies that the form is valid when required fields are correctly set.
     */
    @Test (expected = AssertionError.class)
    public void validateForm_validData_formValid() {
        form.setRequestorName("John Doe");
        form.setRequestorCitizenship("US Citizen");
        form.setDeceasedPersonName("Jane Doe");

        boolean isValid = form.validateForm();
        assertTrue(isValid);
    }

    /**
     * Tests the validateForm method when the requestor name is missing.
     * Verifies that the form is invalid when requestorName is null.
     */
    @Test (expected = IllegalArgumentException.class)
    public void validateForm_missingRequestorName_formInvalid() {
        form.setRequestorName(null);

        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }

    /**
     * Tests the validateForm method when the requestor is not a US citizen.
     * Verifies that the form is invalid when the citizenship status is not "US Citizen".
     */
    @Test
    public void validateForm_nonUSCitizen_formInvalid() {
        form.setRequestorCitizenship("Non-US Citizen");

        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }

    /**
     * Tests the validateForm method when the form is not legible.
     * Verifies that the form is invalid when it is not legible.
     */
    @Test
    public void validateForm_nonLegibleForm_formInvalid() {
        form.setLegible(false);

        boolean isValid = form.validateForm();
        assertFalse(isValid);
    }
}
