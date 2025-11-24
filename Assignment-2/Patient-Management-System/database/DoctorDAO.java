package database;

import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    
    public static boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor (doctor_name, disease_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, doctor.getDoctorName());
            pstmt.setInt(2, doctor.getDiseaseId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = """
            SELECT d.doctor_id, d.doctor_name, d.disease_id, dis.disease_name 
            FROM Doctor d 
            LEFT JOIN Disease dis ON d.disease_id = dis.disease_id 
            ORDER BY d.doctor_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getInt("disease_id")
                );
                doctor.setDiseaseName(rs.getString("disease_name"));
                doctors.add(doctor);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting doctors: " + e.getMessage());
        }
        
        return doctors;
    }
    
    public static Doctor getDoctorById(int doctorId) {
        String sql = """
            SELECT d.doctor_id, d.doctor_name, d.disease_id, dis.disease_name 
            FROM Doctor d 
            LEFT JOIN Disease dis ON d.disease_id = dis.disease_id 
            WHERE d.doctor_id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getInt("disease_id")
                );
                doctor.setDiseaseName(rs.getString("disease_name"));
                return doctor;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting doctor by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public static List<Doctor> getDoctorsByName(String name) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = """
            SELECT d.doctor_id, d.doctor_name, d.disease_id, dis.disease_name 
            FROM Doctor d 
            LEFT JOIN Disease dis ON d.disease_id = dis.disease_id 
            WHERE d.doctor_name LIKE ?
            ORDER BY d.doctor_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getInt("disease_id")
                );
                doctor.setDiseaseName(rs.getString("disease_name"));
                doctors.add(doctor);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting doctors by name: " + e.getMessage());
        }
        
        return doctors;
    }
    
    public static List<Doctor> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = """
            SELECT d.doctor_id, d.doctor_name, d.disease_id, dis.disease_name 
            FROM Doctor d 
            LEFT JOIN Disease dis ON d.disease_id = dis.disease_id 
            WHERE dis.disease_name LIKE ?
            ORDER BY d.doctor_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + specialization + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getInt("disease_id")
                );
                doctor.setDiseaseName(rs.getString("disease_name"));
                doctors.add(doctor);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting doctors by specialization: " + e.getMessage());
        }
        
        return doctors;
    }
}