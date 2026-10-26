/*
 * Decompiled with CFR 0.152.
 */
package com.shopping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String CONFIG_FILE = "db_config.properties";
    public static String host = "127.0.0.1";
    public static String port = "3306";
    public static String database = "online_shopping";
    public static String username = "root";
    public static String password = "";

    public static void loadConfig() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    Properties properties = new Properties();
                    properties.load(fileInputStream);
                    host = properties.getProperty("host", host);
                    port = properties.getProperty("port", port);
                    database = properties.getProperty("database", database);
                    username = properties.getProperty("username", username);
                    password = properties.getProperty("password", password);
                }
                finally {
                    fileInputStream.close();
                }
            }
            catch (Exception exception) {
                System.err.println("Notice: Could not read config file, using default settings.");
            }
        }
    }

    public static void saveConfig(String string, String string2, String string3, String string4, String string5) {
        host = string;
        port = string2;
        database = string3;
        username = string4;
        password = string5;
        Properties properties = new Properties();
        properties.setProperty("host", host);
        properties.setProperty("port", port);
        properties.setProperty("database", database);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE);
            try {
                properties.store(fileOutputStream, "MySQL Remote Database Configuration");
            }
            finally {
                fileOutputStream.close();
            }
        }
        catch (Exception exception) {
            System.err.println("Notice: Could not save config file: " + exception.getMessage());
        }
    }

    public static String getJdbcUrl() {
        return "jdbc:mysql://" + host.trim() + ":" + port.trim() + "/" + database.trim() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new SQLException("MySQL JDBC Driver not found! Ensure mysql-connector-j.jar is in the classpath.", classNotFoundException);
        }
        return DriverManager.getConnection(DBConnection.getJdbcUrl(), username.trim(), password);
    }

    public static void testConnection(String string, String string2, String string3, String string4, String string5) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String string6 = "jdbc:mysql://" + string.trim() + ":" + string2.trim() + "/" + string3.trim() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=5000";
        Connection connection = DriverManager.getConnection(string6, string4.trim(), string5);
        if (connection != null) {
            connection.close();
        }
    }

    static {
        DBConnection.loadConfig();
    }
}
