import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class patientEntry1 extends JFrame {
    private JLabel firstNameLabel, lastNameLabel, usernameLabel, phoneNumberLabel, emailLabel, passwordLabel,
            addressLabel;
    // public instead of private to make tests work
    public static JTextField firstNameField, lastNameField, usernameField, phoneNumberField, emailField, passwordField,
            addressField;
    public static JButton submitButton, backButton;
    public static Boolean successfulAdd = false;
    public static JOptionPane optionSuccess;
    private JComboBox<String> doctorDropdown;

    public static void main(String[] args) {
        User user = new User(); // Initialize user, for testing purposes
        user.username = "tobi_s"; // Set username to Admin, for testing purposes
        user.password = "pass456"; // Set password to password, for testing purposes
        SwingUtilities.invokeLater(() -> new patientEntry1(user).setVisible(true));
    }

    // Grabs the highest ID number for patients and adds 1 to the highest number to
    // prevent overlaps
    private int getNextPatientId() {
        int nextPatientId = 0;
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT MAX(patientid) AS max_patientid FROM Patients";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    nextPatientId = resultSet.getInt("max_patientid") + 1;
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to get next patient ID");
        }
        return nextPatientId;
    }

    public patientEntry1(User user) {
        setTitle("Patient Entry Form");
        setSize(500, 500); // Increased height to accommodate larger notes field
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creation of objects.
        firstNameLabel = new JLabel("First Name:");
        lastNameLabel = new JLabel("Last Name");
        phoneNumberLabel = new JLabel("Phone Number:");
        emailLabel = new JLabel("Email:");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        addressLabel = new JLabel("Address:");

        firstNameField = new JTextField(25);
        lastNameField = new JTextField(25);
        phoneNumberField = new JTextField(25);
        addressField = new JTextField(25);
        emailField = new JTextField(25);
        usernameField = new JTextField(25);
        passwordField = new JTextField(25);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Assuming LoginPage is another class you've implemented
                new LoginPage(user);
            }
        });

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
                        || phoneNumberField.getText().isEmpty() || addressField.getText().isEmpty()
                        || emailField.getText().isEmpty() ||
                        passwordField.getText().isEmpty() || addressField.getText().isEmpty()) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Grabs input values to use for future database inputs.
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    String email = emailField.getText();
                    String address = addressField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String docID = String.valueOf(doctorDropdown.getSelectedItem());
                    Integer docIDs = Integer.valueOf(docID.strip());
                    System.out.println(docIDs);

                    // Reset fields after submission to allow user to submit in quick succession.
                    firstNameField.setText("");
                    lastNameField.setText("");
                    phoneNumberField.setText("");
                    emailField.setText("");
                    addressField.setText("");
                    usernameField.setText("");
                    passwordField.setText("");

                    // Connection to the database
                    try {
                        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
                        final String USERNAME = "co398";
                        final String PASSWORD = "x9plodh";
                        int patientid = getNextPatientId();
                        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql = "INSERT INTO Patients (firstName, lastName, phoneNumber, email, address, username, password, doctorid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                            // preparedStatement.setInt(1, patientid);
                            preparedStatement.setString(1, firstName);
                            preparedStatement.setString(2, lastName);
                            preparedStatement.setString(3, phoneNumber);
                            preparedStatement.setString(4, email);
                            preparedStatement.setString(5, address);
                            preparedStatement.setString(6, username);
                            preparedStatement.setString(7, password);
                            preparedStatement.setInt(8, docIDs);
                            preparedStatement.executeUpdate();
                        }
                        conn.close();
                        String doctorUname = getDoctorUsername(docID);
                    
                        Connection conn2 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql2 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn2.prepareStatement(sql2)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, doctorUname);
                            preparedStatement.setString(3,
                                    "You have been allocated a new patient - Full name: " + firstName + lastName);
                            preparedStatement.executeUpdate();
                            successfulAdd = true;
                            JDialog dialog = new JDialog();
                            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                            conn2.close();
                        }
                        Connection conn3 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql3 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn3.prepareStatement(sql3)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, username);
                            preparedStatement.setString(3, "Congratulations " + firstName + ", you are now a patient on our system");
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
                            JOptionPane.showMessageDialog(dialog, "Patient entry submitted successfully!");
                            
                            conn3.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace(); // Log the exception details
                        System.out.println("Database connection failed");
                    }
                }
            }
        });

        // Initialize doctor dropdown menu
        doctorDropdown = new JComboBox<>();
        populateDoctorDropdown(); // Populate dropdown with doctors from database

        // Use of GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout()); // creation of panel
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints, specified below
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Add components to panel using GridBagConstraints
        // Everything added by row, with the constraints shown above to format it

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1;
        panel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("DoctorID:"), gbc);
        gbc.gridx = 1;
        panel.add(doctorDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(submitButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    private void populateDoctorDropdown() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT doctorID FROM Doctors"; // sql statement ran to find correct data
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String doctorName = resultSet.getString("doctorID");
                    doctorDropdown.addItem(doctorName); // Add each doctor name to the dropdown
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception details
            System.out.println("Failed to populate doctor dropdown");
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

}
