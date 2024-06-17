import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changeDoctor extends JFrame {
    public JLabel patientIdLabel;
    public JTextField patientIdField;
    public JLabel doctorIdLabel;
    public JTextField doctorIdField;
    public JButton updateButton;
    public int patientId;
    public int newDoctorId;
    public int oldDoctorId;
    public User user;
    public JButton backButton;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User user = new User(); // Provide the appropriate User object
            user.username = "tobi_s";//4testing
            new changeDoctor(user).setVisible(true);
        });
    }
    
    public changeDoctor(User user) {
        setTitle("Change Doctor");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Initialize components
        this.user = user;
        patientIdLabel = new JLabel("Patient ID:");
        patientIdField = new JTextField(10);
        doctorIdLabel = new JLabel("New Doctor ID:");
        doctorIdField = new JTextField(10);
        updateButton = new JButton("Update");
        backButton = new JButton("Back");


        // Add action listener to the update button
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                oldDoctorId = getOldDoctorId();
                updateDoctor();
                sendNewDoctorMessage();
                sendOldDoctorMessage();
                sendPatientMessage();
            }
        });

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage(user).setVisible(true);
            }
        });

        // Add components to the frame
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(patientIdLabel);
        panel.add(patientIdField);
        panel.add(doctorIdLabel);
        panel.add(doctorIdField);
        panel.add(updateButton);
        panel.add(backButton);

        getContentPane().add(panel);
    }

    public void updateDoctor() {
        String patientIdStr = patientIdField.getText();
        String doctorIdStr = doctorIdField.getText();
        
        if (patientIdStr.isEmpty() || doctorIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both patient ID and new doctor ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        patientId = Integer.parseInt(patientIdStr);
        newDoctorId = Integer.parseInt(doctorIdStr);

        // Perform the update operation
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "UPDATE Patients SET doctorid = ? WHERE patientid = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, newDoctorId);
                preparedStatement.setInt(2, patientId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Doctor updated successfully for patient with ID: " + patientId);
                } else {
                    System.out.println("No patient found with ID: " + patientId);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to update the doctor for patient with ID: " + patientId);
        }

        // Clear the text fields after update
        patientIdField.setText("");
        doctorIdField.setText("");
        setVisible(true);
    }

    public void sendNewDoctorMessage() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.username);
                preparedStatement.setString(2, getDoctorUsername(String.valueOf(newDoctorId)));
                preparedStatement.setString(3, "You have been assigned a new patient. Patient ID: " + patientId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Confirmation message sent to new Doctor");
                } else {
                    System.out.println("No patient found with ID: " + patientId);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to update the doctor for patient with ID: " + patientId);
        }
    }

    public int getOldDoctorId(){
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT doctorid FROM Patients WHERE patientid = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                int oldDoctorId;
                preparedStatement.setInt(1, Integer.parseInt(patientIdField.getText()));
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                oldDoctorId = resultSet.getInt("doctorid");
                return oldDoctorId;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to get old doctor ID");
        }
        return -1;
    }

    public void sendOldDoctorMessage() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.username);
                preparedStatement.setString(2, getDoctorUsername(String.valueOf(oldDoctorId)));
                preparedStatement.setString(3, "You are no longer the doctor of patientID: " + patientId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Confirmation message sent to old Doctor");
                } else {
                    System.out.println("No patient found with ID: " + patientId);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to update the doctor for patient with ID: " + patientId);
        }
    }

    public void sendPatientMessage() {
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO messages (sender_username, receiver_username, message) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.username);
                preparedStatement.setString(2, getPatientUsername(String.valueOf(patientId)));
                preparedStatement.setString(3, "You have been assigned a new Doctor. Patient ID: " + patientId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Confirmation message sent to patient");
                } else {
                    System.out.println("No patient found with ID: " + patientId);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to update the doctor for patient with ID: " + patientId);
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

    public String getPatientUsername(String patientID){
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

}