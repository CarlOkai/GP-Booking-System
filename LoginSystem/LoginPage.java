import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    public User user;
    public static doctorEntry docEntry;
    public JButton enterDoctorButton;
    public JButton enterPatientButton;
    public JButton changeDoctorButton;
    public JButton arrangeBookingButton;
    public JButton removeBookingButton;
    public JButton reschduleBookingButton;
    public JButton viewBookingsButton;
    public JButton viewDoctorsButton;
    public JButton viewPatientsButton;
    public JButton messagesPageButton;
  

//Getter methods in order to run tests

    // Getter method for enterDoctorButton
    public JButton getEnterDoctorButton() {
        return enterDoctorButton;
    }
    // Getter method for enterPatientButton
    public JButton getEnterPatientButton() {
        return enterPatientButton;
    }
    // Getter method for changeDoctorButton
public JButton getChangeDoctorButton() {
    return changeDoctorButton;
}

// Getter method for arrangeBookingButton
public JButton getArrangeBookingButton() {
    return arrangeBookingButton;
}

// Getter method for removeBookingButton
public JButton getRemoveBookingButton() {
    return removeBookingButton;
}

// Getter method for rescheduleBookingButton
public JButton getRescheduleBookingButton() {
    return reschduleBookingButton;
}

// Getter method for viewBookingsButton
public JButton getViewBookingsButton() {
    return viewBookingsButton;
}

// Getter method for viewDoctorsButton
public JButton getViewDoctorsButton() {
    return viewDoctorsButton;
}

// Getter method for viewPatientsButton
public JButton getViewPatientsButton() {
    return viewPatientsButton;
}

// Getter method for messagesPageButton
public JButton getMessagesPageButton() {
    return messagesPageButton;
}
    private Access access;

    public static void main(String[] args) {
        User user = null;
        new LoginPage(user);
        
    }

    public LoginPage(User user) {

        
        setTitle("LoginPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        access = new Access();

        String username = user.getUsername(); // grabs username
        // Welcome Label inclding persons username
        JLabel welcomeLabel = new JLabel("Welcome " + username); // Will include name in future.
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setPreferredSize(new Dimension(600, 50));

        // Creating buttons for each functionality to be added.
        enterDoctorButton = new JButton("Enter a New Doctor");
        enterPatientButton = new JButton("Enter a New Patient");
        changeDoctorButton = new JButton("Change Patient's Doctor");
        arrangeBookingButton = new JButton("Arrange Booking for a Patient");
        removeBookingButton = new JButton("Remove Booking");
        reschduleBookingButton = new JButton("Reschedule Booking");
        viewBookingsButton = new JButton("View Bookings");
        viewDoctorsButton = new JButton("View All Doctors");
        viewPatientsButton = new JButton("View All Patients");
        messagesPageButton = new JButton("Messages Page");

        // Adding all action listeners to buttons will include functionality with more
        // updates.
        enterDoctorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Closes the LoginPageGUI and opens Doctor Entry GUI.
                dispose();
                access.addToAccessLog(username, "Doctor Entry clicked");
                new doctorEntry(user);
            }
        });

        enterPatientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Closes LoginPageGUI and opens Patient Entry GUI.
                dispose();                
                access.addToAccessLog(username, "Patient Entry clicked");
                new patientEntry1(user);
            }
        });

        changeDoctorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                changeDoctor doctorFrame = new changeDoctor(user);                
                access.addToAccessLog(username, "Change doctor clicked");
                doctorFrame.setVisible(true);
            }
        });

        arrangeBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "Booking Entry clicked");
                new bookingEntry(user);
            }
        });

        removeBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "Remove booking clicked");
                new removeBooking(user);
            }
        });
        reschduleBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "Reschedule booking clicked");
                new rescheduleBooking(user);
            }
        });
        viewBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "View bookings clicked");
                new viewBookings(user);
            }
        });
        viewDoctorsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "View doctors clicked");
                new viewDoctors1(user);
            }
        });

        viewPatientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                access.addToAccessLog(username, "view Patients clicked");
                new viewPatients(user);
            }
        });

        messagesPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Closes the LoginPageGUI and opens Messages Page GUI.
                System.out.println("Valid user");
                MessagesPage messagesPage = new MessagesPage(user);
                messagesPage.initialize(user); // Pass the authenticated User object to initialize
                access.addToAccessLog(username, "Messages clicked");
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                loginFrame.dispose();
            }
        });

        // Apply Grid layout to GUI, for formatting
        JPanel panel = new JPanel(new GridLayout(10, 1));

        // Adding Labels & buttons to GUI
        panel.add(welcomeLabel);
        panel.add(enterDoctorButton);
        panel.add(enterPatientButton);
        panel.add(changeDoctorButton);
        panel.add(arrangeBookingButton);
        panel.add(removeBookingButton);
        panel.add(reschduleBookingButton);
        panel.add(viewBookingsButton);
        panel.add(viewDoctorsButton);
        panel.add(viewPatientsButton);
        panel.add(messagesPageButton);

        getContentPane().add(panel);

        // Set the size of the frame to full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setResizable(false); // Prevent resizing of the window

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Make everything visible in GUI
        setVisible(true);
    }
}