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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ShipperPanel
extends JPanel {
    private JTable shipmentsTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public ShipperPanel() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(UIUtils.BG_CANVAS);
        this.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JPanel jPanel = UIUtils.createCard("\ud83d\ude9a Fleet Delivery & Logistics Dispatch");
        JPanel jPanel2 = new JPanel(new FlowLayout(0, 12, 6));
        jPanel2.setOpaque(false);
        JLabel jLabel = new JLabel("Role: Logistics & Delivery Staff \u2022 Active Customer Destinations");
        jLabel.setFont(new Font("Segoe UI", 1, 12));
        jLabel.setForeground(UIUtils.TEXT_DARK);
        jPanel2.add(jLabel);
        this.countLabel = new JLabel();
        this.countLabel.setFont(new Font("Segoe UI", 1, 12));
        this.countLabel.setForeground(UIUtils.PRIMARY_DARK);
        jPanel2.add(this.countLabel);
        jPanel.add((Component)jPanel2, "Center");
        this.add((Component)jPanel, "North");
        JPanel jPanel3 = UIUtils.createCard("Active Dispatch Shipments & Customer Addresses");
        Object[] objectArray = new String[]{"Shipment ID", "Tracking No", "Carrier", "Current Status", "Order ID", "Customer", "Delivery Destination"};
        this.tableModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.shipmentsTable = new JTable(this.tableModel);
        UIUtils.formatTable(this.shipmentsTable);
        this.shipmentsTable.setSelectionMode(0);
        JScrollPane jScrollPane = new JScrollPane(this.shipmentsTable);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jPanel3.add((Component)jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel4.setOpaque(false);
        JButton jButton = UIUtils.createButton("\ud83d\udce6 Mark In Transit / Shipped", UIUtils.PRIMARY_DARK, Color.WHITE);
        JButton jButton2 = UIUtils.createButton("\u2705 Mark Delivered", UIUtils.SUCCESS, Color.WHITE);
        JButton jButton3 = new JButton("\u270f\ufe0f Change Status...");
        JButton jButton4 = new JButton("\ud83d\udd04 Refresh");
        jButton.addActionListener(actionEvent -> this.updateStatus("Shipped"));
        jButton2.addActionListener(actionEvent -> this.updateStatus("Delivered"));
        jButton3.addActionListener(actionEvent -> this.onCustomStatus());
        jButton4.addActionListener(actionEvent -> this.loadShipments());
        jPanel4.add(jButton);
        jPanel4.add(jButton2);
        jPanel4.add(jButton3);
        jPanel4.add(jButton4);
        jPanel3.add((Component)jPanel4, "South");
        this.add((Component)jPanel3, "Center");
        SwingUtilities.invokeLater(this::loadShipments);
    }

    public void loadShipments() {
        this.tableModel.setRowCount(0);
        String carrier = UserSession.getDisplayName();
        boolean hasSpecificCarrier = carrier != null && !carrier.contains("(") && !carrier.equalsIgnoreCase("shipper");
        String string = "SELECT s.shipment_id, s.tracking_no, s.carrier, s.status AS shipping_status, o.order_id, c.name AS customer_name, CONCAT(a.street, ', ', a.city, ', ', a.state, ' - ', a.pincode) AS delivery_address FROM shipping s LEFT JOIN orders o ON s.shipment_id = o.shipment_id LEFT JOIN customer c ON o.customer_id = c.customer_id LEFT JOIN address a ON s.address_id = a.address_id" + (hasSpecificCarrier ? " WHERE LOWER(s.carrier) LIKE LOWER(?)" : "") + " ORDER BY s.shipment_id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(string)) {
            if (hasSpecificCarrier) {
                statement.setString(1, "%" + carrier + "%");
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                int n = 0;
                while (resultSet.next()) {
                    Vector<Object> vector = new Vector<Object>();
                    vector.add(resultSet.getInt("shipment_id"));
                    vector.add(resultSet.getString("tracking_no"));
                    vector.add(resultSet.getString("carrier"));
                    vector.add(resultSet.getString("shipping_status"));
                    int n2 = resultSet.getInt("order_id");
                    vector.add(n2 > 0 ? Integer.valueOf(n2) : "N/A");
                    vector.add(resultSet.getString("customer_name") != null ? resultSet.getString("customer_name") : "N/A");
                    vector.add(resultSet.getString("delivery_address") != null ? resultSet.getString("delivery_address") : "No Address Recorded");
                    this.tableModel.addRow(vector);
                    ++n;
                }
                this.countLabel.setText("\u2022 Total Packages: " + n + (hasSpecificCarrier ? " (" + carrier + ")" : ""));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void updateStatus(String string) {
        int n = this.shipmentsTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shipment from the table first.");
            return;
        }
        int n2 = (Integer)this.tableModel.getValueAt(n, 0);
        String string2 = "UPDATE shipping SET status = ? WHERE shipment_id = ?";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(string2);
                try {
                    preparedStatement.setString(1, string);
                    preparedStatement.setInt(2, n2);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Shipment #" + n2 + " status updated to '" + string + "'!");
                    this.loadShipments();
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
            JOptionPane.showMessageDialog(this, "Error updating shipment status:\n" + sQLException.getMessage(), "Error", 0);
        }
    }

    private void onCustomStatus() {
        int n = this.shipmentsTable.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shipment from the table first.");
            return;
        }
        int n2 = (Integer)this.tableModel.getValueAt(n, 0);
        String string = (String)this.tableModel.getValueAt(n, 3);
        String[] stringArray = new String[]{"Pending", "In Transit", "Out for Delivery", "Shipped", "Delivered", "Returned"};
        JComboBox<String> jComboBox = new JComboBox<String>(stringArray);
        jComboBox.setSelectedItem(string);
        int n3 = JOptionPane.showConfirmDialog(this, jComboBox, "Update Shipment #" + n2 + " Status", 2);
        if (n3 == 0) {
            this.updateStatus((String)jComboBox.getSelectedItem());
        }
    }
}
