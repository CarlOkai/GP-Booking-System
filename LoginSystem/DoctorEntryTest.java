import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


import javax.swing.JOptionPane;


public class DoctorEntryTest {

    @Test
    public void testConstructor() {
        User user = new User();
        doctorEntry entry = new doctorEntry(user);
        assertNotNull(entry);
    }

    @Test
    public void testBookingSubmission() {
        // Create a dummy user for testing
        User user = new User();
        user.setUsername("testUser");

        // Create an instance of bookingEntry
        bookingEntry bookingEntryInstance = new bookingEntry(user);

        // Set appointment date and time
        bookingEntry.appointmentdateField.setText("2024-04-10");
        bookingEntry.appointmentTimeField.setText("09:00");

        // Trigger the submitButton action
        bookingEntry.submitButton.doClick();

        // Check if the booking was successfully added
        assertTrue(bookingEntry.successfulAdd);
    }

    @Test
    public void testFieldsInitialization() {
        User user = new User();
        doctorEntry entry = new doctorEntry(user);
        assertNotNull(entry.firstNameField);
        assertNotNull(entry.lastNameField);
        assertNotNull(entry.phoneField);
        assertNotNull(entry.emailField);
        assertNotNull(entry.addressField);
        assertNotNull(entry.usernameField);
        assertNotNull(entry.passwordField);
        assertNotNull(entry.workingHoursField);
        assertNotNull(entry.backgroundField);
        assertNotNull(entry.notesField);
        assertNotNull(entry.submitButton);
        assertNotNull(entry.backButton);
    }

    @Test
    public void testSubmitButtonActionPerformed_Success() {
        // Create a doctorEntry instance
        User user = new User();
        user.username = "tobi_s";
        doctorEntry doctorEntry = new doctorEntry(user);

        // Set valid inputs in text fields
        doctorEntry.firstNameField.setText("John");
        doctorEntry.lastNameField.setText("Doe");
        doctorEntry.phoneField.setText("1234567890");
        doctorEntry.emailField.setText("john.doe@example.com");
        doctorEntry.addressField.setText("123 Main St");
        doctorEntry.usernameField.setText("johndoe");
        doctorEntry.passwordField.setText("password");
        doctorEntry.workingHoursField.setText("9am - 5pm");
        doctorEntry.backgroundField.setText("MD");
        doctorEntry.notesField.setText("New doctor");
        
        // Simulate button click and perform assertions
        doctorEntry.submitButton.doClick();

        // Verify that the success message is shown
        assertEquals("Doctor Entry Form", JOptionPane.getFrameForComponent(doctorEntry).getTitle());

        // Verify that text fields are cleared after submission
        assertEquals("", doctorEntry.firstNameField.getText());
        assertEquals("", doctorEntry.lastNameField.getText());
        assertEquals("", doctorEntry.phoneField.getText());
        assertEquals("", doctorEntry.emailField.getText());
        assertEquals("", doctorEntry.addressField.getText());
        assertEquals("", doctorEntry.usernameField.getText());
        assertEquals("", doctorEntry.passwordField.getText());
        assertEquals("", doctorEntry.workingHoursField.getText());
        assertEquals("", doctorEntry.backgroundField.getText());
        assertEquals("", doctorEntry.notesField.getText());
    }
}
