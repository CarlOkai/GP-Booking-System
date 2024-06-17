import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WelcomePage extends JFrame {
    public static JTextField uinput = new JTextField(20);
    public static JTextField pinput = new JPasswordField(20);
    public static Boolean successLogin = false;
    public static JButton submit = new JButton("Log In");
    public static MessagesPage messagesPage;

    // Creating main frame
    public void initialize() {

        // Create the frame
        JFrame frame = new JFrame("Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(104, 108, 115)); // Set background color

        // Maximize frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // GridBagConstraints for the title label
        GridBagConstraints titlegb = new GridBagConstraints();
        titlegb.gridwidth = GridBagConstraints.REMAINDER;
        titlegb.gridx = 0;
        titlegb.gridy = 0;

        // Creating the title label
        JLabel label = new JLabel("Welcome to AdminBuddy");
        label.setFont(new Font("Arial", Font.PLAIN, 40));
        label.setForeground(Color.BLACK);

        // Labels and textfields for username and password
        JLabel ulabel = new JLabel("Username:");
        ulabel.setFont(new Font("Arial", Font.PLAIN, 15));
        ulabel.setForeground(Color.BLACK);

        uinput.setFont(new Font("Arial", Font.PLAIN, 15));
        uinput.setSize(150, 20);
        uinput.setForeground(Color.BLACK);
        pinput.setForeground(Color.BLACK);
        JLabel plabel = new JLabel("Password:");
        plabel.setForeground(Color.BLACK);
        plabel.setFont(new Font("Arial", Font.PLAIN, 15));
        pinput.setFont(new Font("Arial", Font.PLAIN, 15));
        pinput.setSize(150, 20);
        JLabel incorrect = new JLabel("Incorrect username or password");
        incorrect.setForeground(Color.BLACK);
        incorrect.setFont(new Font("Arial", Font.PLAIN, 18));
        GridBagConstraints incorrectgb = new GridBagConstraints();

        // Creating and configuring the login button
        submit.setFont(new Font("Arial", Font.PLAIN, 15));
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(incorrect);
                frame.revalidate();
                String username = uinput.getText();
                String password = pinput.getText();
                User user = getAuthenticatedUser(username, password);

                // If authentication is successful, open the LoginPage, passing in the user
                if (user != null) {
                    successLogin = true;
                    Access access = new Access();
                    access.addToAccessLog(username, "Log in");
                    frame.remove(incorrect);
                    frame.revalidate();
                    System.out.println("Valid user");
                    messagesPage = new MessagesPage(user);
                    messagesPage.initialize(user); // Pass the authenticated User object to initialize
                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    loginFrame.dispose();
                } else {
                    pinput.setText("");
                    frame.add(incorrect, incorrectgb);
                    frame.revalidate();
                }
            }
        });

        GridBagConstraints uinputgb = new GridBagConstraints();
        GridBagConstraints ulabelgb = new GridBagConstraints();
        GridBagConstraints pinputgb = new GridBagConstraints();
        GridBagConstraints plabelgb = new GridBagConstraints();
        GridBagConstraints submitgb = new GridBagConstraints();

        uinputgb.gridx = 1;
        uinputgb.gridy = 1;
        uinputgb.insets = new Insets(10, 0, 10, 0);
        ulabelgb.gridx = 0;
        ulabelgb.gridy = 1;
        ulabelgb.insets = new Insets(10, 0, 10, 10);
        pinputgb.gridx = 1;
        pinputgb.gridy = 2;
        pinputgb.insets = new Insets(10, 0, 10, 0);
        plabelgb.gridx = 0;
        plabelgb.gridy = 2;
        plabelgb.insets = new Insets(10, 0, 10, 10);
        submitgb.gridx = 5;
        submitgb.gridy = 5;
        submitgb.insets = new Insets(10, 10, 50, 10);
        incorrectgb.gridx = 1;
        incorrectgb.gridy = 5;
        incorrectgb.insets = new Insets(10, 10, 10, 10);

        // Adding components to the frame
        frame.add(uinput, uinputgb);
        frame.add(pinput, pinputgb);
        frame.add(label, titlegb);
        frame.add(ulabel, ulabelgb);
        frame.add(plabel, plabelgb);
        frame.add(plabel, plabelgb);
        frame.add(submit, submitgb);

        // Setting frame properties, making it visible
        frame.pack();
        frame.setSize(1000, 625);
        frame.setVisible(true);
    }

    // Authentication method against database
    private User getAuthenticatedUser(String username, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
        final String USERNAME = "co398";
        final String PASSWORD = "x9plodh";

        try {
            // Establish connection to database
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Execute the SQL query to authenticate the user in the admins table
            String sqlAdmins = "SELECT * FROM Admins WHERE username=? AND password=?";
            PreparedStatement preparedStatementAdmins = conn.prepareStatement(sqlAdmins);
            preparedStatementAdmins.setString(1, username);
            preparedStatementAdmins.setString(2, password);

            // If a matching user is found in the admins table, print message
            ResultSet resultSetAdmins = preparedStatementAdmins.executeQuery();

            if (resultSetAdmins.next()) {
                user = new User();
                user.username = resultSetAdmins.getString("username");
                user.password = resultSetAdmins.getString("password");
            } else {
                // If not found in the admins table, check the doctors table
                String sqlDoctors = "SELECT * FROM Doctors WHERE username=? AND password=?";
                PreparedStatement preparedStatementDoctors = conn.prepareStatement(sqlDoctors);
                preparedStatementDoctors.setString(1, username);
                preparedStatementDoctors.setString(2, password);

                // If a matching user is found in the doctors table, print message
                ResultSet resultSetDoctors = preparedStatementDoctors.executeQuery();

                if (resultSetDoctors.next()) {
                    user = new User();
                    user.username = resultSetDoctors.getString("username");
                    user.password = resultSetDoctors.getString("password");
                } else {
                    // Handle case when username and password are not found in either table
                }

                preparedStatementDoctors.close();
            }

            preparedStatementAdmins.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // Print exception details
            System.out.println("Database connection failed");
        }

        return user;
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
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.initialize();

    }
}