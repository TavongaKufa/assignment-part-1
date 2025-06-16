/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poe;


import java.util.Scanner;

public class POE {
    public static void main(String[] args) {
        // Welcome banner
        try (Scanner scanner = new Scanner(System.in)) {
            // Welcome banner
            System.out.println("=".repeat(50));
            System.out.println("       Welcome to the Messaging App");
            System.out.println("=".repeat(50));
            
            // Register user
            System.out.println("\n=== User Registration ===");
            System.out.print("Enter your full name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Choose a username: ");
            String username = scanner.nextLine();
            System.out.print("Choose a password: ");
            String password = scanner.nextLine();
            System.out.print("Confirm password: ");
            String confirmPassword = scanner.nextLine();
            
            // Create login object and register user
            Login user = new Login(fullName, email, username, password, confirmPassword);
            String registrationMessage = user.registerUser();
            System.out.println(registrationMessage);
            
            // Proceed if registration successful
            if (registrationMessage.toLowerCase().contains("successful")) {
                System.out.println("\n=== User Login ===");
                System.out.print("Enter username: ");
                String loginUsername = scanner.nextLine();
                System.out.print("Enter password: ");
                String loginPassword = scanner.nextLine();
                
                boolean isSuccess = user.loginUser(loginUsername, loginPassword);
                System.out.println(user.returnLoginStatus(isSuccess));
                
                if (isSuccess) {
                    boolean keepRunning = true;
                    while (keepRunning) {
                        System.out.println("\nWhat would you like to do next?");
                        System.out.println("1. Run Messaging App");
                        System.out.println("2. Run Unit Tests");
                        System.out.println("3. Exit");
                        System.out.print("Choice: ");
                        String option = scanner.nextLine();
                        
                        switch (option) {
                            case "1" -> MessagingApp.runMessagingApp(user);
                            case "2" -> MessagingApp.runUnitTests();
                            case "3" -> {
                                System.out.println("👋 Goodbye!");
                                keepRunning = false;
                            }
                            default -> System.out.println("❌ Invalid choice. Try again.");
                        }
                    }
                }
            } else {
                System.out.println("❌ Cannot proceed to login due to registration failure.");
            }
        }
    }
}
