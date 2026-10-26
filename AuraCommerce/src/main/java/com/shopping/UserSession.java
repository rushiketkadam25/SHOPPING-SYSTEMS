/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

public class UserSession {
    private static Role currentRole = Role.ADMIN;
    private static String username = "shop_admin";
    private static String displayName = "Shop Admin";
    private static int associatedId = 1;

    public static void setSession(Role role, String string, String string2, int n) {
        currentRole = role;
        username = string;
        displayName = string2;
        associatedId = n;
    }

    public static Role getRole() {
        return currentRole;
    }

    public static String getUsername() {
        return username;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static int getAssociatedId() {
        return associatedId;
    }

    public static boolean isAdmin() {
        return currentRole == Role.ADMIN;
    }

    public static boolean isCustomer() {
        return currentRole == Role.CUSTOMER;
    }

    public static boolean isSupplier() {
        return currentRole == Role.SUPPLIER;
    }

    public static boolean isShipper() {
        return currentRole == Role.SHIPPER;
    }

    public static enum Role {
        ADMIN("Shop Administrator", "\ud83d\udc51"),
        CUSTOMER("Customer / Buyer", "\ud83d\udc64"),
        SUPPLIER("Supplier / Stock Keeper", "\ud83c\udfed"),
        SHIPPER("Courier / Shipping Staff", "\ud83d\ude9a");

        private final String displayName;
        private final String icon;

        private Role(String string2, String string3) {
            this.displayName = string2;
            this.icon = string3;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getIcon() {
            return this.icon;
        }
    }
}
