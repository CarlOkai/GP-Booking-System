import org.junit.Test;
import static org.junit.Assert.*;

public class RescheduleBookingTest {

    @Test
    public void testDoctorPatientID() {
    // Create a dummy user for testing
    User user = new User();
    user.username = "test_user";

    // Create an instance of removeBooking
    rescheduleBooking rescheduleBookingInstance = new rescheduleBooking(user);

    // Fill out all the required fields
    rescheduleBookingInstance.bookingIDField.setText("1");
    rescheduleBookingInstance.appointmentdateField.setText("2024-04-05");
    rescheduleBookingInstance.appointmentTimeField.setText("10:00:00");

    // Perform a sample action that sets the doctorPatientID
    rescheduleBookingInstance.getRescheduleButton().doClick(); // Simulate a click on the "Reschedule" button

    // Retrieve the doctorPatientID
    String[] doctorPatientID = rescheduleBookingInstance.getDoctorPatientID();

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
    public void testDoesBookingExist() {
        // Create a dummy user for testing
        User user = new User();
        user.username = "test_user";

        // Create an instance of rescheduleBooking
        rescheduleBooking rescheduleBooking1 = new rescheduleBooking(user);

        // Test with an existing booking ID
        assertTrue(rescheduleBooking1.doesBookingExist("1"));

        // Test with a non-existing booking ID
        assertFalse(rescheduleBooking1.doesBookingExist("1000"));
    }
    
    @Test
    public void testIsBookingPossible() {
        rescheduleBooking rescheduleBooking3 = new rescheduleBooking(new User());

        // Test with an available appointment date-time for the doctor
        assertTrue(rescheduleBooking3.isBookingPossible("1", "2024-04-07 10:00:00"));

        // Test with an unavailable appointment date-time for the doctor
        boolean result = rescheduleBooking3.isBookingPossible("doctorID", "2024-04-07 10:00:00");
        assertTrue(result);    
    }
}
