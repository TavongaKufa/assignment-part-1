/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.mycompany.test.Login;

/**
 *
 * @author RC_Student_lab
 */
public class LOGINTEST1 {
    
    private Login validUser;
    private Login invalidUser;
    
    public LOGINTEST1() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("Starting Login test suite");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("Login test suite completed");
    }
    
    @BeforeEach
    public void setUp() {
        // Create test users before each test
        validUser = new Login("testuser", "password123", "test@example.com", "Test", "User");
        invalidUser = new Login("", "", "", "", "");
    }
    
    @Test
    public void testValidRegistration() {
        String result = validUser.registerUser();
        assertTrue(result.contains("successful"), "Registration should succeed with valid data");
    }
    
    @Test
    public void testInvalidRegistration() {
        String result = invalidUser.registerUser();
        assertTrue(result.contains("failed"), "Registration should fail with empty fields");
    }
    
    @Test
    public void testInvalidEmail() {
        Login userWithBadEmail = new Login("user", "password123", "notanemail", "Test", "User");
        String result = userWithBadEmail.registerUser();
        assertTrue(result.contains("failed"), "Registration should fail with invalid email");
    }
    
    @Test
    public void testWeakPassword() {
        Login userWithWeakPwd = new Login("user", "weak", "test@example.com", "Test", "User");
        String result = userWithWeakPwd.registerUser();
        assertTrue(result.contains("failed"), "Registration should fail with a weak password");
    }
    
    @Test
    public void testSuccessfulLogin() {
        // First register the user
        validUser.registerUser();
        
        // Then test login with correct credentials
        boolean loginResult = validUser.loginUser(validUser.getUsername(), validUser.getPassword());
        assertTrue(loginResult, "Login should succeed with correct credentials");
        
        // Test login status message
        String statusMessage = validUser.returnLoginStatus(loginResult);
        assertTrue(statusMessage.contains("successful"), "Status should indicate successful login");
    }
    
    @Test
    public void testFailedLogin() {
        // Try to login with wrong password
        boolean loginResult = validUser.loginUser(validUser.getUsername(), "wrongpassword");
        assertFalse(loginResult, "Login should fail with incorrect password");
        
        // Test login status message
        String statusMessage = validUser.returnLoginStatus(loginResult);
        assertTrue(statusMessage.contains("failed"), "Status should indicate failed login");
    }
    
    @Test
    public void testGetUsername() {
        assertEquals("testuser", validUser.getUsername(), "getUsername should return the correct username");
    }
    
    /**
     *
     */
    @Test
    public void testGetPassword() {
        assertEquals("password123", validUser.getPassword(), "getPassword should return the correct password");
    }
}