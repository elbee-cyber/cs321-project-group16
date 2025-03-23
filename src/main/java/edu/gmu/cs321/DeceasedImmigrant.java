package edu.gmu.cs321;

public class DeceasedImmigrant extends Person { //Person class to be implemented
    private int immigrantID;
    private String countryOfOrigin;

    //Constructor for DeceasedImmigrant class
    public DeceasedImmigrant(String firstName, String lastName, String email, String citizenshipStatus, String dateOfBirth, String city, String state, String zipCode, String phoneNumber, int immID, String origin) {
        super(firstName, lastName, email, citizenshipStatus, dateOfBirth, city, state, zipCode, phoneNumber);
        immigrantID = immID; //will have to be changed to a unique id, maybe corresponding to the order in which each deceasedimm is created
        countryOfOrigin = origin;
    }
    
    // Getter for immigrantID
    public int getImmigrantID() {
        return immigrantID;
    }

    // Setter for immigrantID
    public void setImmigrantID(int immigrantID) {
        if (immigrantID < 0) {
            throw new IllegalArgumentException();
        }
        this.immigrantID = immigrantID;
    }

    // Getter for countryOfOrigin
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    // Setter for countryOfOrigin
    public void setCountryOfOrigin(String countryOfOrigin) {
        if (countryOfOrigin == null) {
            throw new IllegalArgumentException();
        }
        this.countryOfOrigin = countryOfOrigin;
    }

    // toString method for DeceasedImmigrant class
    @Override
    public String toString() {
        return super.toString() + "\nImmigrant ID: " + immigrantID + "\nCountry of Origin: " + countryOfOrigin;
    }
}
