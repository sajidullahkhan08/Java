package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String URL = "jdbc:mysql://localhost:3306/patient_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Zama SQL";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // If database doesn't exist, create it
            if (e.getMessage().contains("Unknown database")) {
                createDatabase();
                return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } else {
                throw e;
            }
        }
    }

    private static void createDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS patient_db");
            // Create tables
            stmt.executeUpdate("USE patient_db");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Disease (" +
                "Disease_ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "Disease_Name VARCHAR(100) NOT NULL UNIQUE, " +
                "Disease_Description TEXT)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Doctor (" +
                "Doctor_ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "Disease_ID INT NOT NULL, " +
                "Doctor_Name VARCHAR(100) NOT NULL, " +
                "FOREIGN KEY (Disease_ID) REFERENCES Disease(Disease_ID))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Patient (" +
                "Patient_ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "Patient_Name VARCHAR(100) NOT NULL, " +
                "PF_Name VARCHAR(100), " +
                "Sex ENUM('Male', 'Female') NOT NULL, " +
                "DOB DATE, " +
                "Doctor_ID INT, " +
                "Disease_History TEXT, " +
                "Prescription TEXT, " +
                "FOREIGN KEY (Doctor_ID) REFERENCES Doctor(Doctor_ID))");
        }
    }
}