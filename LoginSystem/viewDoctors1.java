import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class viewDoctors1 extends JFrame {
    public static JTable DoctorsTable;
    public static JTextField searchField;
    public static JComboBox<String> columnFilter;
    public static JButton menu;
    public static LoginPage loginPage;
    public JButton searchButton;


    public static void main(String[] args) {
        User user = null;
        new viewDoctors1(user);
    }

    public viewDoctors1(User user) {
        setTitle("Doctors Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Doctor ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Phone Number");
        model.addColumn("Email");
        model.addColumn("Address");
        // Add columns to the DB, naming each one

        // Retrieve data from the db to put in the table
        try {
            final String DB_URL = "jdbc:mysql://dragon.kent.ac.uk:3306/co398";
            final String USERNAME = "co398";
            final String PASSWORD = "x9plodh";
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // establish connection to db
            String sql = "SELECT doctorid, firstName, lastName, phonenumber, email, address  FROM Doctors"; // Select
                                                                                                            // columns
                                                                                                            // in sql
                                                                                                            // needed
                                                                                                            // for table
                                                                                                            // model
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getString("doctorid"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("phonenumber"),
                            resultSet.getString("email"),
                            resultSet.getString("address")
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
        DoctorsTable = new JTable(model);
        DoctorsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(DoctorsTable);

        // Create search bar
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Create column filter dropdown
        String[] columns = { "Doctor ID", "First Name", "Last Name", "Phone Number", "Email", "Address" };
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
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
                        (DefaultTableModel) DoctorsTable.getModel());
                DoctorsTable.setRowSorter(sorter);
                if (searchTerm.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, columnIndex));
                }
            }
        });

        // Create Main Menu Button
        JButton menu = new JButton("Main Menu");
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage(user);
            }
        });
        // Make a new panel for main menu button
        JPanel menuPanel = new JPanel();
        menuPanel.add(menu);

        // Add the JScrollPane and search panel to the content pane
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(menuPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTable getDoctorsTable() {
        return DoctorsTable;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getColumnFilter() {
        return columnFilter;
    }

    public JButton getMenuButton() {
        return menu;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
