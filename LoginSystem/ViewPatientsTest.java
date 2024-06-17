import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewPatientsTest {
    private viewPatients viewPatientsInstance;

    // Mock connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private Connection connection;
    private Statement statement;

    @BeforeEach
    void setUp() throws SQLException {
        // Establish database connection
        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        
        // Create test table
        statement.executeUpdate("CREATE TABLE Patients (patientid INT, doctorid INT, firstName VARCHAR(255), " +
                "lastName VARCHAR(255), phonenumber VARCHAR(255), email VARCHAR(255), address VARCHAR(255))");

        // Insert test data
        statement.executeUpdate("INSERT INTO Patients VALUES (1, 1, 'John', 'Doe', '1234567890', 'john@example.com', '123 Main St')");
    }

    @Test
    public void testDataRetrieval() throws SQLException {
        // Create instance of viewPatients
        viewPatients viewPatients = new viewPatients(null);

        // Call method to retrieve data from database
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Patient ID");
        model.addColumn("Doctor ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Phone Number");
        model.addColumn("Email");
        model.addColumn("Address");
        Object[] rowData = {"1", "1", "John", "Doe", "1234567890", "john@example.com", "123 Main St"};
        model.addRow(rowData);

        // Check if model is not null and has data
        assertNotNull(model);
        assertEquals(1, model.getRowCount());
        assertEquals(7, model.getColumnCount());
        assertEquals("1", model.getValueAt(0, 0));
        assertEquals("1", model.getValueAt(0, 1));
        assertEquals("John", model.getValueAt(0, 2));
        assertEquals("Doe", model.getValueAt(0, 3));
        assertEquals("1234567890", model.getValueAt(0, 4));
        assertEquals("john@example.com", model.getValueAt(0, 5));
        assertEquals("123 Main St", model.getValueAt(0, 6));
    }

    @Test
    public void testSearchFunctionality() {
        // Set search term and column index
        User user = new User();
        viewPatients view = new viewPatients(user);
        viewPatientsInstance = new viewPatients(null);
        String searchTerm = "Alice";
        int columnIndex = 2; 
        JButton searchButton = view.searchButton;

        // Trigger search action
        viewPatientsInstance.searchField.setText(searchTerm);
        viewPatientsInstance.columnFilter.setSelectedIndex(columnIndex);
        viewPatientsInstance.searchButton.doClick();

        // Get the filtered table model
        DefaultTableModel model = (DefaultTableModel) viewPatientsInstance.patientsTable.getModel();

        // Check if the filtered model has expected rows
        assertEquals(41, model.getRowCount());

        // Check if the filtered model contains the expected data
        assertEquals(searchTerm, model.getValueAt(0, columnIndex));
    }

    @Test
    public void testMenuButtonActionPerformed() {
        // Create a new viewPatients instance
        User user = new User();
        viewPatients view = new viewPatients(user);

        // Get the menuButton
        JButton menuButton = view.menuButton;

        // Simulate a button click
        menuButton.doClick();

        // Verify that the current frame is disposed
        assertFalse(view.isShowing()); 
    }
}
