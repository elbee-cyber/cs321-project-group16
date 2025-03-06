package edu.gmu.cs321;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DataEntryTest {
    
    @Test
    public void testvalidateForm() {
        DataEntry testData = new DataEntry();
        boolean test = testData.validateForm();
        assertTrue(test);
    }
}
