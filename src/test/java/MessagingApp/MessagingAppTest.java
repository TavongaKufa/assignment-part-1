package MessagingApp;

import com.mycompany.poe.Message;
import com.mycompany.poe.MessagingApp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * JUnit test class for MessagingApp
 * Compatible with NetBeans IDE
 * 
 * @author RC_Student_lab
 */
public class MessagingAppTest {
    
    private static ByteArrayOutputStream outputStreamCaptor;
    private static PrintStream originalOut;
    
    // Fields to access private static arrays via reflection
    private static Field sentMessagesField;
    private static Field storedMessagesField;
    private static Field disregardedMessagesField;
    
    public MessagingAppTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        try {
            // Set up output stream capture
            originalOut = System.out;
            outputStreamCaptor = new ByteArrayOutputStream();
            
            // Get access to private static fields using reflection
            sentMessagesField = MessagingApp.class.getDeclaredField("sentMessages");
            sentMessagesField.setAccessible(true);
            
            storedMessagesField = MessagingApp.class.getDeclaredField("storedMessages");
            storedMessagesField.setAccessible(true);
            
            disregardedMessagesField = MessagingApp.class.getDeclaredField("disregardedMessages");
            disregardedMessagesField.setAccessible(true);
            
        } catch (NoSuchFieldException | SecurityException e) {
            System.err.println("Error in setUpClass: " + e.getMessage());
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Restore original System.out
        if (originalOut != null) {
            System.setOut(originalOut);
        }
        
        // Clean up test files
        File testFile = new File("stored_messages.json");
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @BeforeEach
    public void setUp() {
        try {
            // Reset output stream for each test
            outputStreamCaptor.reset();
            System.setOut(new PrintStream(outputStreamCaptor));
            
            // Clear all message arrays before each test
            clearAllMessageArrays();
            
        } catch (Exception e) {
            System.err.println("Error in setUp: " + e.getMessage());
        }
    }
    
    @AfterEach
    public void tearDown() {
        // Restore original output
        System.setOut(originalOut);
    }
    
    // Helper method to clear all message arrays
    private void clearAllMessageArrays() {
        try {
            if (sentMessagesField != null) {
                ArrayList<?> sentMessages = (ArrayList<?>) sentMessagesField.get(null);
                sentMessages.clear();
            }
            if (storedMessagesField != null) {
                ArrayList<?> storedMessages = (ArrayList<?>) storedMessagesField.get(null);
                storedMessages.clear();
            }
            if (disregardedMessagesField != null) {
                ArrayList<?> disregardedMessages = (ArrayList<?>) disregardedMessagesField.get(null);
                disregardedMessages.clear();
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Error clearing message arrays: " + e.getMessage());
        }
    }
    
    // Helper method to get sent messages
    @SuppressWarnings("unchecked")
    private ArrayList<Message> getSentMessages() {
        try {
            return (ArrayList<Message>) sentMessagesField.get(null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Error getting sent messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Helper method to get stored messages
    @SuppressWarnings("unchecked")
    private ArrayList<Message> getStoredMessages() {
        try {
            return (ArrayList<Message>) storedMessagesField.get(null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Error getting stored messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Helper method to get disregarded messages
    @SuppressWarnings("unchecked")
    private ArrayList<Message> getDisregardedMessages() {
        try {
            return (ArrayList<Message>) disregardedMessagesField.get(null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Error getting disregarded messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Helper method to invoke private methods
    private void invokePrivateMethod(String methodName) {
        try {
            Method method = MessagingApp.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("Error invoking method " + methodName + ": " + e.getMessage());
        }
    }
    
    @Test
    public void testPopulateTestData() {
        System.out.println("Testing populate test data...");
        
        // Act
        invokePrivateMethod("populateTestData");
        
        // Assert
        ArrayList<Message> sentMessages = getSentMessages();
        ArrayList<Message> storedMessages = getStoredMessages();
        ArrayList<Message> disregardedMessages = getDisregardedMessages();
        
        assertEquals(2, sentMessages.size(), "Should have 2 sent messages");
        assertEquals(2, storedMessages.size(), "Should have 2 stored messages");
        assertEquals(1, disregardedMessages.size(), "Should have 1 disregarded message");
        
        // Check console output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Test data populated successfully"), 
                  "Should display success message");
        
        System.out.println("✅ Test populate test data passed");
    }
    
    @Test
    public void testDisplaySenderAndRecipient() {
        System.out.println("Testing display sender and recipient...");
        
        // Arrange
        invokePrivateMethod("populateTestData");
        
        // Act
        invokePrivateMethod("displaySenderAndRecipient");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("SENDER AND RECIPIENT"), 
                  "Should display correct header");
        assertTrue(output.contains("Sender: Developer"), 
                  "Should display sender as Developer");
        assertTrue(output.contains("Message 1:"), 
                  "Should display message 1");
        
        System.out.println("✅ Test display sender and recipient passed");
    }
    
    @Test
    public void testDisplaySenderAndRecipientEmpty() {
        System.out.println("Testing display sender and recipient with empty list...");
        
        // Act (without populating test data)
        invokePrivateMethod("displaySenderAndRecipient");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("No sent messages found"), 
                  "Should display no messages found message");
        
        System.out.println("✅ Test display sender and recipient empty passed");
    }
    
    @Test
    public void testDisplayLongestMessage() {
        System.out.println("Testing display longest message...");
        
        // Arrange
        invokePrivateMethod("populateTestData");
        
        // Act
        invokePrivateMethod("displayLongestMessage");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("LONGEST MESSAGE"), 
                  "Should display correct header");
        assertTrue(output.contains("Where are you? You are late!"), 
                  "Should display the longest message");
        
        System.out.println("✅ Test display longest message passed");
    }
    
    @Test
    public void testDisplayFullReport() {
        System.out.println("Testing display full report...");
        
        // Arrange
        invokePrivateMethod("populateTestData");
        
        // Act
        invokePrivateMethod("displayFullReport");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("FULL MESSAGE REPORT"), 
                  "Should display report header");
        assertTrue(output.contains("Total Messages: 5"), 
                  "Should display correct total");
        assertTrue(output.contains("Sent Messages: 2"), 
                  "Should display correct sent count");
        assertTrue(output.contains("Stored Messages: 2"), 
                  "Should display correct stored count");
        assertTrue(output.contains("Disregarded Messages: 1"), 
                  "Should display correct disregarded count");
        
        System.out.println("✅ Test display full report passed");
    }
    
    @Test
    public void testSaveStoredMessagesToJSON() {
        System.out.println("Testing save stored messages to JSON...");
        
        // Arrange
        invokePrivateMethod("populateTestData");
        
        // Act
        invokePrivateMethod("saveStoredMessagesToJSON");
        
        // Assert
        File jsonFile = new File("stored_messages.json");
        assertTrue(jsonFile.exists(), "JSON file should be created");
        assertTrue(jsonFile.length() > 0, "JSON file should not be empty");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("successfully saved"), 
                  "Should display success message");
        
        System.out.println("✅ Test save stored messages to JSON passed");
    }
    
    @Test
    public void testLoadStoredMessagesFromJSON() {
        System.out.println("Testing load stored messages from JSON...");
        
        // Arrange - Create a test JSON file
        createTestJSONFile();
        getStoredMessages().clear(); // Clear current stored messages
        
        // Act
        invokePrivateMethod("loadStoredMessagesFromJSON");
        
        // Assert
        ArrayList<Message> storedMessages = getStoredMessages();
        assertFalse(storedMessages.isEmpty(), "Should load messages from JSON");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("successfully loaded"), 
                  "Should display success message");
        
        System.out.println("✅ Test load stored messages from JSON passed");
    }
    
    @Test
    public void testLoadFromMissingJSON() {
        System.out.println("Testing load from missing JSON file...");
        
        // Arrange - Ensure file doesn't exist
        File jsonFile = new File("stored_messages.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
        
        // Act
        invokePrivateMethod("loadStoredMessagesFromJSON");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error loading"), 
                  "Should display error message");
        
        System.out.println("✅ Test load from missing JSON passed");
    }
    
    @Test
    public void testFindMessageByID() {
        System.out.println("Testing find message by ID...");
        
        try {
            // Arrange
            invokePrivateMethod("populateTestData");
            ArrayList<Message> sentMessages = getSentMessages();
            assertFalse(sentMessages.isEmpty(), "Should have sent messages");
            
            Message testMessage = sentMessages.get(0);
            String messageID = extractMessageID(testMessage);
            
            // Act
            Method findMethod = MessagingApp.class.getDeclaredMethod("findMessageByID", 
                    ArrayList.class, String.class);
            findMethod.setAccessible(true);
            Message foundMessage = (Message) findMethod.invoke(null, sentMessages, messageID);
            
            // Assert
            assertNotNull(foundMessage, "Should find message with valid ID");
            assertEquals(testMessage.getMessageContent(), foundMessage.getMessageContent(), 
                        "Found message should match original");
            
            System.out.println("✅ Test find message by ID passed");
            
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("Error in testFindMessageByID: " + e.getMessage());
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteMessageByHash() {
        System.out.println("Testing delete message by hash...");
        
        try {
            // Arrange
            invokePrivateMethod("populateTestData");
            ArrayList<Message> sentMessages = getSentMessages();
            int initialSize = sentMessages.size();
            
            Message testMessage = sentMessages.get(0);
            String messageHash = extractMessageHash(testMessage);
            
            // Act
            Method deleteMethod = MessagingApp.class.getDeclaredMethod("deleteFromList", 
                    ArrayList.class, String.class, String.class);
            deleteMethod.setAccessible(true);
            boolean result = (Boolean) deleteMethod.invoke(null, sentMessages, messageHash, "SENT");
            
            // Assert
            assertTrue(result, "Should return true for successful deletion");
            assertEquals(initialSize - 1, sentMessages.size(), 
                        "Should decrease message count by 1");
            
            System.out.println("✅ Test delete message by hash passed");
            
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("Error in testDeleteMessageByHash: " + e.getMessage());
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testRunUnitTests() {
        System.out.println("Testing run unit tests...");
        
        // Act
        assertDoesNotThrow(() -> MessagingApp.runUnitTests(), 
                          "Unit tests should run without throwing exceptions");
        
        // Assert
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("RUNNING UNIT TESTS"), 
                  "Should display unit test header");
        assertTrue(output.contains("UNIT TESTS COMPLETED"), 
                  "Should complete unit tests");
        
        System.out.println("✅ Test run unit tests passed");
    }
    
    // Helper method to create test JSON file
    private void createTestJSONFile() {
        try {
            String testJSON = """
                [
                    {
                        "recipient": "+27123456789",
                        "message": "Test message 1"
                    },
                    {
                        "recipient": "+27987654321",
                        "message": "Test message 2"
                    }
                ]
                """;
            
            try (FileWriter writer = new FileWriter("stored_messages.json")) {
                writer.write(testJSON);
            }
        } catch (IOException e) {
            System.err.println("Error creating test JSON file: " + e.getMessage());
        }
    }
    
    // Helper method to extract message ID
    private String extractMessageID(Message message) {
        try {
            Method getIDMethod = MessagingApp.class.getDeclaredMethod("getMessageIDFromMessage", 
                    Message.class);
            getIDMethod.setAccessible(true);
            return (String) getIDMethod.invoke(null, message);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("Error extracting message ID: " + e.getMessage());
            return "UNKNOWN";
        }
    }
    
    // Helper method to extract message hash
    private String extractMessageHash(Message message) {
        try {
            Method getHashMethod = MessagingApp.class.getDeclaredMethod("getMessageHashFromMessage", 
                    Message.class);
            getHashMethod.setAccessible(true);
            return (String) getHashMethod.invoke(null, message);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("Error extracting message hash: " + e.getMessage());
            return "UNKNOWN";
        }
    }
}