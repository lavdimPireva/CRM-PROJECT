package org.example.controller;


import org.example.database.DatabaseConnection;
import org.example.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {


    public static String validateCustomer(Customer customer) {
        if (!ValidationHelper.validateCustomer(customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress())) {
            return "Customer validation failed. Please check the input fields.";
        }
        return null;
    }

    public static boolean updateCustomerWithValidation(int id, String name, String email, String phone, String address) {
        if (ValidationHelper.validateCustomer(name, email, phone, address)) {
            return updateCustomer(id, name, email, phone, address);
        } else {
            System.out.println("Validation failed. Customer not updated.");
            return false;
        }
    }


    public static boolean addCustomerWithValidation(Customer customer) {
        String validationError = validateCustomer(customer);

        if (validationError == null) {
            addCustomer(customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress());
            return true;
        } else {
            return false;
        }
    }

    public static void addCustomer(String name, String email, String phone, String address) {
        String query = "INSERT INTO customers (name, email, phone, address, date_added) VALUES (?, ?, ?, ?, CURRENT_DATE)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);

            stmt.executeUpdate();
            System.out.println("Customer added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Customer> getCustomerData() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT id, name, email, phone, address, date_added FROM customers order by id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String dateAdded = rs.getString("date_added");

                Customer customer = new Customer(id, name, email, phone, address, dateAdded);
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }


    public static void deleteCustomer(int id) {
        String query = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Customer with ID " + id + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static boolean updateCustomer(int id, String name, String email, String phone, String address) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer updated successfully!");
                return true;
            } else {
                System.out.println("Customer with ID " + id + " not found.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}






