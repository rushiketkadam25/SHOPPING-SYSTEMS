/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import com.shopping.DBConnection;
import com.shopping.UIUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class CustomerStorefront
extends JPanel {
    private int activeCustomerId;
    private String activeCustomerName;
    private JTable catalogTable;
    private DefaultTableModel catalogModel;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JLabel selectedProductLabel;
    private JLabel selectedPriceLabel;
    private JLabel selectedStockLabel;
    private JSpinner quantitySpinner;
    private JLabel subtotalLabel;
    private JComboBox<String> paymentMethodBox;
    private JComboBox<String> carrierBox;
    private JButton placeOrderBtn;
    private JTable myOrdersTable;
    private DefaultTableModel myOrdersModel;
    private JTable reviewsTable;
    private DefaultTableModel reviewsModel;

    // Enhanced Checkout Fields
    private JLabel timerLabel;
    private Timer checkoutTimer;
    private int remainingSeconds = 300;
    private CardLayout paymentCardLayout;
    private JPanel paymentDetailsPanel;
    private JTextField upiField;
    private JTextField cardNumberField;
    private JTextField cardExpiryField;
    private JPasswordField cardCvvField;
    private JComboBox<String> bankSelectBox;

    public CustomerStorefront() {
        this.activeCustomerId = UserSession.getAssociatedId() > 0 ? UserSession.getAssociatedId() : 1;
        this.activeCustomerName = UserSession.getDisplayName() != null ? UserSession.getDisplayName() : "Customer";

        this.setLayout(new BorderLayout(14, 14));
        this.setBackground(UIUtils.BG_CANVAS);
        this.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JPanel jPanel = UIUtils.createCard("");
        JPanel jPanel2 = new JPanel(new FlowLayout(0, 12, 4));
        jPanel2.setOpaque(false);
        JLabel jLabel = new JLabel("\ud83d\udc64 Logged in as:");
        jLabel.setFont(new Font("Segoe UI", 1, 13));
        jLabel.setForeground(UIUtils.TEXT_DARK);
        jPanel2.add(jLabel);
        
        JLabel userBadge = new JLabel("  " + this.activeCustomerName + " (Account #" + this.activeCustomerId + ")  ");
        userBadge.setFont(new Font("Segoe UI", 1, 13));
        userBadge.setOpaque(true);
        userBadge.setBackground(new Color(224, 231, 255));
        userBadge.setForeground(UIUtils.PRIMARY_DARK);
        userBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(199, 210, 254), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        jPanel2.add(userBadge);

        JButton jButton = UIUtils.createButton("\ud83d\udd04 Refresh Store", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        jButton.addActionListener(actionEvent -> this.refreshAllData());
        jPanel2.add(jButton);
        jPanel.add((Component)jPanel2, "Center");
        this.add((Component)jPanel, "North");
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setFont(new Font("Segoe UI", 1, 13));
        jTabbedPane.addTab("  \ud83d\udecd\ufe0f  Product Storefront & Quick Checkout  ", this.createStorefrontTab());
        jTabbedPane.addTab("  \ud83d\udce6  My Orders & Live Tracking  ", this.createMyOrdersTab());
        jTabbedPane.addTab("  \u270d\ufe0f  My Reviews & Ratings  ", this.createReviewsTab());
        this.add((Component)jTabbedPane, "Center");
        SwingUtilities.invokeLater(this::refreshAllData);
    }

    private JPanel createStorefrontTab() {
        JPanel jPanel = new JPanel(new BorderLayout(16, 16));
        jPanel.setOpaque(false);
        JPanel jPanel2 = UIUtils.createCard("\ud83d\udecd\ufe0f Available Products");
        jPanel2.setLayout(new BorderLayout(10, 10));
        JPanel jPanel3 = new JPanel(new FlowLayout(0, 8, 4));
        jPanel3.setOpaque(false);
        jPanel3.add(new JLabel("Search:"));
        this.searchField = new JTextField(10);
        jPanel3.add(this.searchField);
        jPanel3.add(new JLabel("Category:"));
        this.categoryCombo = new JComboBox<String>(new String[]{"All Categories"});
        jPanel3.add(this.categoryCombo);
        JButton jButton = UIUtils.createButton("Filter", UIUtils.PRIMARY, Color.WHITE);
        JButton jButton2 = new JButton("Reset");
        jButton.addActionListener(actionEvent -> this.loadCatalog());
        jButton2.addActionListener(actionEvent -> {
            this.searchField.setText("");
            this.categoryCombo.setSelectedIndex(0);
            this.loadCatalog();
        });
        jPanel3.add(jButton);
        jPanel3.add(jButton2);
        jPanel2.add((Component)jPanel3, "North");
        Object[] objectArray = new String[]{"ID", "Product Name", "Description", "Stock", "Price (INR)", "Category"};
        this.catalogModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.catalogTable = new JTable(this.catalogModel);
        UIUtils.formatTable(this.catalogTable);
        this.catalogTable.setSelectionMode(0);
        this.catalogTable.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                this.updateCheckoutCard();
            }
        });
        jPanel2.add((Component)new JScrollPane(this.catalogTable), "Center");
        jPanel.add((Component)jPanel2, "Center");
        JPanel jPanel4 = UIUtils.createCard("\ud83d\uded2 Quick Checkout (TCL)");
        jPanel4.setPreferredSize(new Dimension(345, 600));
        jPanel4.setLayout(new BoxLayout(jPanel4, 1));

        // 1. Session Timer Bar
        JPanel timerBar = new JPanel(new BorderLayout(8, 0));
        timerBar.setOpaque(false);
        timerBar.setMaximumSize(new Dimension(320, 28));
        this.timerLabel = new JLabel("\u23f1\ufe0f Cart Hold: 05:00", 2);
        this.timerLabel.setFont(new Font("Segoe UI", 1, 12));
        this.timerLabel.setForeground(new Color(217, 119, 6));
        JButton resetTimerBtn = new JButton("\u21ba Reset");
        resetTimerBtn.setToolTipText("Reset 5-minute checkout reservation");
        resetTimerBtn.setFont(new Font("Segoe UI", 0, 11));
        resetTimerBtn.setMargin(new Insets(1, 6, 1, 6));
        resetTimerBtn.addActionListener(actionEvent -> this.resetReservationTimer());
        timerBar.add((Component)this.timerLabel, "Center");
        timerBar.add((Component)resetTimerBtn, "East");

        this.selectedProductLabel = new JLabel("Select an item from the catalog");
        this.selectedProductLabel.setFont(new Font("Segoe UI", 1, 14));
        this.selectedProductLabel.setForeground(UIUtils.PRIMARY_DARK);
        this.selectedPriceLabel = new JLabel("Price: \u20b90.00");
        this.selectedPriceLabel.setFont(new Font("Segoe UI", 1, 13));
        this.selectedStockLabel = new JLabel("Stock: -");
        this.selectedStockLabel.setFont(new Font("Segoe UI", 0, 12));
        this.selectedStockLabel.setForeground(UIUtils.TEXT_MUTED);

        this.quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        this.quantitySpinner.setMaximumSize(new Dimension(320, 30));
        this.quantitySpinner.addChangeListener(changeEvent -> this.recalculateTotal());

        this.carrierBox = new JComboBox<String>(new String[]{"BlueDart", "DTDC", "Delhivery", "IndiaPost"});
        this.carrierBox.setMaximumSize(new Dimension(320, 30));

        this.paymentMethodBox = new JComboBox<String>(new String[]{
            "UPI / Instant Pay",
            "Credit / Debit Card",
            "Cash on Delivery (COD)",
            "Net Banking"
        });
        this.paymentMethodBox.setMaximumSize(new Dimension(320, 30));

        // Dynamic Payment Details Panel
        this.paymentCardLayout = new CardLayout();
        this.paymentDetailsPanel = new JPanel(this.paymentCardLayout);
        this.paymentDetailsPanel.setOpaque(false);
        this.paymentDetailsPanel.setMaximumSize(new Dimension(320, 68));
        this.paymentDetailsPanel.setPreferredSize(new Dimension(320, 68));

        // UPI Panel
        JPanel upiPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        upiPanel.setOpaque(false);
        JLabel upiLabel = new JLabel("UPI ID (e.g. name@okhdfcbank):");
        upiLabel.setFont(new Font("Segoe UI", 0, 11));
        upiLabel.setForeground(UIUtils.TEXT_MUTED);
        this.upiField = new JTextField("customer@okaxis");
        this.upiField.setToolTipText("Enter valid UPI ID (e.g. user@okhdfcbank, name@upi)");
        upiPanel.add(upiLabel);
        upiPanel.add(this.upiField);

        // Card Panel (Card No + Expiry + CVV)
        JPanel cardPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        cardPanel.setOpaque(false);
        this.cardNumberField = new JTextField("4532 8901 2345 6789");
        this.cardNumberField.setToolTipText("16-digit card number");
        JPanel cardRow2 = new JPanel(new GridLayout(1, 4, 4, 0));
        cardRow2.setOpaque(false);
        JLabel expLbl = new JLabel("Exp:");
        expLbl.setFont(new Font("Segoe UI", 0, 11));
        expLbl.setForeground(UIUtils.TEXT_MUTED);
        this.cardExpiryField = new JTextField("12/28");
        this.cardExpiryField.setToolTipText("Expiry date MM/YY");
        JLabel cvvLbl = new JLabel("CVV:");
        cvvLbl.setFont(new Font("Segoe UI", 1, 11));
        cvvLbl.setForeground(UIUtils.PRIMARY_DARK);
        this.cardCvvField = new JPasswordField("789");
        this.cardCvvField.setToolTipText("3 or 4 digit CVV/CVC on card back");
        cardRow2.add(expLbl);
        cardRow2.add(this.cardExpiryField);
        cardRow2.add(cvvLbl);
        cardRow2.add(this.cardCvvField);
        cardPanel.add(this.cardNumberField);
        cardPanel.add(cardRow2);

        // COD Panel
        JPanel codPanel = new JPanel(new BorderLayout());
        codPanel.setOpaque(false);
        JLabel codInfo = new JLabel("<html><i>\ud83d\udcb5 Pay cash or scan QR at doorstep delivery.<br/>No convenience fee charged.</i></html>");
        codInfo.setFont(new Font("Segoe UI", 0, 11));
        codInfo.setForeground(UIUtils.TEXT_MUTED);
        codPanel.add((Component)codInfo, "Center");

        // Net Banking Panel
        JPanel netPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        netPanel.setOpaque(false);
        JLabel bankLbl = new JLabel("Select Bank:");
        bankLbl.setFont(new Font("Segoe UI", 0, 11));
        bankLbl.setForeground(UIUtils.TEXT_MUTED);
        this.bankSelectBox = new JComboBox<String>(new String[]{
            "HDFC Bank", "State Bank of India (SBI)", "ICICI Bank", "Axis Bank", "Kotak Mahindra Bank"
        });
        netPanel.add(bankLbl);
        netPanel.add(this.bankSelectBox);

        this.paymentDetailsPanel.add((Component)upiPanel, "UPI");
        this.paymentDetailsPanel.add((Component)cardPanel, "CARD");
        this.paymentDetailsPanel.add((Component)codPanel, "COD");
        this.paymentDetailsPanel.add((Component)netPanel, "NET");

        this.paymentMethodBox.addActionListener(actionEvent -> {
            int sel = this.paymentMethodBox.getSelectedIndex();
            if (sel == 0) this.paymentCardLayout.show(this.paymentDetailsPanel, "UPI");
            else if (sel == 1) this.paymentCardLayout.show(this.paymentDetailsPanel, "CARD");
            else if (sel == 2) this.paymentCardLayout.show(this.paymentDetailsPanel, "COD");
            else if (sel == 3) this.paymentCardLayout.show(this.paymentDetailsPanel, "NET");
        });

        this.subtotalLabel = new JLabel("Total: \u20b90.00");
        this.subtotalLabel.setFont(new Font("Segoe UI", 1, 18));
        this.subtotalLabel.setForeground(UIUtils.SUCCESS);

        this.placeOrderBtn = UIUtils.createButton("\u2605 Place Order (TCL Checkout)", UIUtils.SUCCESS, Color.WHITE);
        this.placeOrderBtn.setMaximumSize(new Dimension(320, 38));
        this.placeOrderBtn.setFont(new Font("Segoe UI", 1, 13));
        this.placeOrderBtn.setEnabled(false);
        this.placeOrderBtn.addActionListener(actionEvent -> this.executeTCLCheckout());

        // Assembly
        jPanel4.add(timerBar);
        jPanel4.add(Box.createVerticalStrut(8));
        jPanel4.add(this.selectedProductLabel);
        jPanel4.add(Box.createVerticalStrut(4));
        jPanel4.add(this.selectedPriceLabel);
        jPanel4.add(this.selectedStockLabel);
        jPanel4.add(Box.createVerticalStrut(10));
        jPanel4.add(new JLabel("Order Quantity:"));
        jPanel4.add(this.quantitySpinner);
        jPanel4.add(Box.createVerticalStrut(8));
        jPanel4.add(new JLabel("Delivery Courier:"));
        jPanel4.add(this.carrierBox);
        jPanel4.add(Box.createVerticalStrut(8));
        jPanel4.add(new JLabel("Payment Method:"));
        jPanel4.add(this.paymentMethodBox);
        jPanel4.add(Box.createVerticalStrut(6));
        jPanel4.add(this.paymentDetailsPanel);
        jPanel4.add(Box.createVerticalStrut(10));
        jPanel4.add(this.subtotalLabel);
        jPanel4.add(Box.createVerticalStrut(10));
        jPanel4.add(this.placeOrderBtn);

        JScrollPane checkoutScroll = new JScrollPane(jPanel4);
        checkoutScroll.setBorder(BorderFactory.createEmptyBorder());
        checkoutScroll.setHorizontalScrollBarPolicy(31); // HORIZONTAL_SCROLLBAR_NEVER
        checkoutScroll.setPreferredSize(new Dimension(360, 520));
        jPanel.add((Component)checkoutScroll, "East");
        return jPanel;
    }

    private JPanel createMyOrdersTab() {
        JPanel jPanel = UIUtils.createCard("\ud83d\udce6 Order History & Shipping Status");
        Object[] objectArray = new String[]{"Order ID", "Date", "Amount (INR)", "Order Status", "Carrier", "Tracking No", "Delivery Status"};
        this.myOrdersModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.myOrdersTable = new JTable(this.myOrdersModel);
        UIUtils.formatTable(this.myOrdersTable);
        jPanel.add((Component)new JScrollPane(this.myOrdersTable), "Center");
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel2.setOpaque(false);
        JButton jButton = UIUtils.createButton("\ud83d\udd04 Refresh Orders", new Color(241, 245, 249), UIUtils.TEXT_DARK);
        jButton.addActionListener(actionEvent -> this.loadMyOrders());
        jPanel2.add(jButton);
        jPanel.add((Component)jPanel2, "South");
        return jPanel;
    }

    private JPanel createReviewsTab() {
        JPanel jPanel = UIUtils.createCard("\u270d\ufe0f Verified Customer Reviews & Feedback");
        Object[] objectArray = new String[]{"Review ID", "Product", "Rating", "Comment", "Review Date"};
        this.reviewsModel = new DefaultTableModel(objectArray, 0){

            @Override
            public boolean isCellEditable(int n, int n2) {
                return false;
            }
        };
        this.reviewsTable = new JTable(this.reviewsModel);
        UIUtils.formatTable(this.reviewsTable);
        jPanel.add((Component)new JScrollPane(this.reviewsTable), "Center");
        JPanel jPanel2 = new JPanel(new FlowLayout(2, 10, 8));
        jPanel2.setOpaque(false);
        JButton jButton = UIUtils.createButton("\u270d\ufe0f Write New Review", UIUtils.PRIMARY, Color.WHITE);
        jButton.addActionListener(actionEvent -> this.showAddReviewDialog());
        jPanel2.add(jButton);
        jPanel.add((Component)jPanel2, "South");
        return jPanel;
    }

    private void resetReservationTimer() {
        this.remainingSeconds = 300;
        if (this.timerLabel != null) {
            this.timerLabel.setText("\u23f1\ufe0f Cart Hold: 05:00");
            this.timerLabel.setForeground(new Color(217, 119, 6));
        }
        if (this.placeOrderBtn != null && this.catalogTable.getSelectedRow() >= 0) {
            this.placeOrderBtn.setEnabled(true);
        }
        if (this.checkoutTimer == null) {
            this.checkoutTimer = new Timer(1000, actionEvent -> this.onTimerTick());
        }
        this.checkoutTimer.restart();
    }

    private void onTimerTick() {
        if (this.remainingSeconds > 0) {
            this.remainingSeconds--;
            int n = this.remainingSeconds / 60;
            int n2 = this.remainingSeconds % 60;
            if (this.timerLabel != null) {
                this.timerLabel.setText(String.format("\u23f1\ufe0f Cart Hold: %02d:%02d", n, n2));
                if (this.remainingSeconds <= 30) {
                    this.timerLabel.setForeground(UIUtils.DANGER);
                } else {
                    this.timerLabel.setForeground(new Color(217, 119, 6));
                }
            }
        } else {
            if (this.checkoutTimer != null) {
                this.checkoutTimer.stop();
            }
            if (this.timerLabel != null) {
                this.timerLabel.setText("\u23f0 Expired! Please re-select");
                this.timerLabel.setForeground(UIUtils.DANGER);
            }
            if (this.placeOrderBtn != null) {
                this.placeOrderBtn.setEnabled(false);
            }
            JOptionPane.showMessageDialog(this,
                "Your 5-minute checkout reservation has expired!\nPlease re-select an item or click '\u21ba Reset' to restart.",
                "Session Expired",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateCheckoutCard() {
        int n = this.catalogTable.getSelectedRow();
        if (n < 0) {
            this.selectedProductLabel.setText("Select an item from the catalog");
            this.selectedPriceLabel.setText("Price: \u20b90.00");
            this.selectedStockLabel.setText("Stock: -");
            this.subtotalLabel.setText("Total: \u20b90.00");
            this.placeOrderBtn.setEnabled(false);
            if (this.checkoutTimer != null) {
                this.checkoutTimer.stop();
            }
            if (this.timerLabel != null) {
                this.timerLabel.setText("\u23f1\ufe0f Cart Hold: --:--");
                this.timerLabel.setForeground(UIUtils.TEXT_MUTED);
            }
            return;
        }
        String string = (String)this.catalogModel.getValueAt(n, 1);
        int n2 = (Integer)this.catalogModel.getValueAt(n, 3);
        double d = Double.parseDouble((String)this.catalogModel.getValueAt(n, 4));
        this.selectedProductLabel.setText(string);
        this.selectedPriceLabel.setText("Unit Price: \u20b9" + String.format("%.2f", d));
        this.selectedStockLabel.setText("Available In Stock: " + n2 + " units");
        this.quantitySpinner.setModel(new SpinnerNumberModel(1, 1, Math.max(1, n2), 1));
        this.placeOrderBtn.setEnabled(n2 > 0);
        this.recalculateTotal();
        if (n2 > 0) {
            this.resetReservationTimer();
        }
    }

    private void recalculateTotal() {
        int n = this.catalogTable.getSelectedRow();
        if (n < 0) {
            return;
        }
        double d = Double.parseDouble((String)this.catalogModel.getValueAt(n, 4));
        int n2 = (Integer)this.quantitySpinner.getValue();
        double d2 = d * (double)n2;
        this.subtotalLabel.setText("Total: \u20b9" + String.format("%.2f", d2));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void executeTCLCheckout() {
        int n = this.catalogTable.getSelectedRow();
        if (n < 0) {
            return;
        }
        if (this.remainingSeconds <= 0) {
            JOptionPane.showMessageDialog(this,
                "Your checkout reservation has expired!\nPlease click '\u21ba Reset' or re-select the item to continue.",
                "Session Expired",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int n2 = (Integer)this.catalogModel.getValueAt(n, 0);
        String string = (String)this.catalogModel.getValueAt(n, 1);
        int n3 = (Integer)this.catalogModel.getValueAt(n, 3);
        double d = Double.parseDouble((String)this.catalogModel.getValueAt(n, 4));
        int n4 = (Integer)this.quantitySpinner.getValue();
        String string2 = (String)this.carrierBox.getSelectedItem();
        
        if (n4 > n3) {
            JOptionPane.showMessageDialog(this, "Not enough stock! Available: " + n3);
            return;
        }

        // Validate payment fields based on selected method
        int selPayment = this.paymentMethodBox.getSelectedIndex();
        String paymentTypeStr = "Cash on Delivery";
        if (selPayment == 0) { // UPI
            String upi = this.upiField.getText().trim();
            if (upi.isEmpty() || !upi.contains("@") || upi.indexOf("@") == 0 || upi.indexOf("@") == upi.length() - 1) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid UPI ID / VPA!\nExample: yourname@okhdfcbank or 9876543210@upi",
                    "Invalid UPI ID",
                    JOptionPane.WARNING_MESSAGE);
                this.upiField.requestFocus();
                return;
            }
            paymentTypeStr = "UPI: " + upi;
        } else if (selPayment == 1) { // Card
            String cardNum = this.cardNumberField.getText().trim().replaceAll("\\s+", "");
            if (cardNum.length() < 12 || !cardNum.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid Card Number (12 to 19 digits)!",
                    "Invalid Card Number",
                    JOptionPane.WARNING_MESSAGE);
                this.cardNumberField.requestFocus();
                return;
            }
            String exp = this.cardExpiryField.getText().trim();
            if (!exp.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid Expiry Date in MM/YY format (e.g. 12/28)!",
                    "Invalid Expiry Date",
                    JOptionPane.WARNING_MESSAGE);
                this.cardExpiryField.requestFocus();
                return;
            }
            String cvv = new String(this.cardCvvField.getPassword()).trim();
            if (!cvv.matches("\\d{3,4}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid 3 or 4 digit CVV security code from the back of your card!",
                    "Invalid CVV Code",
                    JOptionPane.WARNING_MESSAGE);
                this.cardCvvField.requestFocus();
                return;
            }
            String last4 = cardNum.length() > 4 ? cardNum.substring(cardNum.length() - 4) : cardNum;
            paymentTypeStr = "Card: ****" + last4 + " (CVV Verified)";
        } else if (selPayment == 2) { // COD
            paymentTypeStr = "Cash on Delivery (COD)";
        } else { // Net Banking
            paymentTypeStr = "Net Banking: " + this.bankSelectBox.getSelectedItem();
        }

        if (paymentTypeStr.length() > 50) {
            paymentTypeStr = paymentTypeStr.substring(0, 50);
        }

        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            double d2 = d * (double)n4;
            int n5 = 1;
            String string4 = "INSERT INTO shipping (address_id, carrier, tracking_no, status) VALUES (1, ?, ?, 'Pending')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(string4, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, string2);
                preparedStatement.setString(2, "TRK" + System.currentTimeMillis() % 100000L);
                preparedStatement.executeUpdate();
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        n5 = rs.getInt(1);
                    }
                }
            }
            int n6 = 0;
            String sqlOrder = "INSERT INTO orders (customer_id, shipment_id, total_amount, status) VALUES (?, ?, ?, 'Processing')";
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement2.setInt(1, this.activeCustomerId);
                preparedStatement2.setInt(2, n5);
                preparedStatement2.setDouble(3, d2);
                preparedStatement2.executeUpdate();
                try (ResultSet resultSet = preparedStatement2.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        n6 = resultSet.getInt(1);
                    }
                }
            }
            try (PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO order_item (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)")) {
                preparedStatement2.setInt(1, n6);
                preparedStatement2.setInt(2, n2);
                preparedStatement2.setInt(3, n4);
                preparedStatement2.setDouble(4, d);
                preparedStatement2.executeUpdate();
            }
            try (PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE product SET stock_qty = stock_qty - ? WHERE product_id = ?")) {
                preparedStatement2.setInt(1, n4);
                preparedStatement2.setInt(2, n2);
                preparedStatement2.executeUpdate();
            }
            try (PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO payment (order_id, amount, payment_type, status) VALUES (?, ?, ?, 'Completed')")) {
                preparedStatement2.setInt(1, n6);
                preparedStatement2.setDouble(2, d2);
                preparedStatement2.setString(3, paymentTypeStr);
                preparedStatement2.executeUpdate();
            }
            connection.commit();

            if (this.checkoutTimer != null) {
                this.checkoutTimer.stop();
            }
            this.cardCvvField.setText("");

            JOptionPane.showMessageDialog(this,
                "SUCCESS! Order placed successfully.\n\n" +
                "\u2022 Order #" + n6 + " confirmed for " + this.activeCustomerName + "\n" +
                "\u2022 Item: " + string + " x" + n4 + "\n" +
                "\u2022 Total Amount: \u20b9" + String.format("%.2f", d2) + "\n" +
                "\u2022 Payment Method: " + paymentTypeStr + "\n" +
                "\u2022 Delivery Courier: " + string2 + "\n" +
                "\u2022 Database: Transaction COMMITTED atomically to MySQL.",
                "Order Placed",
                JOptionPane.INFORMATION_MESSAGE);

            this.refreshAllData();
        }
        catch (Exception exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                }
                catch (Exception exception2) {
                    // empty catch block
                }
            }
            JOptionPane.showMessageDialog(this, "Order Transaction ROLLED BACK:\n" + exception.getMessage(), "Order Failed", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                }
                catch (Exception exception) {}
            }
        }
    }


    public void refreshAllData() {
        this.loadCategories();
        this.loadCatalog();
        this.loadMyOrders();
        this.loadReviews();
    }

    private void loadCategories() {
        this.categoryCombo.removeAllItems();
        this.categoryCombo.addItem("All Categories");
        try {
            Connection connection = DBConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT name FROM category ORDER BY name");
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

    private void loadCatalog() {
        String string;
        this.catalogModel.setRowCount(0);
        StringBuilder stringBuilder = new StringBuilder("SELECT p.product_id, p.name, p.description, p.stock_qty, p.price, c.name AS category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id WHERE 1=1 ");
        String string2 = this.searchField.getText().trim();
        if (!string2.isEmpty()) {
            stringBuilder.append("AND p.name LIKE ? ");
        }
        if ((string = (String)this.categoryCombo.getSelectedItem()) != null && !string.equals("All Categories")) {
            stringBuilder.append("AND c.name = ? ");
        }
        stringBuilder.append("ORDER BY p.product_id");
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
                            this.catalogModel.addRow(vector);
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

    private void loadMyOrders() {
        this.myOrdersModel.setRowCount(0);
        String string = "SELECT o.order_id, o.order_date, o.total_amount, o.status AS order_status, COALESCE(s.carrier, 'Standard') AS carrier, COALESCE(s.tracking_no, 'Pending') AS tracking_no, COALESCE(s.status, 'Pending') AS shipping_status FROM orders o LEFT JOIN shipping s ON o.shipment_id = s.shipment_id WHERE o.customer_id = ? ORDER BY o.order_id DESC";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(string);
                try {
                    preparedStatement.setInt(1, this.activeCustomerId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("order_id"));
                            vector.add(resultSet.getString("order_date"));
                            vector.add(String.format("%.2f", resultSet.getDouble("total_amount")));
                            vector.add(resultSet.getString("order_status"));
                            vector.add(resultSet.getString("carrier"));
                            vector.add(resultSet.getString("tracking_no"));
                            vector.add(resultSet.getString("shipping_status"));
                            this.myOrdersModel.addRow(vector);
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

    private void loadReviews() {
        this.reviewsModel.setRowCount(0);
        String string = "SELECT r.review_id, p.name AS product_name, r.rating, r.comment, r.review_date FROM review r LEFT JOIN product p ON r.product_id = p.product_id WHERE r.customer_id = ? ORDER BY r.review_id DESC";
        try {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(string);
                try {
                    preparedStatement.setInt(1, this.activeCustomerId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            Vector<Object> vector = new Vector<Object>();
                            vector.add(resultSet.getInt("review_id"));
                            vector.add(resultSet.getString("product_name"));
                            vector.add(resultSet.getInt("rating") + " \u2605");
                            vector.add(resultSet.getString("comment"));
                            vector.add(resultSet.getString("review_date"));
                            this.reviewsModel.addRow(vector);
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

    private void showAddReviewDialog() {
        JTextField jTextField = new JTextField("1");
        JComboBox<Integer> jComboBox = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});
        jComboBox.setSelectedItem(5);
        JTextField jTextField2 = new JTextField();
        JPanel jPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        jPanel.add(new JLabel("Product ID:"));
        jPanel.add(jTextField);
        jPanel.add(new JLabel("Rating (1-5):"));
        jPanel.add(jComboBox);
        jPanel.add(new JLabel("Comment:"));
        jPanel.add(jTextField2);
        int n = JOptionPane.showConfirmDialog(this, jPanel, "Submit Review for Customer #" + this.activeCustomerId, 2);
        if (n == 0) {
            String string = "INSERT INTO review (customer_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(string);
                    try {
                        preparedStatement.setInt(1, this.activeCustomerId);
                        preparedStatement.setInt(2, Integer.parseInt(jTextField.getText().trim()));
                        preparedStatement.setInt(3, (Integer)jComboBox.getSelectedItem());
                        preparedStatement.setString(4, jTextField2.getText().trim());
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Review submitted successfully!");
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
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Failed to submit review:\n" + exception.getMessage(), "Error", 0);
            }
        }
    }
}
