package com.zzzcomputing.bukkit.atmcraft;

import java.util.logging.Level;

public class Logger {
    static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");

    static final String PREFIX = "[AtmCraft]";
    
    public static void severe(String string, Exception ex) {
        logLevel(Level.SEVERE, string, ex);
    }

    public static void severe(String string) {
        logLevel(Level.SEVERE, string);
    }

    public static void info(String string) {
        logLevel(Level.INFO, string);
    }

    public static void warning(String string) {
        logLevel(Level.WARNING, string);
    }

    private static void logLevel(Level loglevel, String txt) {
        logger.log(loglevel, PREFIX + " " + txt);
    }
    
    private static void logLevel(Level loglevel, String txt, Exception e) {
        logger.log(loglevel, PREFIX + " " + txt + " " + e.getMessage(), e);
    }

}
