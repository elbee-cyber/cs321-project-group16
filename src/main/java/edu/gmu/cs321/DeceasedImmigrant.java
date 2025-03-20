package edu.gmu.cs321;

public class DeceasedImmigrant {
    
    private String firstName, lastName, address, city, state, dob;
    private int zip;

    //empty constructor
    public DeceasedImmigrant() {
        firstName = null;
        lastName = null;
        address = null;
        city = null;
        state = null;
        zip = 0;
        dob = null;
    }

    //filled constructor
    public DeceasedImmigrant(String fName, String lName, String addy, String cityName, String stateName, Integer zipCode, String dateOfBirth) {
        firstName = fName;
        lastName = lName;
        address = addy;
        city = cityName;
        state = stateName;
        zip = zipCode;
        dob = dateOfBirth;
    }

    //helper method to check the format of the date of birth value
    private boolean checkDOB() {
        //convert string dob to a char array to check format
        System.out.println(dob);
        char[] ch = dob.toCharArray();
        int mon = Integer.parseInt(dob.substring(0,2));
        int day = Integer.parseInt(dob.substring(3, 5));
        int year = Integer.parseInt(dob.substring(6, 10));
        if (ch[2] != '/' || ch[5] != '/' || mon < 1 || mon > 12 || day < 1 || day > 31 || year < 1908 || year > 2025) {
            System.out.println(mon + ch[2] + day + ch[5] + year);  
            System.out.println("Please format DOB as \"xx/xx/xxxx\".");
            return false;
        }

        //check that february does not go over Feb28
        if (mon == 2 && day > 28) {
            System.out.println("Invalid date");   
            return false;
        }
        return true;
    }

    public boolean checkImm(DeceasedImmigrant imm) {
        if (!imm.checkDOB()) return false;
        
        return true;
    }

    public boolean addImm(DeceasedImmigrant imm) {
        return checkImm(imm);
    }

    public boolean updateImm(int immID, String value, String updated) {
        return true;
    }

    //should return class Immigrant to be created
    public boolean getImm(int immID) {
        return true;
    }
}