package org.example.ui;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.example.controller.CustomerController;
import org.example.entity.Customer;



public class MainUI {

    private static JTable table;
    private static DefaultTableModel tableModel;

    public static void createAndShowUI() {
        JFrame frame = new JFrame("Customer Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Customer Management Dashboard", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Date Added"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Customer");
        JButton updateButton = new JButton("Update Customer");
        JButton deleteButton = new JButton("Delete Customer");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);

        frame.setVisible(true);

        refreshTableData();

        addButton.addActionListener(e -> showAddCustomerDialog());

        updateButton.addActionListener(e -> showUpdateCustomerDialog());

        deleteButton.addActionListener(e -> deleteSelectedCustomer());
    }

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

        int result = JOptionPane.showConfirmDialog( null, dialogPanel,
                "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            Customer customer = new Customer(0, name, email, phone, address, null);

            LoaderDialog loaderDialog = new LoaderDialog(null);
            loaderDialog.showLoader();

            new Thread(() -> {
                try {
                    Thread.sleep(1500);

                    boolean insertSuccess = CustomerController.addCustomerWithValidation(customer);

                    if(insertSuccess) {
                        refreshTableData();
                    } else {
                        loaderDialog.hideLoader();
                        JOptionPane.showMessageDialog(null, "Validation failed. Please check the input fields.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loaderDialog.hideLoader();
                }
            }).start();
        }
    }


    private static void showUpdateCustomerDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);
        String currentAddress = (String) tableModel.getValueAt(selectedRow, 4);

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

            LoaderDialog loaderDialog = new LoaderDialog(null);
            loaderDialog.showLoader();

            new Thread(() -> {
                try {
                    Thread.sleep(1500);

                    boolean updateSuccess = CustomerController.updateCustomerWithValidation(id, name, email, phone, address);

                    if (updateSuccess) {
                        refreshTableData();
                    } else {

                        loaderDialog.hideLoader();
                        JOptionPane.showMessageDialog(null, "Validation failed. Please check the input fields.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loaderDialog.hideLoader();
                }
            }).start();
        }
    }


    private static void deleteSelectedCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?",
                "Delete Customer", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            LoaderDialog loaderDialog = new LoaderDialog(null);
            loaderDialog.showLoader();

            new Thread(() -> {
                try {
                    Thread.sleep(1500);

                    CustomerController.deleteCustomer(id);

                    refreshTableData();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loaderDialog.hideLoader();
                }
            }).start();
        }
    }


    private static void refreshTableData() {
        tableModel.setRowCount(0);

        List<Customer> customers = CustomerController.getCustomerData();

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

