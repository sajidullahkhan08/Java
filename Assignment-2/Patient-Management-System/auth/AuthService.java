package auth;

import model.User;
import database.DBConnection;
import java.sql.*;

public class AuthService {
    
    public static User authenticate(String username, String password, String userType) {
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
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Authentication failed
    }
    
    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Password change error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}