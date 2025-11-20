package database;

import model.Disease;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiseaseDAO {
    
    public static boolean addDisease(Disease disease) {
        String sql = "INSERT INTO Disease (disease_name, disease_description) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, disease.getDiseaseName());
            pstmt.setString(2, disease.getDiseaseDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding disease: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Disease> getAllDiseases() {
        List<Disease> diseases = new ArrayList<>();
        String sql = "SELECT * FROM Disease ORDER BY disease_name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Disease disease = new Disease(
                    rs.getInt("disease_id"),
                    rs.getString("disease_name"),
                    rs.getString("disease_description")
                );
                diseases.add(disease);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting diseases: " + e.getMessage());
        }
        
        return diseases;
    }
    
    public static Disease getDiseaseById(int diseaseId) {
        String sql = "SELECT * FROM Disease WHERE disease_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, diseaseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Disease(
                    rs.getInt("disease_id"),
                    rs.getString("disease_name"),
                    rs.getString("disease_description")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting disease by ID: " + e.getMessage());
        }
        
        return null;
    }
}