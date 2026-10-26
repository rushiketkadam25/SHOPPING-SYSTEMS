/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ModernTableRenderer
extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
        JLabel jLabel = (JLabel)super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
        jLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        if (object == null) {
            jLabel.setText("-");
            return jLabel;
        }
        String string = object.toString();
        if (string.equalsIgnoreCase("Delivered") || string.equalsIgnoreCase("Completed")) {
            jLabel.setText("  \u25cf  " + string + "  ");
            if (!bl) {
                jLabel.setForeground(new Color(22, 101, 52));
                jLabel.setBackground(new Color(220, 252, 231));
            }
            jLabel.setFont(new Font("Segoe UI", 1, 11));
            jLabel.setHorizontalAlignment(0);
            return jLabel;
        }
        if (string.equalsIgnoreCase("Pending") || string.equalsIgnoreCase("In Transit")) {
            jLabel.setText("  \u25cf  " + string + "  ");
            if (!bl) {
                jLabel.setForeground(new Color(154, 52, 18));
                jLabel.setBackground(new Color(254, 243, 199));
            }
            jLabel.setFont(new Font("Segoe UI", 1, 11));
            jLabel.setHorizontalAlignment(0);
            return jLabel;
        }
        if (string.equalsIgnoreCase("Shipped") || string.equalsIgnoreCase("Processing") || string.equalsIgnoreCase("Out for Delivery")) {
            jLabel.setText("  \u25cf  " + string + "  ");
            if (!bl) {
                jLabel.setForeground(new Color(30, 64, 175));
                jLabel.setBackground(new Color(219, 234, 254));
            }
            jLabel.setFont(new Font("Segoe UI", 1, 11));
            jLabel.setHorizontalAlignment(0);
            return jLabel;
        }
        if (string.equalsIgnoreCase("Cancelled")) {
            jLabel.setText("  \u25cf  " + string + "  ");
            if (!bl) {
                jLabel.setForeground(new Color(153, 27, 27));
                jLabel.setBackground(new Color(254, 226, 226));
            }
            jLabel.setFont(new Font("Segoe UI", 1, 11));
            jLabel.setHorizontalAlignment(0);
            return jLabel;
        }
        if (!bl) {
            jLabel.setBackground(n % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            jLabel.setForeground(new Color(30, 41, 59));
        }
        jLabel.setHorizontalAlignment(2);
        return jLabel;
    }
}
