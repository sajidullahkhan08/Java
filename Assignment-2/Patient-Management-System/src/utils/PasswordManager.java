package utils;

import java.io.*;
import java.util.Properties;

public class PasswordManager {
    private static final String PASSWORD_FILE = "passwords.properties";
    private static Properties properties = new Properties();

    static {
        loadPasswords();
    }

    private static void loadPasswords() {
        try (FileInputStream fis = new FileInputStream(PASSWORD_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            // If file doesn't exist, set defaults
            properties.setProperty("admin", "admin");
            properties.setProperty("guest", "guest");
            savePasswords();
        }
    }

    private static void savePasswords() {
        try (FileOutputStream fos = new FileOutputStream(PASSWORD_FILE)) {
            properties.store(fos, "User Passwords");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPassword(String username) {
        return properties.getProperty(username);
    }

    public static void setPassword(String username, String password) {
        properties.setProperty(username, password);
        savePasswords();
    }

    public static boolean validatePassword(String username, String password) {
        String stored = getPassword(username);
        return stored != null && stored.equals(password);
    }
}