import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class bookingEntryTests {

    final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
    final String USERNAME = "co398";
    final String PASSWORD = "x9plodh";

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("bookingEntryTests");
    }
   
    @Test
    public void testBookingEntry() {
        User user = new User();
        bookingEntry bookingEntry = new bookingEntry(user);
        bookingEntry.patientid = "1";
        bookingEntry.appointmentdateField.setText("2021-12-12");
        bookingEntry.appointmentTimeField.setText("12:00");
        bookingEntry.submitButton.doClick();


        assertTrue(isDataSubmittedToDatabase(DB_URL, USERNAME, PASSWORD));
        
    }

    private boolean isDataSubmittedToDatabase(String dbUrl, String username, String password) {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password);
             Statement statement = conn.createStatement()) {

            // Check if the inserted data exists in the database
            String sql = "SELECT * FROM Bookings WHERE patientid = '1' AND doctorid = '1' AND appointmentdatetime = '2021-12-12 12:00:00'";
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next(); // Return true if data exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}