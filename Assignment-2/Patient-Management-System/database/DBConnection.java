package database;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:patient_management.db";
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                System.out.println("Database connection established successfully.");
                initializeDatabase(); // Create tables if they don't exist
            } catch (Exception e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS Users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('Administrator', 'Guest'))
                )
            """;
            
            // Create Disease table
            String createDiseaseTable = """
                CREATE TABLE IF NOT EXISTS Disease (
                    disease_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    disease_name VARCHAR(100) UNIQUE NOT NULL,
                    disease_description TEXT
                )
            """;
            
            // Create Doctor table
            String createDoctorTable = """
                CREATE TABLE IF NOT EXISTS Doctor (
                    doctor_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    doctor_name VARCHAR(100) NOT NULL,
                    disease_id INTEGER,
                    FOREIGN KEY (disease_id) REFERENCES Disease(disease_id)
                )
            """;
            
            // Create Patient table
            String createPatientTable = """
                CREATE TABLE IF NOT EXISTS Patient (
                    patient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_name VARCHAR(100) NOT NULL,
                    pf_name VARCHAR(100) NOT NULL,
                    sex VARCHAR(10) CHECK (sex IN ('Male', 'Female')),
                    dob DATE NOT NULL,
                    doctor_id INTEGER,
                    disease_history TEXT,
                    prescription TEXT,
                    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
                )
            """;
            
            // Execute table creation
            stmt.execute(createUsersTable);
            stmt.execute(createDiseaseTable);
            stmt.execute(createDoctorTable);
            stmt.execute(createPatientTable);
            
            // Insert default users
            insertDefaultUsers();
            
            System.out.println("Database initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertDefaultUsers() {
        String checkUsers = "SELECT COUNT(*) FROM Users";
        String insertAdmin = "INSERT OR IGNORE INTO Users (username, password, user_type) VALUES ('admin', 'admin123', 'Administrator')";
        String insertGuest = "INSERT OR IGNORE INTO Users (username, password, user_type) VALUES ('guest', 'guest123', 'Guest')";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkUsers)) {
            
            if (rs.getInt(1) == 0) {
                stmt.execute(insertAdmin);
                stmt.execute(insertGuest);
                System.out.println("Default users created: admin/admin123 (Administrator), guest/guest123 (Guest)");
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting default users: " + e.getMessage());
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}