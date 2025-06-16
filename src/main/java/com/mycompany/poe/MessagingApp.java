/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poe;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author RC_Student_lab
 */
public class MessagingApp {
    // Arrays to store different types of messages
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void runMessagingApp(Login user) {
        // Populate arrays with test data
        populateTestData();
        
        // Display menu and handle user choices
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1 -> displaySenderAndRecipient();
                case 2 -> displayLongestMessage();
                case 3 -> searchMessageByID();
                case 4 -> searchMessagesByRecipient();
                case 5 -> deleteMessageByHash();
                case 6 -> displayFullReport();
                case 7 -> saveStoredMessagesToJSON();
                case 8 -> loadStoredMessagesFromJSON();
                case 9 -> {
                    running = false;
                    System.out.println("Returning to main menu...");
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       MESSAGING APPLICATION - PART 3");
        System.out.println("=".repeat(50));
        System.out.println("1. Display sender and recipient of all sent messages");
        System.out.println("2. Display the longest sent message");
        System.out.println("3. Search for message ID and display details");
        System.out.println("4. Search messages by recipient");
        System.out.println("5. Delete message using message hash");
        System.out.println("6. Display full report of all messages");
        System.out.println("7. Save stored messages to JSON file");
        System.out.println("8. Load stored messages from JSON file");
        System.out.println("9. Exit to main menu");
        System.out.print("Enter your choice: ");
    }
    
    private static void populateTestData() {
        try {
           // Clear existing arrays
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        
        // Test Data Message 1 - Sent
        Message msg1 = new Message("+27834557836", "Did you get the cake?");
        sentMessages.add(msg1);
        
        // Test Data Message 2 - Stored
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        storedMessages.add(msg2);
        
        // Test Data Message 3 - Disregarded
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        disregardedMessages.add(msg3);
        
        // Test Data Message 4 - Sent (CORRECTED: This should be outgoing to 0838884567)
        Message msg4 = new Message("0838884567", "It is dinner time!");
        sentMessages.add(msg4);
        
        // Test Data Message 5 - Stored
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        storedMessages.add(msg5);
        
        System.out.println("✅ Test data populated successfully.");
        System.out.println("   - Sent messages: " + sentMessages.size());
        System.out.println("   - Stored messages: " + storedMessages.size());
        System.out.println("   - Disregarded messages: " + disregardedMessages.size());
        
    } catch (IllegalArgumentException e) {
        System.out.println("❌ Error populating test data: " + e.getMessage());
    }
}
    
    // 2a. Display the sender and recipient of all sent messages
    private static void displaySenderAndRecipient() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     SENDER AND RECIPIENT OF ALL SENT MESSAGES");
        System.out.println("=".repeat(50));
        
        if (sentMessages.isEmpty()) {
            System.out.println("❌ No sent messages found.");
            return;
        }
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            System.out.println("Message " + (i + 1) + ":");
            System.out.println("Sender: Developer");
            System.out.println("Recipient: " + getRecipientFromMessage(msg));
            System.out.println("Message: " + msg.getMessageContent());
            System.out.println("-".repeat(30));
        }
    }
    
    // 2b. Display the longest sent message
    private static void displayLongestMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            LONGEST MESSAGE");
        System.out.println("=".repeat(50));
        
        Message longestMessage = null;
        String longestType = "";
        
        // Check sent messages
        for (Message msg : sentMessages) {
            if (longestMessage == null || msg.getMessageContent().length() > longestMessage.getMessageContent().length()) {
                longestMessage = msg;
                longestType = "SENT";
            }
        }
        
        // Check stored messages
        for (Message msg : storedMessages) {
            if (longestMessage == null || msg.getMessageContent().length() > longestMessage.getMessageContent().length()) {
                longestMessage = msg;
                longestType = "STORED";
            }
        }
        
        // Check disregarded messages
        for (Message msg : disregardedMessages) {
            if (longestMessage == null || msg.getMessageContent().length() > longestMessage.getMessageContent().length()) {
                longestMessage = msg;
                longestType = "DISREGARDED";
            }
        }
        
        if (longestMessage != null) {
            System.out.println("Longest Message: " + longestMessage.getMessageContent());
            System.out.println("Recipient: " + getRecipientFromMessage(longestMessage));
            System.out.println("Type: " + longestType);
            System.out.println("Length: " + longestMessage.getMessageContent().length() + " characters");
        } else {
            System.out.println("❌ No messages found.");
        }
    }
    
    // 2c. Search for a message ID and display the corresponding recipient and message
    private static void searchMessageByID() {
        System.out.print("\nEnter Message ID to search: ");
        String searchID = scanner.nextLine();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("      SEARCH RESULTS FOR MESSAGE ID: " + searchID);
        System.out.println("=".repeat(50));
        
        Message foundMessage = null;
        String messageType = "";
        
        // Search in sent messages
        foundMessage = findMessageByID(sentMessages, searchID);
        if (foundMessage != null) {
            messageType = "SENT";
        }
        
        // Search in stored messages if not found
        if (foundMessage == null) {
            foundMessage = findMessageByID(storedMessages, searchID);
            if (foundMessage != null) {
                messageType = "STORED";
            }
        }
        
        // Search in disregarded messages if not found
        if (foundMessage == null) {
            foundMessage = findMessageByID(disregardedMessages, searchID);
            if (foundMessage != null) {
                messageType = "DISREGARDED";
            }
        }
        
        if (foundMessage != null) {
            System.out.println("✅ Message found!");
            System.out.println(foundMessage.printMessageDetails());
            System.out.println("Type: " + messageType);
        } else {
            System.out.println("❌ Message with ID '" + searchID + "' not found.");
        }
    }
    
    // 2d. Search for all messages sent to a particular recipient
    private static void searchMessagesByRecipient() {
        System.out.print("\nEnter recipient number to search (+27xxxxxxxxx): ");
        String searchRecipient = scanner.nextLine();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("      MESSAGES FOR RECIPIENT: " + searchRecipient);
        System.out.println("=".repeat(50));
        
        boolean found = false;
        
        // Search sent messages
        System.out.println("SENT MESSAGES:");
        for (Message msg : sentMessages) {
            if (getRecipientFromMessage(msg).equals(searchRecipient)) {
                System.out.println(msg.printMessageDetails());
                System.out.println("Type: SENT");
                System.out.println("-".repeat(30));
                found = true;
            }
        }
        
        // Search stored messages
        System.out.println("\nSTORED MESSAGES:");
        for (Message msg : storedMessages) {
            if (getRecipientFromMessage(msg).equals(searchRecipient)) {
                System.out.println(msg.printMessageDetails());
                System.out.println("Type: STORED");
                System.out.println("-".repeat(30));
                found = true;
            }
        }
        
        // Search disregarded messages
        System.out.println("\nDISREGARDED MESSAGES:");
        for (Message msg : disregardedMessages) {
            if (getRecipientFromMessage(msg).equals(searchRecipient)) {
                System.out.println(msg.printMessageDetails());
                System.out.println("Type: DISREGARDED");
                System.out.println("-".repeat(30));
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("❌ No messages found for recipient: " + searchRecipient);
        }
    }
    
    // 2e. Delete a message using the message hash
    private static void deleteMessageByHash() {
        System.out.print("\nEnter message hash to delete: ");
        String hashToDelete = scanner.nextLine();
        
        // Try to delete from sent messages
        if (deleteFromList(sentMessages, hashToDelete, "SENT")) return;
        
        // Try to delete from stored messages
        if (deleteFromList(storedMessages, hashToDelete, "STORED")) return;
        
        // Try to delete from disregarded messages
        if (deleteFromList(disregardedMessages, hashToDelete, "DISREGARDED")) return;
        
        System.out.println("❌ Message with hash '" + hashToDelete + "' not found.");
    }
    
    private static boolean deleteFromList(ArrayList<Message> messageList, String hash, String type) {
        for (int i = 0; i < messageList.size(); i++) {
            Message msg = messageList.get(i);
            if (getMessageHashFromMessage(msg).equals(hash)) {
                String deletedContent = msg.getMessageContent();
                messageList.remove(i);
                System.out.println("✅ " + type + " message \"" + deletedContent + "\" successfully deleted.");
                return true;
            }
        }
        return false;
    }
    
    // 2f. Display a report that lists the full details of all sent messages
    private static void displayFullReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              FULL MESSAGE REPORT");
        System.out.println("=".repeat(60));
        
        int totalMessages = sentMessages.size() + storedMessages.size() + disregardedMessages.size();
        System.out.println("Total Messages: " + totalMessages);
        System.out.println("Sent Messages: " + sentMessages.size());
        System.out.println("Stored Messages: " + storedMessages.size());
        System.out.println("Disregarded Messages: " + disregardedMessages.size());
        System.out.println("Total Messages from Message class: " + Message.getTotalMessagesSent());
        
        // Display sent messages
        if (!sentMessages.isEmpty()) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("           SENT MESSAGES");
            System.out.println("=".repeat(40));
            for (int i = 0; i < sentMessages.size(); i++) {
                displayMessageDetails(sentMessages.get(i), "SENT", i + 1);
            }
        }
        
        // Display stored messages
        if (!storedMessages.isEmpty()) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("          STORED MESSAGES");
            System.out.println("=".repeat(40));
            for (int i = 0; i < storedMessages.size(); i++) {
                displayMessageDetails(storedMessages.get(i), "STORED", i + 1);
            }
        }
        
        // Display disregarded messages
        if (!disregardedMessages.isEmpty()) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("        DISREGARDED MESSAGES");
            System.out.println("=".repeat(40));
            for (int i = 0; i < disregardedMessages.size(); i++) {
                displayMessageDetails(disregardedMessages.get(i), "DISREGARDED", i + 1);
            }
        }
    }
    
    private static void displayMessageDetails(Message msg, String status, int number) {
        System.out.println("Message #" + number + " (" + status + "):");
        System.out.println(msg.printMessageDetails());
        System.out.println("Sender: Developer");
        System.out.println("Status: " + status);
        System.out.println("Length: " + msg.getMessageContent().length() + " characters");
        System.out.println("-".repeat(30));
    }
    
    // Save stored messages to JSON file using existing toJSON method
    private static void saveStoredMessagesToJSON() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Message msg : storedMessages) {
                jsonArray.put(msg.toJSON());
            }
            
            try (FileWriter writer = new FileWriter("stored_messages.json")) {
                writer.write(jsonArray.toString(2)); // Pretty print with indentation
            } // Pretty print with indentation
            
            System.out.println("✅ " + storedMessages.size() + " stored messages successfully saved to stored_messages.json");
        } catch (IOException e) {
            System.out.println("❌ Error saving to JSON file: " + e.getMessage());
        }
    }
    
    // Load stored messages from JSON file
    private static void loadStoredMessagesFromJSON() {
        try {
            StringBuilder content;
            try (FileReader reader = new FileReader("stored_messages.json")) {
                content = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    content.append((char) character);
                }
            }
            
            JSONArray jsonArray = new JSONArray(content.toString());
            ArrayList<Message> loadedMessages = new ArrayList<>();
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMsg = jsonArray.getJSONObject(i);
                String recipient = jsonMsg.getString("recipient");
                String messageContent = jsonMsg.getString("message");
                
                try {
                    Message msg = new Message(recipient, messageContent);
                    loadedMessages.add(msg);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Skipped invalid message: " + e.getMessage());
                }
            }
            
            if (!loadedMessages.isEmpty()) {
                storedMessages = loadedMessages;
                System.out.println("✅ " + storedMessages.size() + " stored messages successfully loaded from stored_messages.json");
            } else {
                System.out.println("⚠️ No valid messages found in JSON file.");
            }
            
        } catch (IOException e) {
            System.out.println("❌ Error loading from JSON file: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("❌ Error parsing JSON file: " + e.getMessage());
        }
    }
    
    // Helper methods to extract information from Message objects
    private static String getRecipientFromMessage(Message msg) {
        // Extract recipient from message details string
        String details = msg.printMessageDetails();
        String[] lines = details.split("\n");
        for (String line : lines) {
            if (line.startsWith("Recipient: ")) {
                return line.substring("Recipient: ".length());
            }
        }
        return "Unknown";
    }
    
    private static String getMessageHashFromMessage(Message msg) {
        // Extract hash from message details string
        String details = msg.printMessageDetails();
        String[] lines = details.split("\n");
        for (String line : lines) {
            if (line.startsWith("Message Hash: ")) {
                return line.substring("Message Hash: ".length());
            }
        }
        return "Unknown";
    }
    
    private static String getMessageIDFromMessage(Message msg) {
        // Extract ID from message details string
        String details = msg.printMessageDetails();
        String[] lines = details.split("\n");
        for (String line : lines) {
            if (line.startsWith("Message ID: ")) {
                return line.substring("Message ID: ".length());
            }
        }
        return "Unknown";
    }
    
    private static Message findMessageByID(ArrayList<Message> messageList, String searchID) {
        for (Message msg : messageList) {
            if (getMessageIDFromMessage(msg).equals(searchID)) {
                return msg;
            }
        }
        return null;
    }
    
    // Unit test methods for testing functionality
    public static void runUnitTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            RUNNING UNIT TESTS");
        System.out.println("=".repeat(50));
        
        // Test 1: Assert equals - Sent Messages array correctly populated
        System.out.println("Test 1 - Testing sent messages population:");
        populateTestData();
        if (sentMessages.size() == 2) {
            System.out.println("✅ PASS: Sent messages array correctly populated with 2 messages");
        } else {
            System.out.println("❌ FAIL: Expected 2 sent messages, got " + sentMessages.size());
        }
        
        // Test 2: Display longest message test
        System.out.println("\nTest 2 - Testing longest message detection:");
        Message longestMsg = null;
        for (Message msg : storedMessages) {
            if (longestMsg == null || msg.getMessageContent().length() > longestMsg.getMessageContent().length()) {
                longestMsg = msg;
            }
        }
        if (longestMsg != null && longestMsg.getMessageContent().equals("Where are you? You are late! I have asked you to be on time.")) {
            System.out.println("✅ PASS: Longest message correctly identified");
        } else {
            System.out.println("❌ FAIL: Longest message not correctly identified");
        }
        
        // Test 3: Search for message ID
        System.out.println("\nTest 3 - Testing message ID search:");
        Message testMsg = sentMessages.get(0);
        String testID = getMessageIDFromMessage(testMsg);
        Message foundMsg = findMessageByID(sentMessages, testID);
        if (foundMsg != null) {
            System.out.println("✅ PASS: Message found by ID search");
        } else {
            System.out.println("❌ FAIL: Message not found by ID search");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           UNIT TESTS COMPLETED");
        System.out.println("=".repeat(50));
    }
    
}
