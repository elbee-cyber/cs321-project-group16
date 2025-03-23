package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeceasedImmTest {
    
    DeceasedImmigrant newImm = new DeceasedImmigrant("John", "Smith", "jsmith@gmu.edu","Citizen", "01/01/2000", "Fairfax", "VA", "22030", "123-456-7890", 123, "England");

    @Test
    public void testCreateNewImm() {        
        assertEquals("Name: John Smith, Email: jsmith@gmu.edu, Citizenship Status: Citizen, Address: Fairfax, VA 22030\nImmigrant ID: 123\nCountry of Origin: England", newImm.toString());
    }

    @Test
    public void testCorrectSettersAndGetters() {
        newImm.setFirstName("Jane");
        newImm.setLastName("Doe");
        newImm.setEmail("jdoe@gmu.edu");
        newImm.setCitizenshipStatus("Non-Citizen");
        newImm.setDateOfBirth("02/02/2000");
        newImm.setCity("Arlington");
        newImm.setState("VA");
        newImm.setZipCode("22202");
        newImm.setPhoneNumber("987-654-3210");
        newImm.setImmigrantID(456);
        newImm.setCountryOfOrigin("France");

        assertEquals("Jane", newImm.getFirstName());
        assertEquals("Doe", newImm.getLastName());
        assertEquals("jdoe@gmu.edu", newImm.getEmail());
        assertEquals("Non-Citizen", newImm.getCitizenshipStatus());
        assertEquals("02/02/2000", newImm.getDateOfBirth());
        assertEquals("Arlington", newImm.getCity());
        assertEquals("VA", newImm.getState());
        assertEquals("22202", newImm.getZipCode());
        assertEquals("987-654-3210", newImm.getPhoneNumber());
        assertEquals(456, newImm.getImmigrantID());
        assertEquals("France", newImm.getCountryOfOrigin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectSetters() {
        newImm.setFirstName(null);
        newImm.setLastName(null);
        newImm.setEmail(null);
        newImm.setCitizenshipStatus(null);
        newImm.setDateOfBirth(null);
        newImm.setCity(null);
        newImm.setState(null);
        newImm.setZipCode(null);
        newImm.setPhoneNumber(null);
        newImm.setImmigrantID(-1);
        newImm.setCountryOfOrigin(null);
    }
}
