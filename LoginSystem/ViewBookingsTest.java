import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Component;


public class ViewBookingsTest {
    
    private viewBookings viewBookingsInstance;
    
    @Test
    public void testInitialization() {
        User user = new User();
        viewBookings bookingsView = new viewBookings(user);

        // Check if the JFrame is visible
        assertTrue(bookingsView.isVisible());

        // Check if the JTable is initialized
        assertNotNull(viewBookings.BookingsTable);
        assertTrue(viewBookings.BookingsTable instanceof JTable);

        // Check if the JScrollPane is initialized
        Component[] components = bookingsView.getContentPane().getComponents();
        JScrollPane scrollPane = null;
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                scrollPane = (JScrollPane) comp;
                break;
            }
        }
        assertNotNull(scrollPane);

        // Check if the JTextField is initialized
        assertNotNull(viewBookings.searchField);
        assertTrue(viewBookings.searchField instanceof JTextField);

        // Check if the JComboBox is initialized
        assertNotNull(viewBookings.columnFilter);
        assertTrue(viewBookings.columnFilter instanceof JComboBox);

        // Check if the JButton is initialized
        assertNotNull(bookingsView.getMenuButton());
        assertTrue(bookingsView.getMenuButton() instanceof JButton);
    }
    
    @Test
    public void testTableModelCreation() {
        // Create a new instance of viewBookings
        viewBookings view = new viewBookings(null);

        // Get the table model
        DefaultTableModel model = view.getModel();

        // Check if the model is not null
        assertNotNull(model);

        // Check if the model has the expected number of columns
        assertEquals(4, model.getColumnCount());
    }
    
    @Test
    public void testSearchFunctionality() {
        // Create a new instance of viewBookings
        viewBookings view = new viewBookings(null);

        // Set some search term and column index
        String searchTerm = "123";
        int columnIndex = 0;

        // Perform search
        view.performSearch(searchTerm, columnIndex);

        // Get the table model
        DefaultTableModel model = view.getModel();

        // Check if the table model has been filtered correctly
        assertNotNull(model.getRowCount());
        assertEquals(5, model.getRowCount()); 
    }
    
}
