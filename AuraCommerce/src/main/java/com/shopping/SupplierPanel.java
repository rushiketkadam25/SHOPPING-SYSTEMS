/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import com.shopping.DBConnection;
import com.shopping.UIUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class SupplierPanel
extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private int activeSupplierId;
    private JLabel supplierInfoLabel;

    public SupplierPanel() {
        this.activeSupplierId = UserSession.getAssociatedId() > 0 ? UserSession.getAssociatedId() : 1;
        String supplierName = UserSession.getDisplayName() != null ? UserSession.getDisplayName() : "Supplier";

        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(UIUtils.BG_LIGHT);
        this.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        JPanel jPanel = UIUtils.createCard("\ud83c\udfed Supplier Account & Stock Portal");
        JPanel jPanel2 = new JPanel(new FlowLayout(0, 12, 6));
        jPanel2.setOpaque(false);
        JLabel jLabel = new JLabel("\ud83c\udfed Logged in as Supplier:");
        jLabel.setFont(new Font("Segoe UI", 1, 13));
        jLabel.setForeground(UIUtils.TEXT_DARK);
        jPanel2.add(jLabel);

        JLabel supplierBadge = new JLabel("  " + supplierName + " (Supplier #" + this.activeSupplierId + ")  ");
        supplierBadge.setFont(new Font("Segoe UI", 1, 13));
        supplierBadge.setOpaque(true);
        supplierBadge.setBackground(new Color(254, 243, 199));
        supplierBadge.setForeground(new Color(146, 64, 14));
        supplierBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(253, 230, 138), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        jPanel2.add(supplierBadge);

        this.supplierInfoLabel = new JLabel();
        this.supplierInfoLabel.setFont(new Font("Segoe UI", 1, 12));
        this.supplierInfoLabel.setForeground(UIUtils.PRIMARY_DARK);
        jPanel2.add(this.supplierInfoLabel);
        jPanel.add((Component)jPanel2, "Center");
        this.add((Component)jPanel, "North");
        JPanel jPanel3 = UIUtils.createCard("Products Supplied by You");
        Object[] objectArray = new String[]{"Product ID", "Product Name", "Description", "Available Stock", "Price (INR)", "Category"};
        this.tableModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.productTable = new JTable(this.tableModel);
        UIUtils.formatTable(this.productTable);
        this.productTable.setSelectionMode(0);
        JScrollPane jScrollPane = new JScrollPane(this.productTable);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jPanel3.add((Component)jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel4.setOpaque(false);
        JButton jButton = UIUtils.createButton("\ud83d\udce6 Restock Inventory (+Stock)", UIUtils.SUCCESS, Color.WHITE);
        JButton jButton2 = UIUtils.createButton("\u270f\ufe0f Update Price", UIUtils.PRIMARY, Color.WHITE);
        JButton jButton3 = UIUtils.createButton("\u2795 Add Product for this Supplier", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        JButton jButtonDelete = UIUtils.createButton("\ud83d\uddd1\ufe0f Delete Product", UIUtils.DANGER, Color.WHITE);
        JButton jButton4 = new JButton("\ud83d\udd04 Refresh");
        jButton.addActionListener(actionEvent -> this.onRestock());
        jButton2.addActionListener(actionEvent -> this.onUpdatePrice());
        jButton3.addActionListener(actionEvent -> this.onAddProduct());
        jButtonDelete.addActionListener(actionEvent -> this.onDeleteProduct());
        jButton4.addActionListener(actionEvent -> this.loadSupplierProducts());
        jPanel4.add(jButton);
        jPanel4.add(jButton2);
        jPanel4.add(jButton3);
        jPanel4.add(jButtonDelete);
        jPanel4.add(jButton4);
        jPanel3.add((Component)jPanel4, "South");
        this.add((Component)jPanel3, "Center");
        SwingUtilities.invokeLater(this::loadSupplierProducts);
    }


    public void loadSupplierProducts() {
        this.tableModel.setRowCount(0);
        String string = "SELECT p.product_id, p.name, p.description, p.stock_qty, p.price, c.name AS category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id WHERE p.supplier_id = ? ORDER BY p.product_id";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(string);
                try {
                    preparedStatement.setInt(1, this.activeSupplierId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        int n = 0;
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("product_id"));
                            vector.add(resultSet.getString("name"));
                            vector.add(resultSet.getString("description"));
                            vector.add(resultSet.getInt("stock_qty"));
                            vector.add(String.format("%.2f", resultSet.getDouble("price")));
                            vector.add(resultSet.getString("category_name"));
                            this.tableModel.addRow(vector);
                            ++n;
                        }
                        this.supplierInfoLabel.setText("Total Products: " + n);
                    }
                    finally {
                        if (resultSet != null) {
                            resultSet.close();
                        }
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
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void onRestock() {
        int n;
        int n2 = this.productTable.getSelectedRow();
        if (n2 < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to restock.");
            return;
        }
        int n3 = (Integer)this.tableModel.getValueAt(n2, 0);
        String string = (String)this.tableModel.getValueAt(n2, 1);
        String string2 = JOptionPane.showInputDialog(this, "Restock '" + string + "'\nCurrent Stock: " + (n = ((Integer)this.tableModel.getValueAt(n2, 3)).intValue()) + "\n\nEnter additional quantity to add:", "Restock Inventory", 3);
        if (string2 != null && !string2.trim().isEmpty()) {
            try {
                int n4 = Integer.parseInt(string2.trim());
                if (n4 <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity to add must be greater than 0.");
                    return;
                }
                String string3 = "UPDATE product SET stock_qty = stock_qty + ? WHERE product_id = ?";
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string3);
                    try {
                        preparedStatement.setInt(1, n4);
                        preparedStatement.setInt(2, n3);
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Restocked successfully! New Stock: " + (n + n4));
                        this.loadSupplierProducts();
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
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Error restocking: " + exception.getMessage(), "Error", 0);
            }
        }
    }

    private void onUpdatePrice() {
        String string;
        int n = this.productTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to update price.");
            return;
        }
        int n2 = (Integer)this.tableModel.getValueAt(n, 0);
        String string2 = (String)this.tableModel.getValueAt(n, 1);
        String string3 = JOptionPane.showInputDialog(this, "Update Price for '" + string2 + "'\nCurrent Price: \u20b9" + (string = (String)this.tableModel.getValueAt(n, 4)) + "\n\nEnter new price (INR):", string);
        if (string3 != null && !string3.trim().isEmpty()) {
            try {
                double d = Double.parseDouble(string3.trim());
                String string4 = "UPDATE product SET price = ? WHERE product_id = ?";
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string4);
                    try {
                        preparedStatement.setDouble(1, d);
                        preparedStatement.setInt(2, n2);
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Price updated successfully to \u20b9" + String.format("%.2f", d));
                        this.loadSupplierProducts();
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
                JOptionPane.showMessageDialog(this, "Trigger/Constraint Error (negative price rejected):\n" + sQLException.getMessage(), "Error", 0);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Invalid price: " + exception.getMessage());
            }
        }
    }

    private void onAddProduct() {
        JTextField jTextField = new JTextField();
        JTextField jTextField2 = new JTextField();
        JTextField jTextField3 = new JTextField("1");
        JTextField jTextField4 = new JTextField("25");
        JTextField jTextField5 = new JTextField("499.00");
        JPanel jPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        jPanel.add(new JLabel("Product Name:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Description:"));
        jPanel.add(jTextField2);
        jPanel.add(new JLabel("Category ID:"));
        jPanel.add(jTextField3);
        jPanel.add(new JLabel("Initial Stock:"));
        jPanel.add(jTextField4);
        jPanel.add(new JLabel("Price (INR):"));
        jPanel.add(jTextField5);
        int n = JOptionPane.showConfirmDialog(this, jPanel, "Add New Product for Supplier #" + this.activeSupplierId, 2);
        if (n == 0) {
            String string = "INSERT INTO product (name, description, category_id, supplier_id, stock_qty, price) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string);
                    try {
                        preparedStatement.setString(1, jTextField.getText().trim());
                        preparedStatement.setString(2, jTextField2.getText().trim());
                        preparedStatement.setInt(3, Integer.parseInt(jTextField3.getText().trim()));
                        preparedStatement.setInt(4, this.activeSupplierId);
                        preparedStatement.setInt(5, Integer.parseInt(jTextField4.getText().trim()));
                        preparedStatement.setDouble(6, Double.parseDouble(jTextField5.getText().trim()));
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Product added successfully!");
                        this.loadSupplierProducts();
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
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Error adding product:\n" + exception.getMessage(), "Database Error", 0);
            }
        }
    }

    private void onDeleteProduct() {
        int row = this.productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (Integer) this.tableModel.getValueAt(row, 0);
        String productName = (String) this.tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete product '" + productName + "' (ID: " + productId + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM product WHERE product_id = ? AND supplier_id = ?")) {
                ps.setInt(1, productId);
                ps.setInt(2, this.activeSupplierId);
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    JOptionPane.showMessageDialog(this, "Product '" + productName + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.loadSupplierProducts();
                    return;
                }
            } catch (SQLException sqle) {
                int forceConfirm = JOptionPane.showConfirmDialog(
                    this,
                    "Cannot delete product directly because it is referenced in orders or customer reviews.\n" +
                    "Do you want to force delete it along with its historical order items and reviews?",
                    "Order Reference Detected",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                );

                if (forceConfirm == JOptionPane.YES_OPTION) {
                    conn.setAutoCommit(false);
                    try {
                        try (PreparedStatement psRev = conn.prepareStatement("DELETE FROM review WHERE product_id = ?")) {
                            psRev.setInt(1, productId);
                            psRev.executeUpdate();
                        }
                        try (PreparedStatement psItems = conn.prepareStatement("DELETE FROM order_item WHERE product_id = ?")) {
                            psItems.setInt(1, productId);
                            psItems.executeUpdate();
                        }
                        try (PreparedStatement psProd = conn.prepareStatement("DELETE FROM product WHERE product_id = ? AND supplier_id = ?")) {
                            psProd.setInt(1, productId);
                            psProd.setInt(2, this.activeSupplierId);
                            psProd.executeUpdate();
                        }
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "Product and associated records deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.loadSupplierProducts();
                    } catch (Exception ex) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Could not delete product:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        conn.setAutoCommit(true);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

