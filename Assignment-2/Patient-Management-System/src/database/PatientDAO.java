package database;

import model.Patient;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    public void save(Patient p) throws SQLException {
        String sql = "INSERT INTO Patient (Patient_Name, PF_Name, Sex, DOB, Doctor_ID, Disease_History, Prescription) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getFatherName());
            ps.setString(3, p.getSex());
            ps.setDate(4, p.getDob() != null ? Date.valueOf(p.getDob()) : null);
            ps.setInt(5, p.getDoctorId());
            ps.setString(6, p.getDiseaseHistory());
            ps.setString(7, p.getPrescription());
            ps.executeUpdate();
        }
    }

    public List<Patient> searchByName(String name) throws SQLException {
        return searchByField("Patient_Name LIKE ?", "%" + name + "%");
    }

    public List<Patient> searchById(int id) throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT p.*, d.Doctor_Name FROM Patient p " +
                     "LEFT JOIN Doctor d ON p.Doctor_ID = d.Doctor_ID " +
                     "WHERE p.Patient_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) addPatientToList(list, rs);
            }
        }
        return list;
    }

    public List<Patient> searchByAge(int age) throws SQLException {
        // Approximate: age = current year - birth year
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT p.*, d.Doctor_Name FROM Patient p " +
                     "LEFT JOIN Doctor d ON p.Doctor_ID = d.Doctor_ID " +
                     "WHERE TIMESTAMPDIFF(YEAR, p.DOB, CURDATE()) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, age);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) addPatientToList(list, rs);
            }
        }
        return list;
    }

    private List<Patient> searchByField(String condition, Object param) throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT p.*, d.Doctor_Name FROM Patient p " +
                     "LEFT JOIN Doctor d ON p.Doctor_ID = d.Doctor_ID " +
                     "WHERE " + condition;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) addPatientToList(list, rs);
            }
        }
        return list;
    }

    private void addPatientToList(List<Patient> list, ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getInt("Patient_ID"));
        p.setName(rs.getString("Patient_Name"));
        p.setFatherName(rs.getString("PF_Name"));
        p.setSex(rs.getString("Sex"));
        Date dob = rs.getDate("DOB");
        p.setDob(dob != null ? dob.toLocalDate() : null);
        p.setDoctorId(rs.getInt("Doctor_ID"));
        p.setDiseaseHistory(rs.getString("Disease_History"));
        p.setPrescription(rs.getString("Prescription"));
        p.setDoctorName(rs.getString("Doctor_Name"));
        list.add(p);
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Patient WHERE Patient_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Patient findById(int id) throws SQLException {
        List<Patient> list = searchById(id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Patient> findAll() throws SQLException {
        return searchByField("1=1", "");
    }
}