/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import com.shopping.DBConnection;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectionDialog
extends JDialog {
    private JTextField hostField;
    private JTextField portField;
    private JTextField dbField;
    private JTextField userField;
    private JPasswordField passField;
    private boolean connected = false;

    public ConnectionDialog(Frame frame) {
        super(frame, "Remote MySQL Database Settings", true);
        this.setLayout(new BorderLayout(10, 10));
        this.setSize(460, 320);
        this.setLocationRelativeTo(frame);
        JPanel jPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        jPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        jPanel.add(new JLabel("Host / Remote IP:"));
        this.hostField = new JTextField(DBConnection.host);
        jPanel.add(this.hostField);
        jPanel.add(new JLabel("Port:"));
        this.portField = new JTextField(DBConnection.port);
        jPanel.add(this.portField);
        jPanel.add(new JLabel("Database Name:"));
        this.dbField = new JTextField(DBConnection.database);
        jPanel.add(this.dbField);
        jPanel.add(new JLabel("Username:"));
        this.userField = new JTextField(DBConnection.username);
        jPanel.add(this.userField);
        jPanel.add(new JLabel("Password:"));
        this.passField = new JPasswordField(DBConnection.password);
        jPanel.add(this.passField);
        this.add((Component)jPanel, "Center");
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 10, 10));
        JButton jButton = new JButton("Test Connection");
        JButton jButton2 = new JButton("Save & Connect");
        JButton jButton3 = new JButton("Cancel");
        jButton.addActionListener(this::onTest);
        jButton2.addActionListener(this::onSave);
        jButton3.addActionListener(actionEvent -> this.dispose());
        jPanel2.add(jButton);
        jPanel2.add(jButton2);
        jPanel2.add(jButton3);
        this.add((Component)jPanel2, "South");
    }

    private void onTest(ActionEvent actionEvent) {
        String string = this.hostField.getText().trim();
        String string2 = this.portField.getText().trim();
        String string3 = this.dbField.getText().trim();
        String string4 = this.userField.getText().trim();
        String string5 = new String(this.passField.getPassword());
        try {
            DBConnection.testConnection(string, string2, string3, string4, string5);
            JOptionPane.showMessageDialog(this, "SUCCESS: Successfully connected to MySQL at " + string + ":" + string2 + "/" + string3 + "!", "Connection Test Succeeded", 1);
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "FAILED: Could not connect to remote database:\n" + exception.getMessage() + "\n\nTips:\n- Make sure the remote MySQL server is running.\n- Verify MySQL user has remote access permissions (% or your IP).\n- Check port 3306 in firewall.", "Connection Failed", 0);
        }
    }

    private void onSave(ActionEvent actionEvent) {
        block2: {
            String string = this.hostField.getText().trim();
            String string2 = this.portField.getText().trim();
            String string3 = this.dbField.getText().trim();
            String string4 = this.userField.getText().trim();
            String string5 = new String(this.passField.getPassword());
            try {
                DBConnection.testConnection(string, string2, string3, string4, string5);
                DBConnection.saveConfig(string, string2, string3, string4, string5);
                this.connected = true;
                JOptionPane.showMessageDialog(this, "Settings saved and connected successfully!");
                this.dispose();
            }
            catch (Exception exception) {
                int n = JOptionPane.showConfirmDialog(this, "Connection failed:\n" + exception.getMessage() + "\n\nDo you still want to save these settings?", "Warning", 0);
                if (n != 0) break block2;
                DBConnection.saveConfig(string, string2, string3, string4, string5);
                this.dispose();
            }
        }
    }

    public boolean isConnected() {
        return this.connected;
    }
}
