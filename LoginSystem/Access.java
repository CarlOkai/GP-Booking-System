import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Access {

    private static final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
    private static final String USERNAME = "co398";
    private static final String PASSWORD = "x9plodh";

    public void addToAccessLog(String username, String activity) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO AccessLog (Username, Activity) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, activity);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to add entry to AccessLog: " + ex.getMessage());
        }
    }
}
