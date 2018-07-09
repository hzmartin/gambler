package org.scoreboard.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.scoreboard.exception.ConfigurationException;

public class SysConfig {

    private static Properties configuration = new Properties();

    static Logger logger = Logger.getLogger(SysConfig.class);

    public SysConfig() {}

    public static Properties getProperties() {
        return configuration;
    }

    public SysConfig(String fileName) throws ConfigurationException {
        load(fileName);
    }

    public static void load(String fileName) throws ConfigurationException {
        try {
            configuration.loadFromXML(ClassLoader
                .getSystemResourceAsStream(fileName));
        } catch (IOException e) {
            String message = "I/O error while reading configuration file "
                + fileName;
            throw new ConfigurationException(message, e);
        }
    }

    public static void load(FileInputStream fs) throws ConfigurationException {
        try {
            configuration.loadFromXML(fs);
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    public static void load(InputStream fs) throws ConfigurationException {
        try {
            configuration.loadFromXML(fs);
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }

    public static void load(String fileName, String defaultFileName)
        throws ConfigurationException {
        try {
            load(fileName);
        } catch (ConfigurationException e) {
            load(defaultFileName);
        }
    }

    public static void store(String fileName) throws ConfigurationException {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            configuration.storeToXML(file, "");
            file.close();
        } catch (Exception e) {
            throw new ConfigurationException("Failed to save configurations.");
        }
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        try {
            return Integer.valueOf(configuration.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public static void setInteger(String key, Integer value) {
        configuration.setProperty(key, value.toString());
    }

    public static Long getLong(String key, Long defaultValue) {
        try {
            return Long.valueOf(configuration.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static void setLong(String key, Long value) {
        configuration.setProperty(key, value.toString());
    }

    public static Double getDouble(String key, Double defaultValue) {
        try {
            return Double.valueOf(configuration.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double getDouble(String key) {
        return getDouble(key, null);
    }

    public static void setDouble(String key, Double value) {
        configuration.setProperty(key, value.toString());
    }

    public static String getString(String key, String defaultValue) {
        String result = configuration.getProperty(key);
        return result == null ? defaultValue : result;
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static void setString(String key, String value) {
        configuration.setProperty(key, value);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String value = configuration.getProperty(key);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    public static Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public static void setBoolean(String key, Boolean value) {
        configuration.setProperty(key, value.toString());
    }
}
