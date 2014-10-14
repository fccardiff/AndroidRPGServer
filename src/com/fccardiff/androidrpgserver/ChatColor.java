package com.fccardiff.androidrpgserver;

/**
 * Created by fccardiff on 10/11/14.
 */
public class ChatColor {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m"; //TODO Fix
    public static final String UNDERLINE = "\u001B[4m"; //TODO Fix
    public static final String STRIKETHROUGH = "\u001B[9m"; //TODO Fix
    public static final String SERVERMESSAGE = ChatColor.RED + ChatColor.BOLD + "[Server] " + ChatColor.RESET;
}
