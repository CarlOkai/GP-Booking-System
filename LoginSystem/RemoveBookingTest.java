import org.junit.*;
import static org.junit.Assert.*;

public class RemoveBookingTest {

    private User user = new User();

    @Test
    public void testPopulateBookingDropdown() {
        // Create a new user
        User user = new User();
        user.setUsername("testUser");

        // Create an instance of removeBooking
        removeBooking removeBookingInstance = new removeBooking(user);

        // Call the populateBookingDropdown method using the getter method
        removeBookingInstance.populateBookingDropdown();

        // Check if the bookingIDdrop has been populated
        assertTrue(removeBookingInstance.getBookingIDDrop().getItemCount() > 0);
    }
    
    @Test
    public void testRemoveBooking() {
        // Create a new user
        User user = new User();
        user.setUsername("testUser");

        // Create an instance of removeBooking
        removeBooking removeBookingInstance = new removeBooking(user);

        // Set booking ID to remove
        String bookingIDToRemove = "3";

        // Set the booking ID in the bookingIDField
        removeBooking.bookingIDdrop.setSelectedItem(bookingIDToRemove);

        // Trigger the removeButton action
        removeBooking.removeButton.doClick();

        // Check if the booking was successfully removed
        assertTrue(removeBooking.successfullRemove);

    }

    @Test
    public void testDoesBookingExist() {
        removeBooking removeBooking1 = new removeBooking(new User());

        // Test with an existing booking ID
        assertTrue(removeBooking1.doesBookingExist("6"));

        // Test with a non-existing booking ID
        assertFalse(removeBooking1.doesBookingExist("1000"));
    }

    @Test
    public void testDoctorPatientID() {
        // Create a dummy user for testing
        User user = new User();
        user.username = "test_user";

        // Create an instance of removeBooking
        removeBooking removeBookingInstance = new removeBooking(user);

        // Perform a sample action that sets the doctorPatientID
        removeBookingInstance.getRemoveButton().doClick(); // Simulate a click on the "Remove" button

        // Retrieve the doctorPatientID
        String[] doctorPatientID = removeBookingInstance.getDoctorPatientID();

        // Check if the doctorPatientID is not null and has been set correctly
        assertNotNull(doctorPatientID);
        assertEquals(2, doctorPatientID.length);
        assertNotNull(doctorPatientID[0]);
        assertNotNull(doctorPatientID[1]);

        // Print doctor and patient IDs (optional)
        System.out.println("Doctor ID: " + doctorPatientID[0]);
        System.out.println("Patient ID: " + doctorPatientID[1]);
    }

    @Test
    public void testGetDoctorUsername() {
        removeBooking removeBooking3 = new removeBooking(user);

        // Test with an existing doctor ID
        String doctorUsername = removeBooking3.getDoctorUsername("1");
        assertEquals("john_doe", doctorUsername);

        // Test with a non-existing doctor ID
        assertNull(removeBooking3.getDoctorUsername("1000"));
    }

    @Test
    public void testGetPatientUsername() {
        removeBooking removeBooking4 = new removeBooking(user);

        // Test with an existing patient ID
        String patientUsername = removeBooking4.getPatientUsername("2");
        assertEquals("bob_m", patientUsername); //

        // Test with a non-existing patient ID
        assertNull(removeBooking4.getPatientUsername("1000"));
    }
}
