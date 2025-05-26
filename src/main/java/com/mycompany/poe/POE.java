/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poe;

import java.util.Scanner;

/**
 *
 * @author RC_Student_lab
 */
public class POE {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Register a user
        Login user = new Login("", "", "", "", "");
        System.out.println("=== Registration ===");
        String registrationMessage = user.registerUser();
        System.out.println(registrationMessage);
        
        // Only allow login if registration was successful
        if (registrationMessage.contains("successful")) {
            boolean isSuccess = user.loginUser("Rafael", "pass123");
            System.out.println(user.returnLoginStatus(isSuccess));
            
            // Access messenger if login successful
            if (isSuccess) {
                System.out.println("\n=== Messenger ===");
                
                System.out.print("Enter recipient (+27xxxxxxxxx): ");
                String recipient = scanner.nextLine();
                System.out.print("Enter message: ");
                String content = scanner.nextLine();
                
                try {
                    Message message = new Message(recipient, content);
                    System.out.println(message.getMessageOption(1));
                    System.out.println(message.printMessageDetails());
                    System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } else {
            System.out.println("❌ Cannot proceed to login due to registration failure.");
        }
        
        scanner.close();
    }
}