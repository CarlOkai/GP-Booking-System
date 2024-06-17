import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class viewBookings extends JFrame {
    public static JTable BookingsTable;
    public static JTextField searchField;
    public static JComboBox<String> columnFilter;
    public static JButton menu;
    public static LoginPage loginPage;
    public JButton searchButton;

    public static void main(String[] args) {
        User user = null;
        new viewBookings(user);
    }

    public viewBookings(User user) {
        setTitle("Bookings Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Booking ID");
        model.addColumn("Patient ID");
        model.addColumn("Doctor ID");
        model.addColumn("Appointment Date");
        // Add columns to the DB, naming each one

        // Retrieve data from the db to put in the table
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT bookingid, patientid, doctorid, appointmentdatetime  FROM Bookings";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Object[] rowData = {
                        resultSet.getString("bookingid"),
                        resultSet.getString("patientid"),
                        resultSet.getString("doctorid"),
                        resultSet.getString("appointmentdatetime"),
                        // Add data to table model
                    };
                    model.addRow(rowData);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to retrieve data from the database");
        }

        // Create the JTable with the populated table model
        BookingsTable = new JTable(model);
        BookingsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(BookingsTable);
        
        // Create search bar
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        // Create column filter dropdown
        String[] columns = {"bookingid", "patientid", "doctorid", "appointmentdatetime"};
        columnFilter = new JComboBox<>(columns);
        
        //Creation of search panel to store the necessary fields/buttons
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(columnFilter);
        searchPanel.add(searchButton);

        // Add action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim().toLowerCase();
                int columnIndex = columnFilter.getSelectedIndex();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel)BookingsTable.getModel());
                BookingsTable.setRowSorter(sorter);
                if (searchTerm.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, columnIndex));
                }
            }
        });
        //Create Main Menu Button
        menu = new JButton("Main Menu");
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage(user);
            }
        });
        JPanel menuPanel = new JPanel();
        menuPanel.add(menu);

        // Add the JScrollPane and search panel to the content pane
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(menuPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    public DefaultTableModel getModel() {
        return (DefaultTableModel) BookingsTable.getModel();
    }

    public void performSearch(String searchTerm, int columnIndex) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(getDefaultTableModel());
        BookingsTable.setRowSorter(sorter);
        if (searchTerm.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, columnIndex));
        }
    }

    private DefaultTableModel getDefaultTableModel() {
        return (DefaultTableModel) BookingsTable.getModel();
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getColumnFilter() {
        return columnFilter;
    }

    public static JButton getMenuButton() {
        return menu;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
