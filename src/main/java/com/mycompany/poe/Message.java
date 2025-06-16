/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poe;
import java.util.Random;
import org.json.JSONObject;
/**
 *
 * @author RC_Student_lab
 */

public class Message {

   // Static variable to keep track of the total number of messages sent
    private static int messageSentCount = 0;
    
    // Instance variables
    private int numMessages;
    private String messageID;
    private String recipient;
    private String messageContent;
    private String messageHash;
    
    // Constructor
    public Message(String recipient, String messageContent) {
        if (!isValidRecipient(recipient)) {
            throw new IllegalArgumentException("Invalid recipient number. Must start with +27 and be followed by 9 digits.");
        }
        if (messageContent.length() > 250) {
            throw new IllegalArgumentException("Message exceeds 250 characters.");
        }
        this.recipient = recipient.trim();
        this.messageContent = messageContent;
        this.messageID = generateMessageID();
        this.numMessages = ++messageSentCount;
        this.messageHash = createMessageHash();
    }
    
    // Getter for message content
    public String getMessageContent() {
        return messageContent;
    }
    
    // Generate a 9-digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        int id = 100000000 + rand.nextInt(900000000); // Ensures a 9-digit number
        return String.valueOf(id);
    }
    
    // Validate if the message ID has 9 digits
    public boolean isValidMessageID() {
        return messageID != null && messageID.length() == 9;
    }
    
    // Validate recipient phone number format
    public boolean isValidRecipient(String number) {
        return number != null && number.matches("\\+27\\d{9}");
    }
    
    // Create a hash using parts of the content and ID
   
   private String createMessageHash() {
    String[] words = messageContent.trim().split("\\s+");
    
    // Handle empty message content
    if (words.length == 0 || (words.length == 1 && words[0].isEmpty())) {
        String idPrefix = messageID.substring(0, 2);
        return idPrefix + ":" + numMessages + ":EMPTY";
       
    }
    String firstWord = words[0].toUpperCase();
    String lastWord = words[words.length - 1].toUpperCase();
    String idPrefix = messageID.substring(0, 2);
    return idPrefix + ":" + numMessages + ":" + firstWord + lastWord;
}
    // Return a message based on user option
    public String getMessageOption(int option) {
        return switch (option) {
            case 1 -> "Message successfully sent.";
            case 2 -> "Press 0 to delete message.";
            case 3 -> "Message successfully stored.";
            default -> "Invalid option entered.";
        };
    }
    
    // Store message in a JSON object
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("messageID", messageID);
        json.put("messageHash", messageHash);
        json.put("recipient", recipient);
        json.put("message", messageContent);
        return json;
    }
    
    // Print message information
    public String printMessageDetails() {
        return "Message ID: " + messageID +
               "\nMessage Hash: " + messageHash +
               "\nRecipient: " + recipient +
               "\nMessage: " + messageContent;
    }
    
    // Return total messages sent
    public static int getTotalMessagesSent() {
        return messageSentCount;
    }
}