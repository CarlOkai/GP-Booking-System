import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChangeDoctorTest {

    @Test
    public void testUpdateDoctor() {
        // Create a mock User object
        User user = new User();


        // Create an instance of changeDoctor
        changeDoctor doctorChanger = new changeDoctor(user);

        // Set the patient ID and new doctor ID
        doctorChanger.patientIdField.setText("1"); // Assuming patient ID 1 exists
        doctorChanger.doctorIdField.setText("10"); // Set new doctor ID

        // Call the updateDoctor method
        doctorChanger.updateDoctor();

        // Retrieve the new doctor ID for the patient
        int newDoctorId = doctorChanger.newDoctorId;

        // Check if the new doctor ID has been updated successfully
        assertEquals(10, newDoctorId);
    }
}
