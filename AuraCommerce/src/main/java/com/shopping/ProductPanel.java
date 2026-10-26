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

public class ProductPanel
extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JComboBox<String> categoryCombo;

    public ProductPanel() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(UIUtils.BG_CANVAS);
        this.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JPanel jPanel = UIUtils.createCard("\ud83d\udd0d Search & Filter Catalog");
        JPanel jPanel2 = new JPanel(new FlowLayout(0, 10, 6));
        jPanel2.setOpaque(false);
        jPanel2.add(new JLabel("Keyword:"));
        this.searchField = new JTextField(12);
        jPanel2.add(this.searchField);
        jPanel2.add(new JLabel("Category:"));
        this.categoryCombo = new JComboBox<String>(new String[]{"All Categories"});
        jPanel2.add(this.categoryCombo);
        jPanel2.add(new JLabel("Min \u20b9:"));
        this.minPriceField = new JTextField(6);
        jPanel2.add(this.minPriceField);
        jPanel2.add(new JLabel("Max \u20b9:"));
        this.maxPriceField = new JTextField(6);
        jPanel2.add(this.maxPriceField);
        JButton jButton = UIUtils.createButton("Filter", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton jButton2 = new JButton("Reset");
        jButton.addActionListener(actionEvent -> this.loadProducts());
        jButton2.addActionListener(actionEvent -> {
            this.searchField.setText("");
            this.minPriceField.setText("");
            this.maxPriceField.setText("");
            this.categoryCombo.setSelectedIndex(0);
            this.loadProducts();
        });
        jPanel2.add(jButton);
        jPanel2.add(jButton2);
        jPanel.add((Component)jPanel2, "Center");
        this.add((Component)jPanel, "North");
        JPanel jPanel3 = UIUtils.createCard("Master Inventory Ledger");
        Object[] objectArray = new String[]{"ID", "Product Name", "Description", "Available Stock", "Price (INR)", "Category", "Supplier"};
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
        JButton jButton3 = UIUtils.createButton("\u2795 Add Product", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton jButton4 = UIUtils.createButton("\u270f\ufe0f Edit Price/Stock", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        JButton jButton5 = UIUtils.createButton("\ud83d\uddd1\ufe0f Delete Product", UIUtils.DANGER, Color.WHITE);
        JButton jButtonSuppliers = UIUtils.createButton("\ud83c\udfed Manage Suppliers", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        JButton jButton6 = new JButton("\ud83d\udd04 Refresh");
        jButton3.addActionListener(actionEvent -> this.showAddProductDialog());
        jButton4.addActionListener(actionEvent -> this.showEditProductDialog());
        jButton5.addActionListener(actionEvent -> this.onDeleteProduct());
        jButtonSuppliers.addActionListener(actionEvent -> this.showManageSuppliersDialog());
        jButton6.addActionListener(actionEvent -> {
            this.loadCategories();
            this.loadProducts();
        });
        jPanel4.add(jButton3);
        jPanel4.add(jButton4);
        jPanel4.add(jButton5);
        jPanel4.add(jButtonSuppliers);
        jPanel4.add(jButton6);
        jPanel3.add((Component)jPanel4, "South");
        this.add((Component)jPanel3, "Center");
        SwingUtilities.invokeLater(() -> {
            this.loadCategories();
            this.loadProducts();
        });
    }

    public void loadCategories() {
        this.categoryCombo.removeAllItems();
        this.categoryCombo.addItem("All Categories");
        String string = "SELECT name FROM category ORDER BY name";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(string);
                    try {
                        while (resultSet.next()) {
                            this.categoryCombo.addItem(resultSet.getString("name"));
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

    public void loadProducts() {
        boolean bl;
        String string;
        this.tableModel.setRowCount(0);
        StringBuilder stringBuilder = new StringBuilder("SELECT p.product_id, p.name, p.description, p.stock_qty, p.price, c.name AS category_name, s.name AS supplier_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id LEFT JOIN supplier s ON p.supplier_id = s.supplier_id WHERE 1=1 ");
        String string2 = this.searchField.getText().trim();
        if (!string2.isEmpty()) {
            stringBuilder.append("AND p.name LIKE ? ");
        }
        if ((string = (String)this.categoryCombo.getSelectedItem()) != null && !string.equals("All Categories")) {
            stringBuilder.append("AND c.name = ? ");
        }
        String string3 = this.minPriceField.getText().trim();
        String string4 = this.maxPriceField.getText().trim();
        boolean bl2 = !string3.isEmpty();
        boolean bl3 = bl = !string4.isEmpty();
        if (bl2 && bl) {
            stringBuilder.append("AND p.price BETWEEN ? AND ? ");
        } else if (bl2) {
            stringBuilder.append("AND p.price >= ? ");
        } else if (bl) {
            stringBuilder.append("AND p.price <= ? ");
        }
        stringBuilder.append("ORDER BY p.product_id ASC");
        try {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString());
                try {
                    int n = 1;
                    if (!string2.isEmpty()) {
                        preparedStatement.setString(n++, "%" + string2 + "%");
                    }
                    if (string != null && !string.equals("All Categories")) {
                        preparedStatement.setString(n++, string);
                    }
                    if (bl2 && bl) {
                        preparedStatement.setDouble(n++, Double.parseDouble(string3));
                        preparedStatement.setDouble(n++, Double.parseDouble(string4));
                    } else if (bl2) {
                        preparedStatement.setDouble(n++, Double.parseDouble(string3));
                    } else if (bl) {
                        preparedStatement.setDouble(n++, Double.parseDouble(string4));
                    }
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("product_id"));
                            vector.add(resultSet.getString("name"));
                            vector.add(resultSet.getString("description"));
                            vector.add(resultSet.getInt("stock_qty"));
                            vector.add(String.format("%.2f", resultSet.getDouble("price")));
                            vector.add(resultSet.getString("category_name"));
                            vector.add(resultSet.getString("supplier_name"));
                            this.tableModel.addRow(vector);
                        }
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

    private void showAddProductDialog() {
        JTextField jTextField = new JTextField();
        JTextField jTextField2 = new JTextField();
        JTextField jTextField3 = new JTextField("1");
        JTextField jTextField4 = new JTextField("1");
        JTextField jTextField5 = new JTextField("10");
        JTextField jTextField6 = new JTextField("999.00");
        JPanel jPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        jPanel.add(new JLabel("Product Name:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Description:"));
        jPanel.add(jTextField2);
        jPanel.add(new JLabel("Category ID:"));
        jPanel.add(jTextField3);
        jPanel.add(new JLabel("Supplier ID:"));
        jPanel.add(jTextField4);
        jPanel.add(new JLabel("Initial Stock:"));
        jPanel.add(jTextField5);
        jPanel.add(new JLabel("Price (INR):"));
        jPanel.add(jTextField6);
        int n = JOptionPane.showConfirmDialog(this, jPanel, "Add New Product", 2);
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
                        preparedStatement.setInt(4, Integer.parseInt(jTextField4.getText().trim()));
                        preparedStatement.setInt(5, Integer.parseInt(jTextField5.getText().trim()));
                        preparedStatement.setDouble(6, Double.parseDouble(jTextField6.getText().trim()));
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Product added successfully!");
                        this.loadProducts();
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
                JOptionPane.showMessageDialog(this, "Database Constraint Violation:\n" + sQLException.getMessage(), "Insert Failed", 0);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + exception.getMessage(), "Input Error", 0);
            }
        }
    }

    private void showEditProductDialog() {
        int n = this.productTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
            return;
        }
        int n2 = (Integer)this.tableModel.getValueAt(n, 0);
        String string = (String)this.tableModel.getValueAt(n, 1);
        int n3 = (Integer)this.tableModel.getValueAt(n, 3);
        String string2 = (String)this.tableModel.getValueAt(n, 4);
        JTextField jTextField = new JTextField(String.valueOf(n3));
        JTextField jTextField2 = new JTextField(string2);
        JPanel jPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        jPanel.add(new JLabel("Product:"));
        jPanel.add(new JLabel(string + " (ID: " + n2 + ")"));
        jPanel.add(new JLabel("Available Stock:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Unit Price (INR):"));
        jPanel.add(jTextField2);
        int n4 = JOptionPane.showConfirmDialog(this, jPanel, "Update Product Stock & Price", 2);
        if (n4 == 0) {
            String string3 = "UPDATE product SET stock_qty = ?, price = ? WHERE product_id = ?";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string3);
                    try {
                        preparedStatement.setInt(1, Integer.parseInt(jTextField.getText().trim()));
                        preparedStatement.setDouble(2, Double.parseDouble(jTextField2.getText().trim()));
                        preparedStatement.setInt(3, n2);
                        int n5 = preparedStatement.executeUpdate();
                        if (n5 > 0) {
                            JOptionPane.showMessageDialog(this, "Product updated successfully!");
                            this.loadProducts();
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
                JOptionPane.showMessageDialog(this, "Database Constraint Violation:\n" + sQLException.getMessage(), "Update Failed", 0);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + exception.getMessage(), "Input Error", 0);
            }
        }
    }

    private void onDeleteProduct() {
        int n = this.productTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int n2 = (Integer)this.tableModel.getValueAt(n, 0);
        String string = (String)this.tableModel.getValueAt(n, 1);
        int n3 = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete product '" + string + "' (ID: " + n2 + ")?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (n3 == JOptionPane.YES_OPTION) {
            String string2 = "DELETE FROM product WHERE product_id = ?";
            try (Connection connection = DBConnection.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(string2)) {
                    preparedStatement.setInt(1, n2);
                    int n4 = preparedStatement.executeUpdate();
                    if (n4 > 0) {
                        JOptionPane.showMessageDialog(this, "Product '" + string + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.loadProducts();
                        return;
                    }
                } catch (SQLException sQLException) {
                    int forceConfirm = JOptionPane.showConfirmDialog(
                        this,
                        "Cannot delete product directly because it is referenced in customer orders or reviews.\n" +
                        "Do you want to force delete it along with associated historical order items and reviews?",
                        "Foreign Key Reference Detected",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE
                    );
                    if (forceConfirm == JOptionPane.YES_OPTION) {
                        connection.setAutoCommit(false);
                        try {
                            try (PreparedStatement psRev = connection.prepareStatement("DELETE FROM review WHERE product_id = ?")) {
                                psRev.setInt(1, n2);
                                psRev.executeUpdate();
                            }
                            try (PreparedStatement psItems = connection.prepareStatement("DELETE FROM order_item WHERE product_id = ?")) {
                                psItems.setInt(1, n2);
                                psItems.executeUpdate();
                            }
                            try (PreparedStatement psProd = connection.prepareStatement("DELETE FROM product WHERE product_id = ?")) {
                                psProd.setInt(1, n2);
                                psProd.executeUpdate();
                            }
                            connection.commit();
                            JOptionPane.showMessageDialog(this, "Product and associated records deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            this.loadProducts();
                        } catch (Exception ex) {
                            connection.rollback();
                            JOptionPane.showMessageDialog(this, "Could not force delete product:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            connection.setAutoCommit(true);
                        }
                    }
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Database connection error: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showManageSuppliersDialog() {
        javax.swing.JDialog dialog = new javax.swing.JDialog(SwingUtilities.getWindowAncestor(this), "Manage Suppliers", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(680, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel supModel = new DefaultTableModel(new String[]{"ID", "Name", "Contact Email", "Phone", "Address"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable supTable = new JTable(supModel);
        UIUtils.formatTable(supTable);

        Runnable loadSuppliers = () -> {
            supModel.setRowCount(0);
            try (Connection conn = DBConnection.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT supplier_id, name, contact_email, phone, address FROM supplier ORDER BY supplier_id")) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("supplier_id"));
                    row.add(rs.getString("name"));
                    row.add(rs.getString("contact_email"));
                    row.add(rs.getString("phone"));
                    row.add(rs.getString("address"));
                    supModel.addRow(row);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error loading suppliers: " + ex.getMessage());
            }
        };
        loadSuppliers.run();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnAdd = UIUtils.createButton("\u2795 Add Supplier", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton btnDel = UIUtils.createButton("\ud83d\uddd1\ufe0f Delete Supplier", UIUtils.DANGER, Color.WHITE);
        JButton btnClose = new JButton("Close");

        btnAdd.addActionListener(e -> {
            JTextField nameF = new JTextField();
            JTextField emailF = new JTextField();
            JTextField phoneF = new JTextField();
            JTextField addrF = new JTextField();
            JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
            form.add(new JLabel("Supplier Name:")); form.add(nameF);
            form.add(new JLabel("Contact Email:")); form.add(emailF);
            form.add(new JLabel("Phone:")); form.add(phoneF);
            form.add(new JLabel("Address:")); form.add(addrF);
            int res = JOptionPane.showConfirmDialog(dialog, form, "Add New Supplier", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION && !nameF.getText().trim().isEmpty()) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("INSERT INTO supplier (name, contact_email, phone, address) VALUES (?, ?, ?, ?)")) {
                    ps.setString(1, nameF.getText().trim());
                    ps.setString(2, emailF.getText().trim());
                    ps.setString(3, phoneF.getText().trim());
                    ps.setString(4, addrF.getText().trim());
                    ps.executeUpdate();
                    loadSuppliers.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error adding supplier: " + ex.getMessage());
                }
            }
        });

        btnDel.addActionListener(e -> {
            int sel = supTable.getSelectedRow();
            if (sel < 0) {
                JOptionPane.showMessageDialog(dialog, "Please select a supplier from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int supId = (Integer) supModel.getValueAt(sel, 0);
            String supName = (String) supModel.getValueAt(sel, 1);
            int conf = JOptionPane.showConfirmDialog(
                dialog,
                "Are you sure you want to delete supplier '" + supName + "' (ID: " + supId + ")?\n" +
                "Existing products supplied by them will remain in the catalog with supplier unassigned.",
                "Confirm Delete Supplier",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (conf == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM supplier WHERE supplier_id = ?")) {
                    ps.setInt(1, supId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Supplier deleted successfully!");
                    loadSuppliers.run();
                    this.loadProducts();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error deleting supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        btnPanel.add(btnClose);

        dialog.add(new JScrollPane(supTable), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}

