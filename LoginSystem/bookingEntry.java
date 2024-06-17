import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bookingEntry extends JFrame {
    private User user;

    // Initialize necessary labels, textfields, and buttons.
    private JLabel patientidLabel, appointmentdateLabel, appointmentTimeLabel;
    public static JTextField appointmentdateField, appointmentTimeField;
    public static JButton submitButton, backButton;
    public static Boolean successfulAdd = false;
    public static JOptionPane optionSuccess;
    public String appointmentdateTime;
    private String patientUsername;
    private JComboBox<String> patientList;
    public String patientid;
    public String doctorid;
    public String doctorUsername;

    public bookingEntry(User user) {
        this.user = user;
        setTitle("Booking entry form");
        setSize(500, 500); //Setting initial frame size
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closing 
        setLocationRelativeTo(null); //Centering the frame
        

        // Creation of objects.
        patientidLabel = new JLabel("Patient id:");
        appointmentdateLabel = new JLabel("Appointment date:");
        appointmentTimeLabel = new JLabel("Appointment time:");

        appointmentdateField = new JTextField(25);
        appointmentTimeField = new JTextField(25);
        

        backButton = new JButton("Back"); // Back button creation
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();//dispose the current frame
                // Assuming LoginPage is another class you've implemented
                new LoginPage(user);
            }
        });

        submitButton = new JButton("Submit"); //Submit button creation
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (appointmentdateField.getText().isEmpty() || appointmentTimeField.getText().isEmpty()) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Grabs input values to use for future database inputs.
                    patientid = String.valueOf(patientList.getSelectedItem()).substring(0, String.valueOf(patientList.getSelectedItem()).indexOf(":"));
                    appointmentdateTime = appointmentdateField.getText() + " " + appointmentTimeField.getText();
                    doctorid = getDoctorID(patientid);
                    Access access = new Access();
                    access.addToAccessLog(user.getUsername(), "booking created");
                    // Reset fields after submission to allow user to submit in quick succession.
                    appointmentdateField.setText("");
                    appointmentTimeField.setText("");

                    // Connection to the database
                    if(!isBookingPossible(doctorid, appointmentdateTime)){
                        JOptionPane.showMessageDialog(null, "Doctor is already booked at this time.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } 
                        
                    try {
                        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
                        final String USERNAME = "co398";
                        final String PASSWORD = "x9plodh";
                        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql = "INSERT INTO Bookings (patientid, doctorid, appointmentdatetime) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                            preparedStatement.setString(1, patientid);
                            preparedStatement.setString(2, doctorid);
                            preparedStatement.setString(3, appointmentdateTime);
                            
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
                            preparedStatement.setString(1, user.getUsername());
                            preparedStatement.setString(2, patientUsername);
                            preparedStatement.setString(3, "Your appointment has been booked!");
                            

                            preparedStatement.executeUpdate();
                            
                        }
                    Connection conn4 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                    String sql4 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                    doctorUsername = getDoctorUsername(doctorid);
                    try (PreparedStatement preparedStatement = conn4.prepareStatement(sql4)) {
                        preparedStatement.setString(1, user.getUsername());
                        preparedStatement.setString(2, doctorUsername);
                        preparedStatement.setString(3, "You have been assigned a new appointment");
                        preparedStatement.executeUpdate();
                        successfulAdd = true;
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
                    } catch (SQLException ex) {
                        ex.printStackTrace(); // Log the exception details
                        System.out.println("Database connection failed");
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

        patientList = new JComboBox<>();
        populatePatientList();

        // Add components to panel using GridBagConstraints
        // Everything added by row, with the constraints shown above to format it

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        panel.add(patientList, gbc);

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
        panel.add(submitButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    private void populatePatientList() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT patientid, firstName FROM Patients"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String patientid = resultSet.getString("patientid");
                    String patientName = resultSet.getString("firstName");
                    patientList.addItem(patientid + ": " + patientName); // Add each patient to the dropdown
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to populate patient list");
        }
    }

    private String getDoctorUsername(String doctorid) {
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

    private String getDoctorID(String patientid) {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT doctorid FROM Patients WHERE patientid = ?"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, patientid);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getString("doctorid");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to get doctor id");
        }
        return null;
    }

    private boolean isBookingPossible(String doctorID, String appointmentDateTime) {
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
                if(resultSet.next() == false){
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to check booking availability");
        }
        return false;
    }
}
