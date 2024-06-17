import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class rescheduleBooking extends JFrame {


    public static void main(String[] args) {
        User user = new User();
        user.username = "tobi_s";
        new rescheduleBooking(user);
    }

    // Initialize necessary labels, textfields, and buttons.
    private JLabel bookingIDLabel, appointmentdateLabel, appointmentTimeLabel;
    public static JTextField bookingIDField, appointmentdateField, appointmentTimeField;
    public static JButton rescheduleButton, backButton;
    public static Boolean successfullReschedule = false;
    public static JOptionPane optionSuccess;
    public String[] doctorPatientID;
    private String doctorUsername;
    private String patientUsername;

    public rescheduleBooking(User user) {
        setTitle("Reschedule Booking form");
        setSize(500, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creation of objects.
        bookingIDLabel = new JLabel("booking id:");
        bookingIDField = new JTextField(25);

        appointmentdateLabel = new JLabel("New Appointment date:");
        appointmentTimeLabel = new JLabel("New Appointment time:");

        appointmentdateField = new JTextField(25);
        appointmentTimeField = new JTextField(25);
        
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Assuming LoginPage is another class you've implemented
                new LoginPage(user);
            }
        });

        rescheduleButton = new JButton("Reschedule");
        rescheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bookingIDField.getText().isEmpty() || appointmentdateField.getText().isEmpty() || appointmentTimeField.getText().isEmpty()) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Grabs input values to use for future database inputs.
                    String bookingID = bookingIDField.getText();
                    String appointmentdate = appointmentdateField.getText();
                    String appointmentTime = appointmentTimeField.getText();
                    String appointmentDateTime = appointmentdate + " " + appointmentTime;
                    // Reset fields after submission to allow user to submit in quick succession.
                    bookingIDField.setText("");
                    appointmentdateField.setText("");
                    appointmentTimeField.setText("");
                    // Connection to the database
                    if(!doesBookingExist(bookingID)){
                        JOptionPane.showMessageDialog(null, "Booking does not exist.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    doctorPatientID = getDoctorPatientID(bookingID);
                    String doctorid = doctorPatientID[0];
                    String patientid = doctorPatientID[1];
                    if(!isBookingPossible(doctorid, appointmentdate)){
                        JOptionPane.showMessageDialog(null, "Booking not possible.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
                        final String USERNAME = "co398";
                        final String PASSWORD = "x9plodh";
                        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql = "UPDATE Bookings SET appointmentDateTime = ? WHERE bookingID = ?"; // sql statement to update data
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                            preparedStatement.setString(1, appointmentDateTime); // replace with your new appointment date and time
                            preparedStatement.setString(2, bookingID);
                            preparedStatement.executeUpdate();
                        }
                        conn.close();
                        Connection conn2 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql2 = "SELECT username FROM Patients WHERE patientID = ?";
                        try{
                            PreparedStatement preparedStatement = conn2.prepareStatement(sql2);
                            preparedStatement.setString(1, patientid);
                            ResultSet rs = preparedStatement.executeQuery();
                            rs.next();
                            patientUsername = rs.getString("username");
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace(); // Log the exception details
                            System.out.println("Database connection failed");
                        }
                        conn.close();
                         
                        Connection conn3 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql3 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn3.prepareStatement(sql3)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, patientUsername);
                            preparedStatement.setString(3, "Your appointment has been rescheduled to " + appointmentDateTime + " by " + user.username);
                        
                            preparedStatement.executeUpdate();
                        }
                        Connection conn4 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql4 = "SELECT username FROM Doctors WHERE doctorID = ?";
                        try{
                            PreparedStatement preparedStatement = conn4.prepareStatement(sql4);
                            preparedStatement.setString(1, doctorid);
                            ResultSet rs = preparedStatement.executeQuery();
                            rs.next();
                            doctorUsername = rs.getString("username");
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace(); // Log the exception details
                            System.out.println("Database connection failed");
                        }
                        conn.close();
                        Connection conn5 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql5 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn5.prepareStatement(sql5)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, doctorUsername);
                            preparedStatement.setString(3, "Appointment with patient " + patientUsername + " has been rescheduled to " + appointmentDateTime + " by " + user.username);
                        
                            preparedStatement.executeUpdate();
                            successfullReschedule = true;
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
                            optionSuccess.showMessageDialog(dialog, "Booking successful!");
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
        panel.add(bookingIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(appointmentdateLabel, gbc);

        gbc.gridx = 1;
        panel.add(appointmentdateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(appointmentTimeLabel, gbc);

        gbc.gridx = 1;
        panel.add(appointmentTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(rescheduleButton, gbc);

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
            String sql = "SELECT doctorid, patientid FROM Bookings WHERE bookingid = ?"; // sql statement ran to find correct data
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

    public boolean doesBookingExist(String bookingid){
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

    public boolean isBookingPossible(String doctorID, String appointmentDateTime) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT appointmentdatetime FROM Bookings WHERE doctorid = ? && appointmentdatetime = ?"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, doctorID);
                preparedStatement.setString(2, appointmentDateTime);
                ResultSet resultSet = preparedStatement.executeQuery();
                return !resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to check booking availability");
        }
        return false;
    }

    // Getter method for rescheduleButton
    public JButton getRescheduleButton() {
        return rescheduleButton;
    }

    // Getter method for removeButton
    public String[] getDoctorPatientID() {
        return doctorPatientID;
    }
}
