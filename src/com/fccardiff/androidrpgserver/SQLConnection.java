package com.fccardiff.androidrpgserver;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;

/**
 * Created by fccardiff on 9/26/14.
 */
public class SQLConnection {
    static String password = "password";
    static String username = "username";
    static String databaseName = "androidrpgserver";
    static StringBuilder passwordStars = new StringBuilder();
    static int i = 0;
    static final java.util.Properties properties = new java.util.Properties();
    public static void connect(){
        try {
            properties.load(new FileInputStream("server.properties"));
            System.out.println(properties.values());
            // TODO: Properties.values shows this as true, but yet it returns false
         //   databaseName = properties.getProperty("mysql.databaseName");
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:androidrpgserver.db");
            if(password != null) {
                while (i < password.length()) {
                    passwordStars.append("*");
                    i++;
                }
            }
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Successfully connected to SQL Server at " + databaseName + " with username " + username + " and password " + passwordStars.toString() + ChatColor.RESET);
            if(!properties.getProperty("mysql.setup").equalsIgnoreCase("true")){
                try {
                    connection.prepareStatement("CREATE TABLE IF NOT EXISTS USERS (UserID VARCHAR(15) NOT NULL, Password VARCHAR(15) NOT NULL);").execute();
                    System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Successfully created user database USERS." + ChatColor.RESET);
                    connection.prepareStatement("CREATE TABLE IF NOT EXISTS PERMISSIONS (UserID VARCHAR(15) NOT NULL, PermissionsList VARCHAR(10000) NOT NULL);").execute();
                    System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Successfully created user database PERMISSIONS." + ChatColor.RESET);
                    connection.close();
                    properties.setProperty("mysql.setup", "true");
                    properties.store(new FileOutputStream("server.properties"), "");
                }
                catch(Exception e){
                    System.out.println("[ERROR] Couldn't setup database!");
                    System.out.println("[ERROR] The stack trace is displayed below:\n");
                    e.printStackTrace();
                    System.out.println("[ERROR] Skipping this issue and continuing...");
                }
            }
        }
        catch(Exception e){
            System.out.println("[ERROR] Couldn't connect to SQL server! Are IP and Port entered correctly?");
            System.out.println("[ERROR] The stack trace is displayed below:\n");
            e.printStackTrace();
            System.out.println("\n");
            System.out.println("[ERROR] Skipping this issue and continuing...");
        }
    }
    public static void setUserPermission(String username, String userPermission){
        try{
            Class.forName("org.sqlite.JDBC"); // TODO FIX THIS METHOD
            Connection connection = DriverManager.getConnection("jdbc:sqlite:androidrpgserver.db");
            ResultSet resultSet = connection.prepareStatement("SELECT PermissionsList FROM PERMISSIONS WHERE UserID=" + "'" + username + "'").executeQuery();
            String currentPermissions = resultSet.toString();
            StringBuilder stringBuilder = new StringBuilder().append(currentPermissions).append(",").append(" ").append(userPermission);
            String permissions = stringBuilder.toString();
            //currentPermissions = connection. <- get permissionList then append
            connection.prepareStatement("INSERT INTO VALUES PERMISSIONS (" + "'" + username + "'" + ", " + "'" + permissions + "'" + ");").execute();
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Permission " + userPermission + " successfully added to user " + username + ChatColor.RESET);
            connection.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void createPermissions(String userID, String password){
        try{
            // TODO MAKE PASSWORD SECURE
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:androidrpgserver.db");
            connection.prepareStatement("INSERT INTO USERS (UserID, Password) VALUES (" + "'" + userID + "'" + ", " + "'" + password + "'" + ");").execute();
            System.out.println("[Server] " + userID + "\'s username and password were saved to USERS.");
            connection.prepareStatement("INSERT INTO PERMISSIONS (UserID, PermissionsList) VALUES (" + "'" + userID + "'" + ", " + "'permissionsCreated:true');").execute();
            System.out.println("[Server] " + userID + "\'s Permissions were saved to PERMISSIONS.");
            connection.close();
        }
        catch(Exception e){
            System.out.println("[Server] Error! Couldn\'t create permissions! The stacktrace is displayed below:");
            e.printStackTrace();
        }
    }
    public static boolean checkUserPermissions(String userID){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:androidrpgserver.db");
            ResultSet resultSet1 = connection.prepareStatement("SELECT UserID FROM USERS WHERE UserID=" + "'" + userID + "'").executeQuery();
            String rs1 = resultSet1.getString(1);
            ResultSet resultSet2 = connection.prepareStatement("SELECT PermissionsList from PERMISSIONS WHERE UserID=" + "'" + userID + "'").executeQuery();
            String rs2 = resultSet2.getString(1);
            connection.close();
            if(rs1 != null && rs2 != null) {
                return true;
            }
            else{
                return false; // TODO Fix this method
            }
        }
        catch(Exception e){
            return false;
        }
    }
    public static String displayUserPermissions(String userID){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:androidrpgserver.db");
            ResultSet permissionList = connection.prepareStatement("SELECT PermissionsList from PERMISSIONS WHERE UserID=" + "'" + userID + "'").executeQuery();
            String s = permissionList.getString(1);
            connection.close();
            return s;
        }
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            return "[Server] Cannot find user permissions! Does the user exist in the database?\n" + exceptionAsString;
        }
    }
}