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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel
extends JPanel {
    private JComboBox<String> reportSelector;
    private JTextArea sqlQueryArea;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private static final String[][] PREDEFINED_REPORTS = new String[][]{{"\ud83d\udcca Executive Order Summary & Logistics Report (Customer + Order + Payment + Carrier Multi-Join)", "SELECT c.name AS customer_name, o.order_id, o.total_amount, COALESCE(p.payment_type, 'N/A') AS payment_type, COALESCE(p.status, 'Pending') AS payment_status, COALESCE(s.carrier, 'Standard') AS carrier FROM customer c INNER JOIN orders o ON c.customer_id = o.customer_id LEFT JOIN payment p ON o.order_id = p.order_id LEFT JOIN shipping s ON o.shipment_id = s.shipment_id"}, {"\ud83c\udfc6 VIP High-Spenders Club (> \u20b91,000 Gross Spending Analysis)", "SELECT c.name AS customer_name, o.customer_id, SUM(o.total_amount) AS total_spent FROM orders o JOIN customer c ON o.customer_id = c.customer_id GROUP BY o.customer_id, c.name HAVING SUM(o.total_amount) > 1000 ORDER BY total_spent DESC"}, {"\ud83c\udff7\ufe0f Master Product Catalog & Supplier Relationship Matrix", "SELECT p.product_id, p.name AS product_name, c.name AS category_name, s.name AS supplier_name, p.price, p.stock_qty FROM product p INNER JOIN category c ON p.category_id = c.category_id INNER JOIN supplier s ON p.supplier_id = s.supplier_id"}, {"\ud83d\udcc8 Category Inventory Volume & Distribution", "SELECT c.name AS category_name, COUNT(p.product_id) AS total_products FROM category c LEFT JOIN product p ON c.category_id = p.category_id GROUP BY c.name"}, {"\ud83c\udf10 Customer Orders Master View (customer_order_view)", "SELECT * FROM customer_order_view"}, {"\ud83d\udecd\ufe0f Product Catalog & Stock View (product_view)", "SELECT * FROM product_view"}, {"\ud83d\udcb3 Payment Transactions View (payment_view)", "SELECT * FROM payment_view"}, {"\ud83d\ude9a Logistics & Dispatch View (shipping_view)", "SELECT * FROM shipping_view"}};

    public ReportsPanel() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(UIUtils.BG_CANVAS);
        this.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JPanel jPanel = UIUtils.createCard("\ud83d\udcca Business Intelligence & Database Views");
        String[] stringArray = new String[PREDEFINED_REPORTS.length + 1];
        stringArray[0] = "-- Select an Executive Report / View --";
        for (int i = 0; i < PREDEFINED_REPORTS.length; ++i) {
            stringArray[i + 1] = PREDEFINED_REPORTS[i][0];
        }
        this.reportSelector = new JComboBox<String>(stringArray);
        this.reportSelector.setFont(new Font("Segoe UI", 1, 12));
        jPanel.add(this.reportSelector, "North");
        this.sqlQueryArea = new JTextArea(4, 50);
        this.sqlQueryArea.setFont(new Font("Consolas", 0, 13));
        this.sqlQueryArea.setText(PREDEFINED_REPORTS[0][1]);
        this.sqlQueryArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane jScrollPane = new JScrollPane(this.sqlQueryArea);
        jScrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.CARD_BORDER));
        jPanel.add((Component)jScrollPane, "Center");
        this.reportSelector.addActionListener(actionEvent -> {
            int n = this.reportSelector.getSelectedIndex();
            if (n > 0) {
                this.sqlQueryArea.setText(PREDEFINED_REPORTS[n - 1][1]);
            }
        });
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel2.setOpaque(false);
        JButton jButton = UIUtils.createButton("\u26a1 Execute Query", UIUtils.PRIMARY_DARK, Color.WHITE);
        jButton.addActionListener(actionEvent -> this.executeCurrentQuery());
        jPanel2.add(jButton);
        jPanel.add((Component)jPanel2, "South");
        this.add((Component)jPanel, "North");
        JPanel jPanel3 = UIUtils.createCard("Query Output & Data Ledger");
        this.tableModel = new DefaultTableModel();
        this.resultTable = new JTable(this.tableModel);
        UIUtils.formatTable(this.resultTable);
        JScrollPane jScrollPane2 = new JScrollPane(this.resultTable);
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
        jPanel3.add((Component)jScrollPane2, "Center");
        this.statusLabel = new JLabel("Ready. Select an executive query and click 'Execute Query'.");
        this.statusLabel.setForeground(UIUtils.TEXT_MUTED);
        this.statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));
        jPanel3.add((Component)this.statusLabel, "South");
        this.add((Component)jPanel3, "Center");
        SwingUtilities.invokeLater(this::executeCurrentQuery);
    }

    private void executeCurrentQuery() {
        String string = this.sqlQueryArea.getText().trim();
        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or select a SQL query.");
            return;
        }
        long l = System.currentTimeMillis();
        try {
            Connection connection = DBConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(string);
                    try {
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int n = resultSetMetaData.getColumnCount();
                        Vector<String> vector = new Vector<String>();
                        for (int i = 1; i <= n; ++i) {
                            vector.add(resultSetMetaData.getColumnLabel(i));
                        }
                        Vector vector2 = new Vector();
                        int n2 = 0;
                        while (resultSet.next()) {
                            Vector<String> vector3 = new Vector<String>();
                            for (int i = 1; i <= n; ++i) {
                                Object object = resultSet.getObject(i);
                                vector3.add(object != null ? object.toString() : "NULL");
                            }
                            vector2.add(vector3);
                            ++n2;
                        }
                        this.tableModel.setDataVector(vector2, vector);
                        long l2 = System.currentTimeMillis() - l;
                        this.statusLabel.setText("\u26a1 Query executed in " + l2 + "ms \u2022 " + n2 + " rows returned successfully.");
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
        catch (SQLException sQLException) {
            this.statusLabel.setText("SQL Error: " + sQLException.getMessage());
            JOptionPane.showMessageDialog(this, "Error executing SQL:\n" + sQLException.getMessage(), "Query Error", 0);
        }
        catch (Exception exception) {
            this.statusLabel.setText("Error: " + exception.getMessage());
        }
    }
}
