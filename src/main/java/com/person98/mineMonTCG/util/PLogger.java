package com.person98.mineMonTCG.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PLogger {
    private static Logger logger;
    private static String prefix;
    private static ConsoleColor color;

    public static void setup(String loggerPrefix, ConsoleColor loggerColor) {
        // Set system property to force Log4j2 to use our configuration
        System.setProperty("log4j2.configurationFile", "log4j2.xml");

        // Initialize logger
        logger = LogManager.getLogger("MineMonTCG");
        prefix = loggerPrefix;
        color = loggerColor;

        // Disable other logging systems
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.LoggerFactory.getLogger("org.slf4j").atInfo().setMessage("").log();
    }

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void warning(String message) {
        log(LogLevel.WARN, message);
    }

    public static void severe(String message) {
        log(LogLevel.ERROR, message);
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    private static void log(LogLevel level, String message) {
        if (logger == null) {
            throw new IllegalStateException("PLogger has not been set up. Call setup() first.");
        }

        String formattedMessage = color + "[" + prefix + "] " + ConsoleColor.WHITE + message;

        switch (level) {
            case DEBUG -> logger.debug(formattedMessage);
            case INFO -> logger.info(formattedMessage);
            case WARN -> logger.warn(formattedMessage);
            case ERROR -> logger.error(formattedMessage);
        }
    }

    private enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    public enum ConsoleColor {
        BLACK("\u001b[30m"),
        RED("\u001b[31m"),
        GREEN("\u001b[32m"),
        YELLOW("\u001b[33m"),
        BLUE("\u001b[34m"),
        PURPLE("\u001b[35m"),
        CYAN("\u001b[36m"),
        WHITE("\u001b[37m"),
        BRIGHT_RED("\u001b[91m"),
        BRIGHT_GREEN("\u001b[92m");

        private final String code;

        ConsoleColor(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}