/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poe;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Message functionality
 * @author RC_Student_lab
 */
public class MessageTest {
    
    private Message message;
    private final String validRecipient = "+27123456789";
    private final String validContent = "Hello World Test Message";
    
    public MessageTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("Starting Message tests...");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("Message tests completed.");
    }
    
    @BeforeEach
    public void setUp() {
        // Create a new Message instance for each test
        message = new Message(validRecipient, validContent);
    }
    
    // Constructor tests
    @Test
    public void testValidMessageCreation() {
        Message msg = new Message(validRecipient, validContent);
        assertNotNull(msg, "Message should be created successfully");
        assertEquals(validContent, msg.getMessageContent(), "Message content should match");
    }
    
    @Test
    public void testInvalidRecipientThrowsException() {
        // Test various invalid recipient formats
        assertThrows(IllegalArgumentException.class, () -> {
            new Message("123456789", "Test message");
        }, "Invalid recipient without +27 should throw exception");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Message("+271234567", "Test message"); // Too short
        }, "Recipient with too few digits should throw exception");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Message("+2712345678901", "Test message"); // Too long
        }, "Recipient with too many digits should throw exception");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Message("+28123456789", "Test message"); // Wrong country code
        }, "Wrong country code should throw exception");
    }
    
    @Test
    public void testMessageTooLongThrowsException() {
        String longMessage = "a".repeat(251); // 251 characters
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(validRecipient, longMessage);
        }, "Message exceeding 250 characters should throw exception");
    }
    
    @Test
    public void testMaxLengthMessageAccepted() {
        String maxMessage = "a".repeat(250); // Exactly 250 characters
        assertDoesNotThrow(() -> {
            new Message(validRecipient, maxMessage);
        }, "Message with exactly 250 characters should be accepted");
    }
    
    // Tests for isValidRecipient method
    @Test
    public void testIsValidRecipientValid() {
        assertTrue(message.isValidRecipient("+27123456789"), "Valid SA number should pass");
        assertTrue(message.isValidRecipient("+27987654321"), "Another valid SA number should pass");
        assertTrue(message.isValidRecipient("+27000000000"), "SA number with zeros should pass");
    }
    
    @Test
    public void testIsValidRecipientInvalid() {
        assertFalse(message.isValidRecipient("0123456789"), "Number without +27 should fail");
        assertFalse(message.isValidRecipient("+271234567"), "Number too short should fail");
        assertFalse(message.isValidRecipient("+2712345678901"), "Number too long should fail");
        assertFalse(message.isValidRecipient("+28123456789"), "Wrong country code should fail");
        assertFalse(message.isValidRecipient(null), "Null recipient should fail");
        assertFalse(message.isValidRecipient(""), "Empty recipient should fail");
        assertFalse(message.isValidRecipient("+27abcdefghi"), "Non-numeric digits should fail");
    }
    
    // Tests for message ID generation and validation
    @Test
    public void testIsValidMessageID() {
        assertTrue(message.isValidMessageID(), "Generated message ID should be valid");
    }
    
    @Test
    public void testMessageIDLength() {
        // Create multiple messages to test ID generation
        for (int i = 0; i < 10; i++) {
            Message msg = new Message(validRecipient, "Test " + i);
            assertTrue(msg.isValidMessageID(), "Each generated message ID should be 9 digits");
        }
    }
    
    // Tests for getMessageOption method
    @Test
    public void testGetMessageOptionValid() {
        assertEquals("Message successfully sent.", message.getMessageOption(1), 
                    "Option 1 should return success message");
        assertEquals("Press 0 to delete message.", message.getMessageOption(2), 
                    "Option 2 should return delete instruction");
        assertEquals("Message successfully stored.", message.getMessageOption(3), 
                    "Option 3 should return stored message");
    }
    
    @Test
    public void testGetMessageOptionInvalid() {
        assertEquals("Invalid option entered.", message.getMessageOption(0), 
                    "Option 0 should return invalid message");
        assertEquals("Invalid option entered.", message.getMessageOption(4), 
                    "Option 4 should return invalid message");
        assertEquals("Invalid option entered.", message.getMessageOption(-1), 
                    "Negative option should return invalid message");
        assertEquals("Invalid option entered.", message.getMessageOption(100), 
                    "Large option should return invalid message");
    }
    
    // Tests for JSON functionality
    @Test
    public void testToJSON() {
        JSONObject json = message.toJSON();
        
        assertNotNull(json, "JSON object should not be null");
        assertTrue(json.has("messageID"), "JSON should contain messageID");
        assertTrue(json.has("messageHash"), "JSON should contain messageHash");
        assertTrue(json.has("recipient"), "JSON should contain recipient");
        assertTrue(json.has("message"), "JSON should contain message");
        
        assertEquals(validRecipient, json.getString("recipient"), 
                    "JSON recipient should match original");
        assertEquals(validContent, json.getString("message"), 
                    "JSON message should match original");
    }
    
    // Tests for printMessageDetails
    @Test
    public void testPrintMessageDetails() {
        String details = message.printMessageDetails();
        
        assertNotNull(details, "Message details should not be null");
        assertTrue(details.contains("Message ID:"), "Details should contain Message ID label");
        assertTrue(details.contains("Message Hash:"), "Details should contain Message Hash label");
        assertTrue(details.contains("Recipient:"), "Details should contain Recipient label");
        assertTrue(details.contains("Message:"), "Details should contain Message label");
        assertTrue(details.contains(validRecipient), "Details should contain recipient");
        assertTrue(details.contains(validContent), "Details should contain message content");
    }
    
    // Tests for static message count
    @Test
    public void testMessageCountIncrement() {
        int initialCount = Message.getTotalMessagesSent();
        
        // Create new messages and verify count increases
        new Message(validRecipient, "Test 1");
        assertEquals(initialCount + 1, Message.getTotalMessagesSent(), 
                    "Message count should increment by 1");
        
        new Message(validRecipient, "Test 2");
        assertEquals(initialCount + 2, Message.getTotalMessagesSent(), 
                    "Message count should increment by 2");
    }
    
    // Tests for message hash creation
    @Test
    public void testMessageHashFormat() {
        Message msg = new Message(validRecipient, "Hello World");
        String details = msg.printMessageDetails();
        
        // Extract hash from details (this is a bit hacky but works for testing)
        String[] lines = details.split("\n");
        String hashLine = "";
        for (String line : lines) {
            if (line.startsWith("Message Hash:")) {
                hashLine = line.substring("Message Hash:".length()).trim();
                break;
            }
        }
        
        assertFalse(hashLine.isEmpty(), "Message hash should not be empty");
        assertTrue(hashLine.contains(":"), "Message hash should contain colons as separators");
        
        // Hash should contain first and last word in uppercase
        assertTrue(hashLine.contains("HELLO"), "Hash should contain first word in uppercase");
        assertTrue(hashLine.contains("WORLD"), "Hash should contain last word in uppercase");
    }
    
    // Edge case tests
    @Test
    public void testSingleWordMessage() {
        Message msg = new Message(validRecipient, "Hello");
        assertNotNull(msg, "Single word message should be created successfully");
        assertEquals("Hello", msg.getMessageContent(), "Single word content should match");
    }
    
    @Test
    public void testMessageWithExtraSpaces() {
        Message msg = new Message("  " + validRecipient + "  ", "  Hello World  ");
        assertEquals("Hello World  ", msg.getMessageContent(), "Message content should preserve trailing spaces");
    }
    
    @Test
    public void testEmptyMessage() {
        assertDoesNotThrow(() -> {
            new Message(validRecipient, "");
        }, "Empty message should be allowed");
    }
    
    @Test
    public void testMessageWithSpecialCharacters() {
        String specialMessage = "Hello! @#$%^&*()_+ World?";
        Message msg = new Message(validRecipient, specialMessage);
        assertEquals(specialMessage, msg.getMessageContent(), 
                    "Message with special characters should be preserved");
    }
    
    // Integration test
    @Test
    public void testCompleteMessageFlow() {
        // Create message
        Message msg = new Message(validRecipient, "Integration test message");
        
        // Verify all components work together
        assertTrue(msg.isValidMessageID(), "Message ID should be valid");
        assertTrue(msg.isValidRecipient(validRecipient), "Recipient should be valid");
        assertEquals("Message successfully sent.", msg.getMessageOption(1), 
                    "Option 1 should work");
        
        // Verify JSON creation
        JSONObject json = msg.toJSON();
        assertNotNull(json, "JSON should be created");
        
        // Verify details printing
        String details = msg.printMessageDetails();
        assertNotNull(details, "Details should be printed");
        
        // Verify message count updated
        assertTrue(Message.getTotalMessagesSent() > 0, "Total message count should be positive");
    }
}