import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class testcases {
    @Test
    public void testusernameinput() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("john_doe");
        assertEquals(finPage.uinput.getText(), "john_doe");

    }

    @Test
    public void testpasswordinput() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.pinput.setText("password");
        assertEquals(finPage.pinput.getText(), "password");

    }

    @Test
    public void testloginSuccess() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("john_doe");
        finPage.pinput.setText("password123");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }

        finPage.submit.doClick();
        assertEquals(finPage.successLogin, true);

    }

    @Test
    public void testloginFail() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("john_do");
        finPage.pinput.setText("password12");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }

        finPage.submit.doClick();
        assertEquals(finPage.successLogin, false);

    }

    @Test
    public void testEmptyLogin() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("");
        finPage.pinput.setText("");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }

        finPage.submit.doClick();
        assertEquals(finPage.successLogin, false);

    }

    @Test
    public void testGoToMainMenu() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("john_doe");
        finPage.pinput.setText("password123");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }

        finPage.submit.doClick();
        MessagesPage testMessage = finPage.messagesPage;
        testMessage.loginPage.doClick();
        return;
    }

    @Test
    public void testGoToAddDoctor() {
        WelcomePage finPage = new WelcomePage();
        finPage.initialize();
        finPage.uinput.setText("john_doe");
        finPage.pinput.setText("password123");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }

        finPage.submit.doClick();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }
        MessagesPage testMessage = finPage.messagesPage;
        testMessage.loginPage.doClick();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }
        LoginPage testLog = testMessage.actionsPage;
        testLog.enterDoctorButton.doClick();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }
        doctorEntry dEntry = testLog.docEntry;
        dEntry.firstNameField.setText("Lewis");
        dEntry.lastNameField.setText("Hamilton");
        dEntry.phoneField.setText("077777777");
        dEntry.emailField.setText("lewishamilton@gmail.com");
        dEntry.addressField.setText("Lauda Drive");
        dEntry.usernameField.setText("goatLewis2222222243");
        dEntry.passwordField.setText("lewisPassword");
        dEntry.workingHoursField.setText("50");
        dEntry.backgroundField.setText("British F1 driver turned doctor");
        dEntry.notesField.setText("He's now a doctor");
        dEntry.submitButton.doClick();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            fail("There shouldn't be an exception");
        }
        assertEquals(false, dEntry.successfulAdd);
    }

    // works
    @Test
    public void testTablePopulation() {
        viewDoctors viewDoctor = new viewDoctors(null);
        assertTrue(viewDoctor.getDoctorsTable().getRowCount() > 0);
    }

    //works
    @Test
    public void testSearchFunctionality() {
        // Initialize the viewDoctors object
        viewDoctors viewDoctor = new viewDoctors(null);
        // Simulate search for a specific term
        viewDoctor.getSearchField().setText("John");
        viewDoctor.getColumnFilter().setSelectedIndex(1); // Search in the "First Name" column
        viewDoctor.getSearchButton().doClick(); // Simulate button clickss
        // Check if table is filtered correctly
        assertEquals(3, viewDoctor.getDoctorsTable().getRowCount());
    }

}
