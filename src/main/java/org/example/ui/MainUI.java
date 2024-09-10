package org.example.ui;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.example.controller.CustomerController;
import org.example.entity.Customer;



public class MainUI {

    private static JTable table;  // Table for customer data
    private static DefaultTableModel tableModel;  // Model for the table

    public static void createAndShowUI() {
        // Create a new JFrame (the main window)
        JFrame frame = new JFrame("Customer Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set the size of the window

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label
        JLabel label = new JLabel("Customer Management Dashboard", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Create the table model with column names
        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Date Added"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);  // Wrap the table in a scroll pane
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons for Add, Update, Delete
        JButton addButton = new JButton("Add Customer");
        JButton updateButton = new JButton("Update Customer");
        JButton deleteButton = new JButton("Delete Customer");

        // Add buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add the button panel to the bottom of the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame to be visible
        frame.setVisible(true);

        // Load and display the initial customer data in the table
        refreshTableData();

        // Add action listener to the Add button
        addButton.addActionListener(e -> showAddCustomerDialog());

        // Add action listener to the Update button
        updateButton.addActionListener(e -> showUpdateCustomerDialog());

        // Add action listener to the Delete button
        deleteButton.addActionListener(e -> deleteSelectedCustomer());
    }

    // Method to show the add customer dialog
    private static void showAddCustomerDialog() {
        JTextField nameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JTextField addressField = new JTextField(10);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(4, 2));
        dialogPanel.add(new JLabel("Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Email:"));
        dialogPanel.add(emailField);
        dialogPanel.add(new JLabel("Phone:"));
        dialogPanel.add(phoneField);
        dialogPanel.add(new JLabel("Address:"));
        dialogPanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            Customer customer = new Customer(0, name, email, phone, address, null);

            // Show the loader while inserting the customer
            LoaderDialog loaderDialog = new LoaderDialog(null);  // Pass your main frame reference instead of null
            loaderDialog.showLoader();  // Show the loader

            // Run the insertion in a background thread
            new Thread(() -> {
                try {
                    // Simulate processing (e.g., inserting into the database)
                    Thread.sleep(1500);  // Simulate a delay, this should be replaced with actual database operation

                    // Use the CustomerController class to validate and add the customer
                    boolean insertSuccess = CustomerController.addCustomerWithValidation(customer);

                    if(insertSuccess) {
                        refreshTableData();
                    } else {
                        loaderDialog.hideLoader();  // Hide the loader after operation is done
                        JOptionPane.showMessageDialog(null, "Validation failed. Please check the input fields.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Refresh the table data to show the newly added customer

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loaderDialog.hideLoader();  // Hide the loader after operation is done
                }
            }).start();
        }
    }


    // Method to show the update customer dialog
    private static void showUpdateCustomerDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to update.");
            return;
        }

        // Get the current data of the selected customer
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);
        String currentAddress = (String) tableModel.getValueAt(selectedRow, 4);

        // Create fields pre-filled with the current customer data
        JTextField nameField = new JTextField(currentName, 10);
        JTextField emailField = new JTextField(currentEmail, 10);
        JTextField phoneField = new JTextField(currentPhone, 10);
        JTextField addressField = new JTextField(currentAddress, 10);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(4, 2));
        dialogPanel.add(new JLabel("Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Email:"));
        dialogPanel.add(emailField);
        dialogPanel.add(new JLabel("Phone:"));
        dialogPanel.add(phoneField);
        dialogPanel.add(new JLabel("Address:"));
        dialogPanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Update Customer Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            // Show the loader dialog while updating the customer
            LoaderDialog loaderDialog = new LoaderDialog(null);  // Pass your main frame reference instead of null
            loaderDialog.showLoader();  // Show the loader

            // Run the update in a background thread
            new Thread(() -> {
                try {
                    Thread.sleep(1500);  // Simulate a delay, this should be replaced with actual database operation

                    // Validate and update the customer
                    boolean updateSuccess = CustomerController.updateCustomerWithValidation(id, name, email, phone, address);

                    if (updateSuccess) {

                        // Refresh the table data to reflect the updated customer
                        refreshTableData();
                    } else {

                        loaderDialog.hideLoader();  // Hide the loader after operation is done
                        // If validation fails, show an error message dialog
                        JOptionPane.showMessageDialog(null, "Validation failed. Please check the input fields.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Hide the loader after the operation is done
                    loaderDialog.hideLoader();
                }
            }).start();  // Start the thread
        }
    }


    // Method to delete the selected customer
    private static void deleteSelectedCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
            return;
        }

        // Get the ID of the selected customer
        int id = (int) tableModel.getValueAt(selectedRow, 0);

        // Confirm the deletion
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?",
                "Delete Customer", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // Show the loader dialog while deleting the customer
            LoaderDialog loaderDialog = new LoaderDialog(null);  // Pass your main frame reference instead of null
            loaderDialog.showLoader();  // Show the loader

            // Run the deletion in a background thread
            new Thread(() -> {
                try {

                    Thread.sleep(1500);  //

                    // Perform the deletion
                    CustomerController.deleteCustomer(id);

                    // Refresh the table data to remove the deleted customer
                    refreshTableData();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Hide the loader after the operation is done
                    loaderDialog.hideLoader();
                }
            }).start();  // Start the thread
        }
    }


    // Method to refresh the table data with customers from the database
    private static void refreshTableData() {
        // Clear the current table data
        tableModel.setRowCount(0);

        // Fetch the customer data from the database
        List<Customer> customers = CustomerController.getCustomerData();

        // Add each customer to the table
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getDateAdded()
            };
            tableModel.addRow(row);
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::createAndShowUI);
    }
}

