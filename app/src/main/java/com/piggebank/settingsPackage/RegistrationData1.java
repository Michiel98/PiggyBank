package com.piggebank.settingsPackage;

public class RegistrationData1 {

    //From InformationFragment
    String firstName;
    String lastName;
    String birthdate;


    public RegistrationData1(String firstName, String lastName,String birthdate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }


}
