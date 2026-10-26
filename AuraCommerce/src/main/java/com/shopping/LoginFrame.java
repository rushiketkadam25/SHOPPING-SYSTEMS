/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import com.shopping.ConnectionDialog;
import com.shopping.Main;
import com.shopping.UIUtils;
import com.shopping.UserSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginFrame
extends JFrame {
    private JComboBox<String> roleCombo;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        super("AURA COMMERCE - Enterprise Authentication");
        this.setDefaultCloseOperation(3);
        this.setSize(540, 590);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setBackground(UIUtils.BG_CANVAS);
        this.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setOpaque(false);
        jPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JPanel jPanel2 = UIUtils.createCard("");
        jPanel2.setLayout(new BoxLayout(jPanel2, 1));
        jPanel2.setPreferredSize(new Dimension(460, 500));
        jPanel2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIUtils.CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(24, 30, 24, 30)));
        JLabel jLabel = new JLabel("\ud83d\udecd\ufe0f AURA COMMERCE");
        jLabel.setFont(new Font("Segoe UI", 1, 24));
        jLabel.setForeground(UIUtils.PRIMARY_DARK);
        jLabel.setAlignmentX(0.5f);
        JLabel jLabel2 = new JLabel("Next-Gen Enterprise POS & Database Suite");
        jLabel2.setFont(new Font("Segoe UI", 0, 12));
        jLabel2.setForeground(UIUtils.TEXT_MUTED);
        jLabel2.setAlignmentX(0.5f);
        jPanel2.add(jLabel);
        jPanel2.add(Box.createVerticalStrut(4));
        jPanel2.add(jLabel2);
        jPanel2.add(Box.createVerticalStrut(20));
        JPanel jPanel3 = new JPanel(new GridLayout(3, 2, 8, 12));
        jPanel3.setOpaque(false);
        jPanel3.setMaximumSize(new Dimension(410, 115));
        jPanel3.add(new JLabel("Select Portal Role:"));
        this.roleCombo = new JComboBox<String>(new String[]{"\ud83d\udc51 System Administrator", "\ud83d\udc64 Customer / Buyer", "\ud83c\udfed Supplier Partner", "\ud83d\ude9a Logistics Fleet Shipper"});
        this.roleCombo.setFont(new Font("Segoe UI", 1, 12));
        this.roleCombo.addActionListener(actionEvent -> this.updateCredentialsForRole());
        jPanel3.add(this.roleCombo);
        jPanel3.add(new JLabel("Username:"));
        this.usernameField = new JTextField("shop_admin");
        jPanel3.add(this.usernameField);
        jPanel3.add(new JLabel("Password:"));
        this.passwordField = new JPasswordField("Admin@123");
        jPanel3.add(this.passwordField);
        jPanel2.add(jPanel3);
        jPanel2.add(Box.createVerticalStrut(16));
        JButton jButton = UIUtils.createButton("Log In to Portal", UIUtils.PRIMARY_DARK, Color.WHITE);
        jButton.setFont(new Font("Segoe UI", 1, 14));
        jButton.setMaximumSize(new Dimension(410, 42));
        jButton.setAlignmentX(0.5f);
        jButton.addActionListener(this::handleLogin);
        this.getRootPane().setDefaultButton(jButton);
        jPanel2.add(jButton);
        jPanel2.add(Box.createVerticalStrut(20));
        JLabel jLabel3 = new JLabel("\u26a1 1-Click Instant Demo Access (No Typing Needed):");
        jLabel3.setFont(new Font("Segoe UI", 1, 11));
        jLabel3.setForeground(UIUtils.TEXT_MUTED);
        jLabel3.setAlignmentX(0.5f);
        jPanel2.add(jLabel3);
        jPanel2.add(Box.createVerticalStrut(10));
        JPanel jPanel4 = new JPanel(new GridLayout(2, 2, 8, 8));
        jPanel4.setOpaque(false);
        jPanel4.setMaximumSize(new Dimension(410, 76));
        JButton jButton2 = new JButton("\ud83d\udc51 Admin Console");
        JButton jButton3 = new JButton("\ud83d\udc64 Buyer Storefront");
        JButton jButton4 = new JButton("\ud83c\udfed Supplier Portal");
        JButton jButton5 = new JButton("\ud83d\ude9a Logistics Fleet");
        jButton2.addActionListener(actionEvent -> this.quickLogin(0, "shop_admin", "Admin@123", UserSession.Role.ADMIN, "System Administrator", 1));
        jButton3.addActionListener(actionEvent -> this.quickLogin(1, "customer", "Cust@123", UserSession.Role.CUSTOMER, "Aarav Sharma", 1));
        jButton4.addActionListener(actionEvent -> this.quickLogin(2, "inventory_manager", "Inv@123", UserSession.Role.SUPPLIER, "Tech Suppliers", 1));
        jButton5.addActionListener(actionEvent -> this.quickLogin(3, "shipper", "Ship@123", UserSession.Role.SHIPPER, "BlueDart Express", 1));
        jPanel4.add(jButton2);
        jPanel4.add(jButton3);
        jPanel4.add(jButton4);
        jPanel4.add(jButton5);
        jPanel2.add(jPanel4);
        jPanel2.add(Box.createVerticalStrut(14));
        JButton jButton6 = new JButton("\u2699\ufe0f Configure Remote Database Connection...");
        jButton6.setAlignmentX(0.5f);
        jButton6.addActionListener(actionEvent -> {
            ConnectionDialog connectionDialog = new ConnectionDialog(this);
            connectionDialog.setVisible(true);
        });
        jPanel2.add(jButton6);
        jPanel.add(jPanel2);
        this.add((Component)jPanel, "Center");
    }

    private void updateCredentialsForRole() {
        int n = this.roleCombo.getSelectedIndex();
        switch (n) {
            case 0: {
                this.usernameField.setText("shop_admin");
                this.passwordField.setText("Admin@123");
                break;
            }
            case 1: {
                this.usernameField.setText("aarav.sharma@example.com");
                this.passwordField.setText("Cust@123");
                break;
            }
            case 2: {
                this.usernameField.setText("contact@techsuppliers.com");
                this.passwordField.setText("Inv@123");
                break;
            }
            case 3: {
                this.usernameField.setText("BlueDart Express");
                this.passwordField.setText("Ship@123");
                break;
            }
        }
    }

    private void quickLogin(int n, String string, String string2, UserSession.Role role, String string3, int n2) {
        this.roleCombo.setSelectedIndex(n);
        this.usernameField.setText(string);
        this.passwordField.setText(string2);
        UserSession.setSession(role, string, string3, n2);
        this.launchApp();
    }

    private void handleLogin(ActionEvent actionEvent) {
        int n = this.roleCombo.getSelectedIndex();
        String string = this.usernameField.getText().trim();
        String password = new String(this.passwordField.getPassword()).trim();
        
        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username or email address.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserSession.Role role;
        int associatedId = 1;
        String displayName = string;

        switch (n) {
            case 1: {
                role = UserSession.Role.CUSTOMER;
                displayName = "Customer (" + string + ")";
                try (java.sql.Connection conn = DBConnection.getConnection();
                     java.sql.PreparedStatement ps = conn.prepareStatement(
                         "SELECT customer_id, name FROM customer WHERE LOWER(email) = LOWER(?) OR LOWER(name) = LOWER(?) OR customer_id = ? LIMIT 1")) {
                    ps.setString(1, string);
                    ps.setString(2, string);
                    int parsedId = -1;
                    try { parsedId = Integer.parseInt(string); } catch (Exception ignore) {}
                    ps.setInt(3, parsedId);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            associatedId = rs.getInt("customer_id");
                            displayName = rs.getString("name");
                        }
                    }
                } catch (Exception ignore) {}
                break;
            }
            case 2: {
                role = UserSession.Role.SUPPLIER;
                displayName = "Supplier (" + string + ")";
                try (java.sql.Connection conn = DBConnection.getConnection();
                     java.sql.PreparedStatement ps = conn.prepareStatement(
                         "SELECT supplier_id, name FROM supplier WHERE LOWER(contact_email) = LOWER(?) OR LOWER(name) = LOWER(?) OR supplier_id = ? LIMIT 1")) {
                    ps.setString(1, string);
                    ps.setString(2, string);
                    int parsedId = -1;
                    try { parsedId = Integer.parseInt(string); } catch (Exception ignore) {}
                    ps.setInt(3, parsedId);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            associatedId = rs.getInt("supplier_id");
                            displayName = rs.getString("name");
                        }
                    }
                } catch (Exception ignore) {}
                break;
            }
            case 3: {
                role = UserSession.Role.SHIPPER;
                displayName = "Logistics (" + string + ")";
                try (java.sql.Connection conn = DBConnection.getConnection();
                     java.sql.PreparedStatement ps = conn.prepareStatement(
                         "SELECT DISTINCT carrier FROM shipping WHERE LOWER(carrier) LIKE LOWER(?) LIMIT 1")) {
                    ps.setString(1, "%" + string + "%");
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            displayName = rs.getString("carrier");
                        }
                    }
                } catch (Exception ignore) {}
                break;
            }
            default: {
                role = UserSession.Role.ADMIN;
                displayName = "System Administrator";
                break;
            }
        }
        UserSession.setSession(role, string, displayName, associatedId);
        this.launchApp();
    }

    private void launchApp() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}
