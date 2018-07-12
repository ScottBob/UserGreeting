package com.kcar;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DataAccess {

    Properties dbConnectionProperties = new Properties();
    String userName = "yourusername";
    String password = "yoursecurepassword";
    String dbms = "postgresql";
    String serverName = "localhost";
    String portNumber = "5432";
    String dbName = "testdb";

    private Properties getDBConnectionProperties() {
        InputStream input = null;
        String fileName = System.getProperty("user.home") + "/dbConnection.properties";
        try {
            input = new FileInputStream(fileName);
            dbConnectionProperties.load(input);
        } catch (IOException e) {
            dbConnectionProperties.setProperty("userName", userName);
            dbConnectionProperties.setProperty("password", password);
            dbConnectionProperties.setProperty("dbms", dbms);
            dbConnectionProperties.setProperty("serverName", serverName);
            dbConnectionProperties.setProperty("portNumber", portNumber);
            dbConnectionProperties.setProperty("dbName", dbName);
            storePropertiesFile(fileName, dbConnectionProperties);
        }

        return dbConnectionProperties;
    }

    private File storePropertiesFile(String fileName, Properties properties) {
        FileOutputStream fileOut = null;
        File file = new File(fileName);
        try {
            fileOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to create database connection properties file: " + fileName + System.lineSeparator() + e.getMessage());
            e.printStackTrace();
            return file;
        }

        try {
            properties.store(fileOut, "Database Connection Credentials");
        } catch (IOException e) {
            System.out.println("Unable to store data to database connection properties file: " + fileName + System.lineSeparator() + e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    public Map<String, String> getData() {
        HashMap<String,String> data = new HashMap<>();
        try (Connection conn = getConnection()) {
            // Connect to db
//            Connection conn = getConnection();
            // Create query
            String query = "select NAME, VALUE from TESTTABLE";
            // Execute query
            Statement stmt = conn.createStatement();
            if (stmt.execute(query)) {
                // Get results
                ResultSet result = stmt.getResultSet();
                while (result.next()) {
                    String name = result.getString(1);
                    String value = result.getString(2);
                    data.put(name, value);
                }
                // Disconnect from db
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getErrorCode());
        }
        return data;
    }

    public Connection getConnection() throws SQLException {

        Properties dbcProps = getDBConnectionProperties();
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbcProps.get("userName"));
        connectionProps.put("password", dbcProps.get("password"));
//        connectionProps.setProperty("ssl","true");
        String connectionString =
                "jdbc:" + dbcProps.get("dbms") + "://" +
                        dbcProps.get("serverName") +
                        ":" + dbcProps.get("portNumber") + "/" +
                        dbcProps.get("dbName");

        System.out.println(connectionString);
        conn = DriverManager.getConnection(
                connectionString,
                connectionProps);
        return conn;
    }
}
