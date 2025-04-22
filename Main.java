/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.assignement1;

/**
 *
 * @author RC_Student_lab
 */


import com.mycompany.test.Login;

public class Main {
  
    public static void main(String[] args) {
        // Register a user
        Login user = new Login("username", "password", "email@example.com", "John", "Doe");
        System.out.println("=== Registration ===");
        String registrationMessage = user.registerUser();
        System.out.println(registrationMessage);
        // Only allow login if registration was successful
        if (registrationMessage.contains("successful")) {
            // Pass the username and password to the loginUser method
            
            System.out.println("❌ Cannot proceed to login due to registration failure.");
        }
    }
}