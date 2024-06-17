import org.junit.Test;
import static org.junit.Assert.*;

public class ViewDoctors1Test {

    @Test
    public void testConstructor() {
        viewDoctors1 viewDoctors = new viewDoctors1(null);
        assertNotNull(viewDoctors);
    }

    @Test
    public void testSearchFunctionality() {
        // Create an instance of the viewDoctors1 class
        viewDoctors1 viewDoctors = new viewDoctors1(null);

        // Set a search term in the search field
        viewDoctors.searchField.setText("John");

        // Set the column filter to "First Name"
        viewDoctors.columnFilter.setSelectedItem("First Name");

        // Simulate clicking the search button
        viewDoctors.searchButton.doClick();

        // Check if the search functionality works by verifying that the table is filtered
        assertNotNull(viewDoctors.DoctorsTable.getRowSorter());
    }

    //works
    @Test
    public void testTablePopulation() {
        viewDoctors1 viewDoctor = new viewDoctors1(null);
        assertTrue(viewDoctor.getDoctorsTable().getRowCount() > 0);
    }
}
