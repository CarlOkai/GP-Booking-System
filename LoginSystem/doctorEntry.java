import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class doctorEntry extends JFrame {
    private User user;

    public static void main(String[] args) {
        User user = new User();
        user.username = "tobi_s";
        user.password = "pass456";
        new doctorEntry(user);
    }

    // Initialize necessary labels, textfields, and buttons.
    private JLabel firstNameLabel, lastNameLabel, phoneLabel, emailLabel, addressLabel, usernameLabel, passwordLabel,
            workingHoursLabel, backgroundLabel, notesLabel;
    public static JTextField firstNameField, lastNameField, phoneField, emailField, addressField, usernameField,
            passwordField, workingHoursField, backgroundField;
    public static JTextArea notesField;
    public static JButton submitButton, backButton;
    public static Boolean successfulAdd = false;
    public static JOptionPane optionSuccess;

    public doctorEntry(User user) {
        setTitle("Doctor Entry Form");
        // Set the window to open in full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window on the screen

        // Creation of objects.
        firstNameLabel = new JLabel("First Name:");
        lastNameLabel = new JLabel("Last Name:");
        phoneLabel = new JLabel("Phone Number:");
        emailLabel = new JLabel("Email:");
        addressLabel = new JLabel("Address:");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        workingHoursLabel = new JLabel("Working Hours:");
        backgroundLabel = new JLabel("Background:");
        notesLabel = new JLabel("Notes:");

        firstNameField = new JTextField(25);
        lastNameField = new JTextField(25);
        phoneField = new JTextField(25);
        emailField = new JTextField(25);
        addressField = new JTextField(25);
        usernameField = new JTextField(25);
        passwordField = new JTextField(25);
        workingHoursField = new JTextField(25);
        backgroundField = new JTextField(25);
        notesField = new JTextArea(2, 25);
        notesField.setRows(2);
        notesField.setLineWrap(true);

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
                        || phoneField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        addressField.getText().isEmpty() || workingHoursField.getText().isEmpty()
                        || backgroundField.getText().isEmpty() || notesField.getText().isEmpty()) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Grabs input values to use for future database inputs.
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String phone = phoneField.getText();
                    String email = emailField.getText();
                    String address = addressField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String workingHours = workingHoursField.getText();
                    String background = backgroundField.getText();
                    String notes = notesField.getText();

                    // Reset fields after submission to allow user to submit in quick succession.
                    firstNameField.setText("");
                    lastNameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                    addressField.setText("");
                    usernameField.setText("");
                    passwordField.setText("");
                    workingHoursField.setText("");
                    backgroundField.setText("");
                    notesField.setText("");

                    // Connection to the database

                    try {
                        final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
                        final String USERNAME = "co398";
                        final String PASSWORD = "x9plodh";
                        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql = "INSERT INTO Doctors (phonenumber, email, firstName, lastname, address, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                            preparedStatement.setString(1, phone);
                            preparedStatement.setString(2, email);
                            preparedStatement.setString(3, firstName);
                            preparedStatement.setString(4, lastName);
                            preparedStatement.setString(5, address);
                            preparedStatement.setString(6, username);
                            preparedStatement.setString(7, password);

                            preparedStatement.executeUpdate();
                        }
                        conn.close();
                        Connection conn2 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        String sql2 = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = conn2.prepareStatement(sql2)) {
                            preparedStatement.setString(1, user.username);
                            preparedStatement.setString(2, username);
                            preparedStatement.setString(3, "You have been added to the system as a doctor.");

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
                            optionSuccess.showMessageDialog(dialog, "Doctor entry submitted successfully!");

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
        panel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(workingHoursLabel, gbc);

        gbc.gridx = 1;
        panel.add(workingHoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(backgroundLabel, gbc);

        gbc.gridx = 1;
        panel.add(backgroundField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(notesLabel, gbc);

        gbc.gridx = 1;
        panel.add(notesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(submitButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }
}
