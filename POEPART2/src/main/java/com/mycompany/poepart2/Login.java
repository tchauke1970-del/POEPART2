/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart2;

/**
 *
 * @author Student
 */
import java.util.regex.Pattern;

/** Registration and authentication feature. Regexes are named and attributed here. */
public class Login {
    // Regex attribution: username must contain "_" and be at most five characters.
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.{1,5}$).*_.*$");
    // Regex attribution: password needs >=8 chars, uppercase, digit and special charachter.
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    // Regex attribution: South African number in international format, +27 plus 9 digits.
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+27\\d{9}$");

    private final String firstName, lastName;
    private final String username, password, cellPhoneNumber;
    private boolean loggedIn;

    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName == null ? "" : firstName.trim();
        this.lastName = lastName == null ? "" : lastName.trim();
        this.username = username == null ? "" : username;
        this.password = password == null ? "" : password;
        this.cellPhoneNumber = cellPhoneNumber == null ? "" : cellPhoneNumber;
    }
    public boolean checkUserName() { return USERNAME_PATTERN.matcher(username).matches(); }
    public boolean checkPasswordComplexity() { return PASSWORD_PATTERN.matcher(password).matches(); }
    public boolean checkCellPhoneNumber() { return PHONE_PATTERN.matcher(cellPhoneNumber).matches(); }

    /** Returns the exact registration feedback for the first failed validation.
     * @return  */
    public String registerUser() {
        if (!checkUserName())
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        if (!checkPasswordComplexity())
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        if (!checkCellPhoneNumber())
            return "Cell phone number incorrectly formatted or does not contain international code.";
        return "User successfully registered.";
    }
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        loggedIn = enteredUsername != null && enteredPassword != null
                && username.equals(enteredUsername) && password.equals(enteredPassword)
                && checkUserName() && checkPasswordComplexity() && checkCellPhoneNumber();
        return loggedIn;
    }
    public String returnLoginStatus() {
        return loggedIn ? "Welcome " + firstName + ", " + lastName + " it is great to see you again."
                : "Username or password incorrect, please try again.";
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
}