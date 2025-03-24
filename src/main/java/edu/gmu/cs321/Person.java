package edu.gmu.cs321;

/**
 * This class represents a Person with basic details like name, email, citizenship status,
 * date of birth, address, and phone number.
 */
public class Person {

    private String firstName;
    private String lastName;
    private String email;
    private String citizenshipStatus; // e.g., Citizen or Non-Citizen
    private String dateOfBirth;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;

    /**
     * Constructor to initialize a Person object with necessary details.
     * 
     * @param firstName        First name of the person.
     * @param lastName         Last name of the person.
     * @param email            Email address of the person.
     * @param citizenshipStatus Citizenship status (e.g., Citizen, Non-Citizen).
     * @param dateOfBirth      Date of birth of the person.
     * @param city             City where the person resides.
     * @param state            State where the person resides.
     * @param zipCode          Zip code of the person's address.
     * @param phoneNumber      Phone number of the person.
     */
    public Person(String firstName, String lastName, String email, String citizenshipStatus, 
                  String dateOfBirth, String city, String state, String zipCode, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.citizenshipStatus = citizenshipStatus;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter for first name.
     * 
     * @return first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first name.
     * 
     * @param firstName the first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for last name.
     * 
     * @return last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for last name.
     * 
     * @param lastName the last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for email.
     * 
     * @return email address of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     * 
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for citizenship status.
     * 
     * @return citizenship status of the person.
     */
    public String getCitizenshipStatus() {
        return citizenshipStatus;
    }

    /**
     * Setter for citizenship status.
     * 
     * @param citizenshipStatus the citizenship status to set.
     */
    public void setCitizenshipStatus(String citizenshipStatus) {
        this.citizenshipStatus = citizenshipStatus;
    }

    /**
     * Getter for date of birth.
     * 
     * @return date of birth of the person.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Setter for date of birth.
     * 
     * @param dateOfBirth the date of birth to set.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Getter for city.
     * 
     * @return city where the person resides.
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter for city.
     * 
     * @param city the city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter for state.
     * 
     * @return state where the person resides.
     */
    public String getState() {
        return state;
    }

    /**
     * Setter for state.
     * 
     * @param state the state to set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter for zip code.
     * 
     * @return zip code of the person's address.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Setter for zip code.
     * 
     * @param zipCode the zip code to set.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Getter for phone number.
     * 
     * @return phone number of the person.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for phone number.
     * 
     * @param phoneNumber the phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns a string representation of the Person object.
     * 
     * @return a formatted string containing the person's details.
     */
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + ", Email: " + email + ", Citizenship Status: " + citizenshipStatus + 
               ", Address: " + city + ", " + state + " " + zipCode;
    }
}
