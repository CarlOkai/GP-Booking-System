import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class viewPatients extends JFrame {
    public static JTable patientsTable;
    public static JTextField searchField;
    public static JComboBox<String> columnFilter;
    public static JButton menuButton;
    public JButton searchButton;
    public static LoginPage loginPage;

    public static void main(String[] args) {
        User user = null;
        new viewPatients(user);
    }

    public viewPatients(User user) {
        setTitle("Patients Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Set the Look and Feel to the system Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Patient ID");
        model.addColumn("Doctor ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Phone Number");
        model.addColumn("Email");
        model.addColumn("Address");

        // Retrieve data from the db to put in the table
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT patientid, doctorid, firstName, lastName, phonenumber, email, address  FROM Patients";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getString("patientid"),
                            resultSet.getString("doctorid"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("phonenumber"),
                            resultSet.getString("email"),
                            resultSet.getString("address")
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
        patientsTable = new JTable(model);
        patientsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(patientsTable);

        // Create search bar
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Create column filter dropdown
        String[] columns = {"Patient ID", "Doctor ID", "First Name", "Last Name", "Phone Number", "Email", "Address"};
        columnFilter = new JComboBox<>(columns);

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
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) patientsTable.getModel());
                patientsTable.setRowSorter(sorter);
                if (searchTerm.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, columnIndex));
                }
            }
        });

        //Create Main Menu Button
        menuButton = new JButton("Main Menu");
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage(user);
            }
        });
        JPanel menuPanel = new JPanel();
        menuPanel.add(menuButton);

        // Add the JScrollPane and search panel to the content pane
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(menuPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTable getDoctorsTable() {
        return patientsTable;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getColumnFilter() {
        return columnFilter;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
