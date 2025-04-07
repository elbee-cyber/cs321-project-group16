package edu.gmu.cs321;

//This class represents a DeceasedImmigrant domain object that extends the Person class.
public class DeceasedImmigrant extends Person { //Person class to be implemented
    private int immigrantID;
    private String countryOfOrigin;

    /**
     * Constructor to initialize a DeceasedImmigrant object with necessary details.
     * 
     * @param firstName
     * @param lastName
     * @param email
     * @param citizenshipStatus
     * @param dateOfBirth
     * @param city
     * @param state
     * @param zipCode
     * @param phoneNumber
     * @param immID
     * @param origin
     */
    public DeceasedImmigrant(String firstName, String lastName, String email,
                                String citizenshipStatus, String dateOfBirth, String city, String state,
                                String zipCode, String phoneNumber, int immID, String origin) {
        super(firstName, lastName, email, citizenshipStatus, dateOfBirth, 
                city, state, zipCode, phoneNumber);
        immigrantID = immID; //will have to be changed to a unique id, maybe corresponding to the order in which each deceasedimm is created
        countryOfOrigin = origin;
    }
    
    /**
     * Getter for immigrantID
     * 
     * @return ID number of the deceased immigrant
     */
    public int getImmigrantID() {
        return immigrantID;
    }

    /**
     * Setter for immigrantID
     * 
     * @param ID number of the deceased immigrant.
     * @throws IllegalArgumentException if immigrantID is less than 0
     */
    public void setImmigrantID(int immigrantID) {
        if (immigrantID < 0) {
            throw new IllegalArgumentException();
        }
        this.immigrantID = immigrantID;
    }

    /**
     * Getter for countryOfOrigin
     * 
     * @return the deceased immigrant's country of origin
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Setter for countryOfOrigin
     * 
     * @param the deceased immigrant's country of origin
     * @throws IllegalArgumentException if countryOfOrigin is null
     */
    public void setCountryOfOrigin(String countryOfOrigin) {
        if (countryOfOrigin == null) {
            throw new IllegalArgumentException();
        }
        this.countryOfOrigin = countryOfOrigin;
    }

    /**
     * Returns a string representation of the DeceasedImmigrant object.
     * 
     * @return a formatted string containing the deceased immigrant's details
     */
    @Override
    public String toString() {
        return super.toString() + "\nImmigrant ID: " + immigrantID + 
                "\nCountry of Origin: " + countryOfOrigin;
    }
}
