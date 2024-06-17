import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PatientEntryTest {

    @Test
    public void testFirstNameField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);        
        // Set text for the first name field
        patientEntry.firstNameField.setText("John");        
        // Verify if the first name field contains the expected value
        assertEquals("John", patientEntry.firstNameField.getText());
    }

    @Test
    public void testLastNameField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);       
        // Set text for the last name field
        patientEntry.lastNameField.setText("Doe");      
        // Verify if the last name field contains the expected value
        assertEquals("Doe", patientEntry.lastNameField.getText());
    }

    @Test
    public void testPhoneNumberField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);     
        // Set text for the phone number field 
                 
        patientEntry.phoneNumberField.setText("1234567890");       
        // Verify if the phone number field contains the expected value
        assertEquals("1234567890", patientEntry.phoneNumberField.getText());
    }

    @Test
    public void testEmailField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);       
        // Set text for the email field
        patientEntry.emailField.setText("john@example.com");      
        // Verify if the email field contains the expected value
        assertEquals("john@example.com", patientEntry.emailField.getText());
    }

    @Test
    public void testAddressField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);   
        // Set text for the address field
        patientEntry.addressField.setText("123 Main St");   
        // Verify if the address field contains the expected value
        assertEquals("123 Main St", patientEntry.addressField.getText());
    }

    @Test
    public void testUsernameField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);    
        // Set text for the username field
        patientEntry.usernameField.setText("john_doe");    
        // Verify if the username field contains the expected value
        assertEquals("john_doe", patientEntry.usernameField.getText());
    }

    @Test
    public void testPasswordField() {
        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);    
        // Set text for the password field
        patientEntry.passwordField.setText("password");   
        // Verify if the password field contains the expected value
        assertEquals("password", patientEntry.passwordField.getText());
    }

    @Test
    public void testDatabaseConnection() {
        
        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
        final String USERNAME = "co398";
        final String PASSWORD = "x9plodh";

        Connection conn = null;
        
        try {
            // Attempt to establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Check if the connection is not null
            assertNotNull("Database connection is not null", conn);

            // Close the connection
            conn.close();
        } catch (SQLException e) {
            
            e.printStackTrace();
            // Fail the test if connection fails
            fail("Failed to connect to the database");
        }
    }
        @Test
    public void testSubmitButtonWithValidData() {
        // Define your database URL, username, and password
        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
        final String USERNAME = "co398";
        final String PASSWORD = "x9plodh";

        // Create an instance of the patientEntry1 class
        patientEntry1 patientEntry = new patientEntry1(null);

        // Set mock data for the text fields
        patientEntry.firstNameField.setText("John");
        patientEntry.lastNameField.setText("Doe");
        patientEntry.phoneNumberField.setText("1234567890");
        patientEntry.emailField.setText("john@example.com");
        patientEntry.addressField.setText("123 Main St");
        patientEntry.usernameField.setText("jamesmay");
        patientEntry.passwordField.setText("password");

        // Simulate clicking the submit button
        patientEntry.submitButton.doClick();

        // Verify that the data is submitted to the database successfully
        assertTrue("Data should be submitted to the database", isDataSubmittedToDatabase(DB_URL, USERNAME, PASSWORD));
    }

    private boolean isDataSubmittedToDatabase(String dbUrl, String username, String password) {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password);
             Statement statement = conn.createStatement()) {

            // Check if the inserted data exists in the database
            String sql = "SELECT * FROM Patients WHERE username = 'jamesmay'";
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next(); // Return true if data exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}