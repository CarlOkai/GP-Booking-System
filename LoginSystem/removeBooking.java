import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class removeBooking extends JFrame {
    private User user;

    public static void main(String[] args) {
        User user = new User();
        user.username = "tobi_s";
        new removeBooking(user);
    }

    // Initialize necessary labels, textfields, and buttons.
    private JLabel bookingIDLabel;
    public static JTextField bookingIDField;
    public static JButton removeButton, backButton;
    public static Boolean successfullRemove = false;
    public static JOptionPane optionSuccess;
    public static JComboBox<String> bookingIDdrop;
    public String[] doctorPatientID;
    private String patientUsername;
    private String doctorUsername;

    public removeBooking(User user) {
        setTitle("Remove Booking form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creation of objects.
        bookingIDLabel = new JLabel("booking id:");
        // bookingIDField = new JTextField(25);
        bookingIDdrop = new JComboBox<>();
        populateBookingDropdown();
        this.user = user;
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage(user);
            }
        });

        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ((String.valueOf(bookingIDdrop.getSelectedItem()).strip()).isEmpty()) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Grabs input values to use for future database inputs.

                    String bID = String.valueOf(bookingIDdrop.getSelectedItem());
                    String bookingID = bID.strip();

                    // Reset fields after submission to allow user to submit in quick succession.
                    bookingIDdrop.setSelectedIndex(0);
                    // Connection to the database
                    if (!doesBookingExist(bookingID)) {
                        JOptionPane.showMessageDialog(null, "Booking does not exist.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    doctorPatientID = getDoctorPatientID(bookingID);
                    String doctorid = doctorPatientID[0];
                    String patientid = doctorPatientID[1];
                    doctorUsername = getDoctorUsername(doctorid);
                    patientUsername = getPatientUsername(patientid);
                    bookingIDdrop.setSelectedIndex(0);
                    try {
                        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
                        final String USERNAME = "co398";
                        final String PASSWORD = "x9plodh";
                        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql = "DELETE FROM Bookings WHERE bookingID = ?"; // sql statement ran to find correct
                                                                                 // data
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                            preparedStatement.setString(1, bookingID);
                            preparedStatement.executeUpdate();
                        }
                        conn.close();
                        Connection conn2 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql2 = "INSERT INTO messages (sender_username, receiver_username, Message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn2.prepareStatement(sql2)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, doctorUsername);
                            preparedStatement.setString(3, "Your booking has been removed. Booking ID: " + bookingID);

                            preparedStatement.executeUpdate();
                        }

                        Connection conn3 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql3 = "INSERT INTO messages (sender_username, receiver_username, Message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn3.prepareStatement(sql3)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, patientUsername);
                            preparedStatement.setString(3, "Your booking has been removed. Booking ID: " + bookingID);

                            preparedStatement.executeUpdate();
                            successfullRemove = true;
                            JDialog dialog = new JDialog();
                            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                            // Create a timer to close the dialog after 3 seconds
                            Timer timer = new Timer(2000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // Close the dialog
                                    dialog.dispose();
                                }
                            });
                            timer.setRepeats(false); // Set to false to only fire once
                            timer.start();
                            bookingIDdrop.removeAllItems();
                            populateBookingDropdown();
                            optionSuccess.showMessageDialog(dialog, "Booking removed successfully!");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace(); // Log the exception details
                        System.out.println("Database connection failed");
                    }
                }
            }
        });

        // Use of GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout()); // creation of panel
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints, specified below
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Add components to panel using GridBagConstraints
        // Everything added by row, with the constraints shown above to format it

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(bookingIDLabel, gbc);

        gbc.gridx = 1;
        panel.add(bookingIDdrop, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(removeButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    public String[] getDoctorPatientID(String bookingid) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            doctorPatientID = new String[2];
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT doctorid, patientid FROM Bookings WHERE bookingid = ?"; // sql statement ran to find
                                                                                         // correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, bookingid);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                doctorPatientID[0] = resultSet.getString("doctorid");
                doctorPatientID[1] = resultSet.getString("patientid");
                return doctorPatientID;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to get doctor id");
        }
        return null;
    }

    public void populateBookingDropdown() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT bookingID FROM Bookings"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String bookID = resultSet.getString("bookingID");
                    bookingIDdrop.addItem(bookID); // Add each doctor name to the dropdown
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to populate booking dropdown");
        }
    }

    public static boolean doesBookingExist(String bookingid) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT * FROM Bookings WHERE bookingid = ?"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, bookingid);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to get booking id");
        }
        return false;
    }

    public String getDoctorUsername(String doctorid) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT username FROM Doctors WHERE doctorid = ?"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, doctorid);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getString("username");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to get doctor username");
        }
        return null;
    }

    public String getPatientUsername(String patientID) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT username FROM Patients WHERE patientid = ?"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, patientID);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getString("username");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to get patient username");
        }
        return null;
    }

    // Getter method for bookingIDField
    public JTextField getBookingIDField() {
        return bookingIDField;
    }

    // Getter method for removeButton
    public JButton getRemoveButton() {
        return removeButton;
    }

    // Getter method for removeButton
    public String[] getDoctorPatientID() {
        return doctorPatientID;
    }

    // Getter method for backButton
    public JButton getBackButton() {
        return backButton;
    }

    // Getter method for successfullRemove
    public Boolean getSuccessfullRemove() {
        return successfullRemove;
    }

    // Getter method for optionSuccess
    public JOptionPane getOptionSuccess() {
        return optionSuccess;
    }

    // Getter method for bookingIDdrop
    public JComboBox<String> getBookingIDDrop() {
        return bookingIDdrop;
    }
}
