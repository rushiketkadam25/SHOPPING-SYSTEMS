/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import com.shopping.DBConnection;
import com.shopping.UIUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel
extends JPanel {
    private JTable customerTable;
    private DefaultTableModel customerModel;
    private JTable reviewTable;
    private DefaultTableModel reviewModel;

    public CustomerPanel() {
        this.setLayout(new GridLayout(2, 1, 14, 14));
        this.setBackground(UIUtils.BG_CANVAS);
        this.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JPanel jPanel = UIUtils.createCard("\ud83d\udc65 Registered Customer Accounts");
        Object[] objectArray = new String[]{"Customer ID", "Full Name", "Email Address", "Phone Number", "Registered Date", "Gender"};
        this.customerModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.customerTable = new JTable(this.customerModel);
        UIUtils.formatTable(this.customerTable);
        JScrollPane jScrollPane = new JScrollPane(this.customerTable);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jPanel.add((Component)jScrollPane, "Center");
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel2.setOpaque(false);
        JButton jButton = UIUtils.createButton("\u2795 Add Customer", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton jButton2 = UIUtils.createButton("\u270f\ufe0f Edit Contact", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        JButton jButton3 = UIUtils.createButton("\ud83d\uddd1\ufe0f Delete Customer", UIUtils.DANGER, Color.WHITE);
        JButton jButton4 = new JButton("\ud83d\udd04 Refresh");
        jButton.addActionListener(actionEvent -> this.showAddCustomerDialog());
        jButton2.addActionListener(actionEvent -> this.showEditCustomerDialog());
        jButton3.addActionListener(actionEvent -> this.onDeleteCustomer());
        jButton4.addActionListener(actionEvent -> this.loadCustomers());
        jPanel2.add(jButton);
        jPanel2.add(jButton2);
        jPanel2.add(jButton3);
        jPanel2.add(jButton4);
        jPanel.add((Component)jPanel2, "South");
        this.add(jPanel);
        JPanel jPanel3 = UIUtils.createCard("\u270d\ufe0f Verified Product Reviews & Ratings");
        Object[] objectArray2 = new String[]{"Review ID", "Customer Name", "Product Name", "Rating", "Customer Comment", "Review Date"};
        this.reviewModel = new DefaultTableModel(objectArray2, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.reviewTable = new JTable(this.reviewModel);
        UIUtils.formatTable(this.reviewTable);
        JScrollPane jScrollPane2 = new JScrollPane(this.reviewTable);
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
        jPanel3.add((Component)jScrollPane2, "Center");
        JPanel jPanel4 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel4.setOpaque(false);
        JButton jButton5 = UIUtils.createButton("\u2795 Add Review", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton jButton6 = UIUtils.createButton("\ud83d\uddd1\ufe0f Delete Review", UIUtils.DANGER, Color.WHITE);
        JButton jButton7 = new JButton("\ud83d\udd04 Refresh");
        jButton5.addActionListener(actionEvent -> this.showAddReviewDialog());
        jButton6.addActionListener(actionEvent -> this.onDeleteReview());
        jButton7.addActionListener(actionEvent -> this.loadReviews());
        jPanel4.add(jButton5);
        jPanel4.add(jButton6);
        jPanel4.add(jButton7);
        jPanel3.add((Component)jPanel4, "South");
        this.add(jPanel3);
        SwingUtilities.invokeLater(() -> {
            this.loadCustomers();
            this.loadReviews();
        });
    }

    public void loadCustomers() {
        this.customerModel.setRowCount(0);
        String string = "SELECT customer_id, name, email, phone, registered_on, gender FROM customer ORDER BY customer_id";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(string);
                    try {
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("customer_id"));
                            vector.add(resultSet.getString("name"));
                            vector.add(resultSet.getString("email"));
                            vector.add(resultSet.getString("phone"));
                            vector.add(resultSet.getString("registered_on"));
                            vector.add(resultSet.getString("gender"));
                            this.customerModel.addRow(vector);
                        }
                    }
                    finally {
                        if (resultSet != null) {
                            resultSet.close();
                        }
                    }
                }
                finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
            finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void loadReviews() {
        this.reviewModel.setRowCount(0);
        String string = "SELECT r.review_id, c.name AS customer_name, p.name AS product_name, r.rating, r.comment, r.review_date FROM review r LEFT JOIN customer c ON r.customer_id = c.customer_id LEFT JOIN product p ON r.product_id = p.product_id ORDER BY r.review_id DESC";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(string);
                    try {
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("review_id"));
                            vector.add(resultSet.getString("customer_name"));
                            vector.add(resultSet.getString("product_name"));
                            vector.add(resultSet.getInt("rating") + " \u2605");
                            vector.add(resultSet.getString("comment"));
                            vector.add(resultSet.getString("review_date"));
                            this.reviewModel.addRow(vector);
                        }
                    }
                    finally {
                        if (resultSet != null) {
                            resultSet.close();
                        }
                    }
                }
                finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
            finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void showAddCustomerDialog() {
        JTextField jTextField = new JTextField();
        JTextField jTextField2 = new JTextField();
        JTextField jTextField3 = new JTextField();
        JComboBox<String> jComboBox = new JComboBox<String>(new String[]{"M", "F", "Other"});
        JPanel jPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        jPanel.add(new JLabel("Full Name:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Email Address:"));
        jPanel.add(jTextField2);
        jPanel.add(new JLabel("Phone Number:"));
        jPanel.add(jTextField3);
        jPanel.add(new JLabel("Gender:"));
        jPanel.add(jComboBox);
        int n = JOptionPane.showConfirmDialog(this, jPanel, "Register New Customer", 2);
        if (n == 0) {
            String string = "INSERT INTO customer (name, email, phone, gender) VALUES (?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string);
                    try {
                        preparedStatement.setString(1, jTextField.getText().trim());
                        preparedStatement.setString(2, jTextField2.getText().trim());
                        preparedStatement.setString(3, jTextField3.getText().trim());
                        preparedStatement.setString(4, (String)jComboBox.getSelectedItem());
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Customer added successfully!");
                        this.loadCustomers();
                    }
                    finally {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    }
                }
                finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
            catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(this, "Database Error (check constraints):\n" + sQLException.getMessage(), "Insert Failed", 0);
            }
        }
    }

    private void showEditCustomerDialog() {
        int n = this.customerTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table first.");
            return;
        }
        int n2 = (Integer)this.customerModel.getValueAt(n, 0);
        String string = (String)this.customerModel.getValueAt(n, 1);
        String string2 = (String)this.customerModel.getValueAt(n, 2);
        String string3 = (String)this.customerModel.getValueAt(n, 3);
        JTextField jTextField = new JTextField(string2 != null ? string2 : "");
        JTextField jTextField2 = new JTextField(string3 != null ? string3 : "");
        JPanel jPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        jPanel.add(new JLabel("Customer:"));
        jPanel.add(new JLabel(string + " (ID: " + n2 + ")"));
        jPanel.add(new JLabel("Phone:"));
        jPanel.add(jTextField2);
        jPanel.add(new JLabel("Email:"));
        jPanel.add(jTextField);
        int n3 = JOptionPane.showConfirmDialog(this, jPanel, "Update Customer Contact", 2);
        if (n3 == 0) {
            String string4 = "UPDATE customer SET phone = ?, email = ? WHERE customer_id = ?";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string4);
                    try {
                        preparedStatement.setString(1, jTextField2.getText().trim());
                        preparedStatement.setString(2, jTextField.getText().trim());
                        preparedStatement.setInt(3, n2);
                        int n4 = preparedStatement.executeUpdate();
                        if (n4 > 0) {
                            JOptionPane.showMessageDialog(this, "Customer contact updated!");
                            this.loadCustomers();
                        }
                    }
                    finally {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    }
                }
                finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
            catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(this, "Error updating customer:\n" + sQLException.getMessage(), "Database Error", 0);
            }
        }
    }

    private void onDeleteCustomer() {
        int n = this.customerTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table first.");
            return;
        }
        int n2 = (Integer)this.customerModel.getValueAt(n, 0);
        String string = (String)this.customerModel.getValueAt(n, 1);
        int n3 = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete customer '" + string + "' (ID: " + n2 + ")?", "Confirm Delete", 0);
        if (n3 == 0) {
            String string2 = "DELETE FROM customer WHERE customer_id = ?";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string2);
                    try {
                        preparedStatement.setInt(1, n2);
                        int n4 = preparedStatement.executeUpdate();
                        if (n4 > 0) {
                            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
                            this.loadCustomers();
                        }
                    }
                    finally {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    }
                }
                finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
            catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(this, "Cannot delete customer (referenced in orders or reviews):\n" + sQLException.getMessage(), "Constraint Error", 0);
            }
        }
    }

    private void showAddReviewDialog() {
        JTextField jTextField = new JTextField("1");
        JTextField jTextField2 = new JTextField("1");
        JComboBox<Integer> jComboBox = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});
        jComboBox.setSelectedItem(5);
        JTextField jTextField3 = new JTextField();
        JPanel jPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        jPanel.add(new JLabel("Customer ID:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Product ID:"));
        jPanel.add(jTextField2);
        jPanel.add(new JLabel("Rating (1-5):"));
        jPanel.add(jComboBox);
        jPanel.add(new JLabel("Comment:"));
        jPanel.add(jTextField3);
        int n = JOptionPane.showConfirmDialog(this, jPanel, "Add Verified Review", 2);
        if (n == 0) {
            String string = "INSERT INTO review (customer_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string);
                    try {
                        preparedStatement.setInt(1, Integer.parseInt(jTextField.getText().trim()));
                        preparedStatement.setInt(2, Integer.parseInt(jTextField2.getText().trim()));
                        preparedStatement.setInt(3, (Integer)jComboBox.getSelectedItem());
                        preparedStatement.setString(4, jTextField3.getText().trim());
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Review published successfully!");
                        this.loadReviews();
                    }
                    finally {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    }
                }
                finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
            catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(this, "Failed to publish review:\n" + sQLException.getMessage(), "Database Error", 0);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + exception.getMessage(), "Input Error", 0);
            }
        }
    }

    private void onDeleteReview() {
        int n = this.reviewTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a review from the table to delete.");
            return;
        }
        int n2 = (Integer)this.reviewModel.getValueAt(n, 0);
        int n3 = JOptionPane.showConfirmDialog(this, "Delete Review ID: " + n2 + "?", "Confirm Delete Review", 0);
        if (n3 == 0) {
            String string = "DELETE FROM review WHERE review_id = ?";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string);
                    try {
                        preparedStatement.setInt(1, n2);
                        int n4 = preparedStatement.executeUpdate();
                        if (n4 > 0) {
                            JOptionPane.showMessageDialog(this, "Review #" + n2 + " deleted successfully!");
                            this.loadReviews();
                        }
                    }
                    finally {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    }
                }
                finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
            catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(this, "Error deleting review:\n" + sQLException.getMessage(), "Error", 0);
            }
        }
    }
}
