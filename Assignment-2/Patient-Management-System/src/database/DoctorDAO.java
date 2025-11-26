package database;

import model.Doctor;
import model.Disease;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    public void save(Doctor d) throws SQLException {
        String sql = "INSERT INTO Doctor (Disease_ID, Doctor_Name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getDiseaseId());
            ps.setString(2, d.getName());
            ps.executeUpdate();
        }
    }

    public List<Doctor> getAllWithDiseaseName() throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT d.Doctor_ID, d.Doctor_Name, dis.Disease_Name " +
                     "FROM Doctor d JOIN Disease dis ON d.Disease_ID = dis.Disease_ID " +
                     "ORDER BY d.Doctor_Name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Doctor doc = new Doctor();
                doc.setId(rs.getInt("Doctor_ID"));
                doc.setName(rs.getString("Doctor_Name"));
                doc.setDiseaseName(rs.getString("Disease_Name"));
                list.add(doc);
            }
        }
        return list;
    }

    public List<Doctor> findByDiseaseId(int diseaseId) throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT d.Doctor_ID, d.Doctor_Name, dis.Disease_Name " +
                     "FROM Doctor d JOIN Disease dis ON d.Disease_ID = dis.Disease_ID " +
                     "WHERE d.Disease_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diseaseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doc = new Doctor();
                    doc.setId(rs.getInt("Doctor_ID"));
                    doc.setName(rs.getString("Doctor_Name"));
                    doc.setDiseaseName(rs.getString("Disease_Name"));
                    list.add(doc);
                }
            }
        }
        return list;
    }
}