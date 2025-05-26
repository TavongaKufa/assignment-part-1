/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poe;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author RC_Student_lab
 */
public class Login {
  // Attributes
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Constructor
    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }
    // Methods to check username
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Method to check password complexity
    public static boolean checkPasswordComplexity(String password) {
        return password.length() >= 8 &&
            password.matches(".[A-Z].") &&
            password.matches(".\\d.") &&
            password.matches(".[^a-zA-Z0-9].");
    }

    // Method to validate SA cell phone number format
    public static boolean checkCellPhoneNumber(String cellNumber) {
        return Pattern.matches("^\\+27\\d{8,9}$", cellNumber);
    }

    // Method to register user
    public String registerUser() {
        Scanner scanner = new Scanner(System.in);

        // First Name
        System.out.print("Enter First Name: ");
        this.firstName = scanner.nextLine();

        // Last Name
        System.out.print("Enter Last Name: ");
        this.lastName = scanner.nextLine();

        // Username
        System.out.print("Enter username: ");
        this.username = scanner.nextLine();
        if (!checkUserName(username)) {
            return "❌ Username is not correctly formatted. Please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        System.out.println("Username successfully captured.");

        // Password
        System.out.print("Enter password: ");
        this.password = scanner.nextLine();
        System.out.print(password);
        if (!checkPasswordComplexity(password)) {
            return "❌ Password is not correctly formatted. Please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        System.out.println("Password successfully captured.");

        // Cell Phone
        System.out.print("Enter SA cell phone number (with +27): ");
        this.cellPhoneNumber = scanner.nextLine();
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "❌ Cell phone number is incorrectly formatted or does not contain the international code (+27).";
        }
        System.out.println("Cell phone number successfully added.");

        return "✅ Registration successful for username: " + this.username;
    }

    // Method to login user
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Return login status message
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + this.firstName + " " + this.lastName + ", it is great to see you again.";
        } else {
            return "❌ Username or password incorrect, please try again.";
        }
    }  
    
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
}
