package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeceasedImmTest {
    
    DeceasedImmigrant testImm = new DeceasedImmigrant("John", "Smith", "4400 University Drive", "Fairfax", "VA", 22030, "01/01/2000");

    @Test
    public void testConstructorCorrect() {        
        assertTrue(testImm.addImm(testImm));
    }

    @Test
    public void testConstructorIncorrect() {
        DeceasedImmigrant nullImm = new DeceasedImmigrant(); 
        assertFalse(nullImm.addImm(nullImm));
    }
}
