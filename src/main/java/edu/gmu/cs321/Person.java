package edu.gmu.cs321;

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

    // Constructor for Person class
    public Person(String firstName, String lastName, String email, String citizenshipStatus, String dateOfBirth, String city, String state, String zipCode, String phoneNumber) {
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

    // Getter and Setter for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
    }

    // Getter and Setter for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException();
        }
        this.lastName = lastName;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    // Getter and Setter for citizenshipStatus
    public String getCitizenshipStatus() {
        return citizenshipStatus;
    }

    public void setCitizenshipStatus(String citizenshipStatus) {
        if (citizenshipStatus == null) {
            throw new IllegalArgumentException();
        }
        this.citizenshipStatus = citizenshipStatus;
    }

    // Getter and Setter for dateOfBirth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException();
        }
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null) {
            throw new IllegalArgumentException();
        }
        this.city = city;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        this.state = state;
    }

    // Getter and Setter for zipCode
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        if (zipCode == null) {
            throw new IllegalArgumentException();
        }
        this.zipCode = zipCode;
    }

    // Getter and Setter for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException();
        }
        this.phoneNumber = phoneNumber;
    }

    // String representation of the Person object
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + ", Email: " + email + ", Citizenship Status: " + citizenshipStatus + 
                ", Address: " + city + ", " + state + " " + zipCode;
    }
}