import javax.swing.*;

import org.junit.internal.runners.JUnit38ClassRunner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MessagesPage extends JFrame {
    public static JButton loginPage;
    public static LoginPage actionsPage;
    public static JButton logoutButton;

    public MessagesPage(User user) {
        initialize(user);
    }
public void initialize(User user) {
    // Set the layout manager of the frame's content pane to BorderLayout
    getContentPane().setLayout(new BorderLayout());

    JPanel messagePanel = new JPanel();
    JTextArea messagesTextArea = new JTextArea(45, 150);
    messagesTextArea.setEditable(false);
    messagePanel.setPreferredSize(new Dimension(1000, 800));

    final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
    final String USERNAME = "co398";
    final String PASSWORD = "x9plodh";

    try {
        // Connecting to database
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        String sql = "SELECT sender_username, receiver_username, message, timestamp FROM messages WHERE receiver_username=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String sender = resultSet.getString("sender_username");
                String receiver = resultSet.getString("receiver_username");
                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("Timestamp");
                messagesTextArea.append("From: " + sender + "\n");
                messagesTextArea.append("To: " + receiver + "\n");
                messagesTextArea.append("Message: " + message + "\n");
                messagesTextArea.append("Timestamp: " + timestamp + "\n\n");
            }
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database connection failed");
    }

    messagePanel.add(new JScrollPane(messagesTextArea));
    logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String username = user.getUsername();// get username
            logAccess(username);
            dispose(); // Close the current MessagesPage
        }
    });
    loginPage = new JButton("Main Menu");
    loginPage.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dispose(); // Close the current MessagesPage
            actionsPage = new LoginPage(user); // Create a new LoginPage
            actionsPage.setVisible(true); // Make the LoginPage visible
        }
    });

    add(messagePanel, BorderLayout.CENTER); // Add messagePanel to the center of the content pane
    add(loginPage, BorderLayout.NORTH);
    add(logoutButton, BorderLayout.SOUTH); // Add the button to the frame
    setTitle("Messages Page");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setVisible(true);
}

    private void logAccess(String username) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            // Establish connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // SQL query to insert a log entry into the AccessLog table
            String sql = "INSERT INTO AccessLog (Username) VALUES (?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            }

            // Close resources
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Database connection failure");
        }
    }

    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            new MessagesPage(new User()); // Replace with an actual User object
        });
    }
}
