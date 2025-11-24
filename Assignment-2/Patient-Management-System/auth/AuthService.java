package auth;

import model.User;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    
    public static User authenticate(String username, String password, String userType) {
        // First try database authentication
        User user = authenticateViaDatabase(username, password, userType);
        if (user != null) {
            return user;
        }
        
        // Fallback to hardcoded credentials for development
        return authenticateViaHardcoded(username, password, userType);
    }
    
    private static User authenticateViaDatabase(String username, String password, String userType) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ? AND user_type = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("user_type")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database authentication error: " + e.getMessage());
            // Fall through to hardcoded authentication
        } catch (Exception e) {
            System.err.println("Unexpected authentication error: " + e.getMessage());
            // Fall through to hardcoded authentication
        }
        
        return null;
    }
    
    private static User authenticateViaHardcoded(String username, String password, String userType) {
        System.out.println("Using hardcoded authentication for: " + username);
        
        // Hardcoded credentials for development
        if ("admin".equals(username) && "admin123".equals(password) && "Administrator".equals(userType)) {
            return new User(1, "admin", "admin123", "Administrator");
        } else if ("guest".equals(username) && "guest123".equals(password) && "Guest".equals(userType)) {
            return new User(2, "guest", "guest123", "Guest");
        }
        
        return null;
    }
    
    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        // First try database update
        if (changePasswordInDatabase(username, oldPassword, newPassword)) {
            return true;
        }
        
        // Fallback to hardcoded check
        return changePasswordHardcoded(username, oldPassword, newPassword);
    }
    
    private static boolean changePasswordInDatabase(String username, String oldPassword, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Database password change error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected password change error: " + e.getMessage());
        }
        
        return false;
    }
    
    private static boolean changePasswordHardcoded(String username, String oldPassword, String newPassword) {
        // For hardcoded users, we'll simulate password change
        // In a real system, you'd update the hardcoded values
        boolean oldPasswordCorrect = ("admin".equals(username) && "admin123".equals(oldPassword)) ||
                                    ("guest".equals(username) && "guest123".equals(oldPassword));
        
        if (oldPasswordCorrect) {
            System.out.println("Simulated password change for: " + username);
            return true;
        }
        
        return false;
    }
    
    // New method to get all users (for admin purposes)
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY user_type, username";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("user_type")
                );
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        
        return users;
    }
}