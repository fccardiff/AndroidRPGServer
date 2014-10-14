package com.fccardiff.androidrpgserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main
{
    // TODO: GIVE OPTION TO EASILY CHOOSE SQL OR SQLITE
    // TODO: TEST SQLITE!
    // TODO: SETUP USERNAME/PASSWORD FOR SQLITE, TEST DATABASE CONNECTIONS, INSERT INTO TABLES, ETC.
    public static void main(String[] args)
    {
        try
        {
            int port = 2525;
            ServerSocket server = new ServerSocket(port);
            Scanner in = new Scanner(System.in);
            System.out.println(ChatColor.WHITE + "----------------------- " + ChatColor.BOLD + ChatColor.YELLOW + "[" + ChatColor.RED + " AndroidRPG Server " + ChatColor.YELLOW + "]" + ChatColor.RESET + ChatColor.WHITE + "-----------------------" + ChatColor.RESET);
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.CYAN + "Getting properties." + ChatColor.RESET);
            java.util.Properties properties = new java.util.Properties();
            if(!new File("server.properties").exists()){
                properties.store(new FileOutputStream("server.properties"), "");
            }
            properties.load(new FileInputStream("server.properties"));
            if(properties.getProperty("propertySetup") == null || properties.getProperty("propertySetup").equalsIgnoreCase("false")){
                properties.setProperty("propertySetup", "true");
                properties.setProperty("mysql.ip", "192.168.1.1");
                properties.setProperty("mysql.port", "3306");
                properties.setProperty("mysql.username", "username");
                properties.setProperty("mysql.password", "password");
                properties.setProperty("mysql.setup", "false");
                properties.setProperty("mysql.databaseName", "androidrpgserver");
                properties.store(new FileOutputStream("server.properties"), "");
            }
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Properties file loaded." + ChatColor.RESET);
            System.out.println(properties.values());
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.CYAN + "Initiating SQL Connection..." + ChatColor.RESET);
            SQLConnection.connect();
            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "Started. Now accepting clients." + ChatColor.RESET);
            while (true) {
                if(in.hasNextLine()){
                    String commandMessage = in.nextLine();
                    if (commandMessage.equalsIgnoreCase("stop")) {
                        System.out.println(ChatColor.SERVERMESSAGE + ChatColor.YELLOW + ChatColor.ITALIC + "Stopping server..." + ChatColor.RESET);
                        System.out.println(ChatColor.SERVERMESSAGE + ChatColor.YELLOW + ChatColor.ITALIC + "Server stopped." + ChatColor.RESET);
                        break;
                    }
                    else if(commandMessage.contains("admin")) {
                        int length = commandMessage.length();
                        String user = commandMessage.substring(6, length);
                        SQLConnection.setUserPermission(user, "hasAdmin:true");
                        continue;
                    }
                    else if(commandMessage.equalsIgnoreCase("help")){
                        System.out.println(ChatColor.GREEN + "Commands available: help, admin, stop, permissions, create" + ChatColor.RESET);
                        continue;
                    }
                    else if(commandMessage.contains("permissions")){
                        int length = commandMessage.length();
                        String user = commandMessage.substring(12, length);
                        System.out.println(SQLConnection.displayUserPermissions(user));
                        continue;
                    }
                    else if(commandMessage.contains("create")){
                        int length = commandMessage.length();
                        String user = commandMessage.substring(7, length);
                        if(SQLConnection.checkUserPermissions(user) == false) {
                            SQLConnection.createPermissions(user, "password");
                            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.GREEN + "User " + user + "\'s permissions were set." + ChatColor.RESET);
                        }
                        else{
                            System.out.println(ChatColor.SERVERMESSAGE + ChatColor.RED + "Permissions not created! User already has been created!" + ChatColor.RESET);
                        }
                        continue;
                    }
                    else{
                        System.out.println(ChatColor.SERVERMESSAGE + ChatColor.RED + "Incorrect command specified!" + ChatColor.RESET);
                        System.out.println(ChatColor.SERVERMESSAGE + ChatColor.RED + "Commands available: help, admin, stop, permissions, create" + ChatColor.RESET);
                        continue;
                    }
                }
                Socket socket = server.accept();
                System.out.println(ChatColor.SERVERMESSAGE + ChatColor.YELLOW + "Client connected from: " + socket.getLocalAddress() + ":" + socket.getLocalPort() + ChatColor.RESET);
                Client chat = new Client(socket);
                Thread thread = new Thread(chat);
                thread.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}