import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.*;

public class LoginPageTest {

    @Test
    public void testEnterDoctorButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getEnterDoctorButton().doClick();
        assertTrue(checkAccessLog("Doctor Entry clicked"));
    }

    @Test
    public void testEnterPatientButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getEnterPatientButton().doClick();
        assertTrue(checkAccessLog("Patient Entry clicked"));
    }
    @Test
    public void testChangeDoctorButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getChangeDoctorButton().doClick();
        assertTrue(checkAccessLog("Change doctor clicked"));
    }
    
    @Test
    public void testArrangeBookingButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getArrangeBookingButton().doClick();
        assertTrue(checkAccessLog("Booking Entry clicked"));
    }
    
    @Test
    public void testRemoveBookingButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getRemoveBookingButton().doClick();
        assertTrue(checkAccessLog("Remove booking clicked"));
    }
    
    @Test
    public void testRescheduleBookingButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getRescheduleBookingButton().doClick();
        assertTrue(checkAccessLog("Reschedule booking clicked"));
    }
    
    @Test
    public void testViewBookingsButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getViewBookingsButton().doClick();
        assertTrue(checkAccessLog("View bookings clicked"));
    }
    
    @Test
    public void testViewDoctorsButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getViewDoctorsButton().doClick();
        assertTrue(checkAccessLog("View doctors clicked"));
    }
    
    @Test
    public void testViewPatientsButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getViewPatientsButton().doClick();
        assertTrue(checkAccessLog("View Patients clicked"));
    }
    
    @Test
    public void testMessagesPageButtonActionPerformed() {
        // Create a valid User object for testing
        User user = new User(); 
        LoginPage page = new LoginPage(user);
        page.getMessagesPageButton().doClick();
        assertTrue(checkAccessLog("Messages clicked"));
    }

   

    
    // Helper method to check if the activity is logged in the AccessLog
    private boolean checkAccessLog(String activity) {
        
        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
        final String USERNAME = "co398";
        final String PASSWORD = "x9plodh";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM AccessLog WHERE Activity = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, activity);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next(); // If any entry with the activity is found, return true
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to check AccessLog: " + ex.getMessage());
        }
        return false; // Return false if an error occurs or no matching entry is found
    }
}
