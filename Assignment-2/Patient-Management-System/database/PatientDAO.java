package database;

import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    public static boolean addPatient(Patient patient) {
        String sql = "INSERT INTO Patient (patient_name, pf_name, sex, dob, doctor_id, disease_history, prescription) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getPatientName());
            pstmt.setString(2, patient.getPfName());
            pstmt.setString(3, patient.getSex());
            pstmt.setDate(4, new java.sql.Date(patient.getDob().getTime()));
            pstmt.setInt(5, patient.getDoctorId());
            pstmt.setString(6, patient.getDiseaseHistory());
            pstmt.setString(7, patient.getPrescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET disease_history = ?, prescription = ? WHERE patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getDiseaseHistory());
            pstmt.setString(2, patient.getPrescription());
            pstmt.setInt(3, patient.getPatientId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deletePatient(int patientId) {
        String sql = "DELETE FROM Patient WHERE patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }
    
    public static Patient getPatientById(int patientId) {
        String sql = """
            SELECT p.*, d.doctor_name, 
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as age
            FROM Patient p 
            LEFT JOIN Doctor d ON p.doctor_id = d.doctor_id 
            WHERE p.patient_id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("age"));
                return patient;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting patient by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public static List<Patient> getPatientsByName(String name) {
        List<Patient> patients = new ArrayList<>();
        String sql = """
            SELECT p.*, d.doctor_name,
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as age
            FROM Patient p 
            LEFT JOIN Doctor d ON p.doctor_id = d.doctor_id 
            WHERE p.patient_name LIKE ?
            ORDER BY p.patient_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("age"));
                patients.add(patient);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting patients by name: " + e.getMessage());
        }
        
        return patients;
    }
    
    public static List<Patient> getPatientsByAge(int age) {
        List<Patient> patients = new ArrayList<>();
        // This is a simplified age calculation - in production you'd want more precise calculation
        String sql = """
            SELECT p.*, d.doctor_name,
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as calculated_age
            FROM Patient p 
            LEFT JOIN Doctor d ON p.doctor_id = d.doctor_id 
            WHERE calculated_age = ?
            ORDER BY p.patient_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, age);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("calculated_age"));
                patients.add(patient);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting patients by age: " + e.getMessage());
        }
        
        return patients;
    }
    
    public static List<Patient> getPatientsByDisease(String diseaseName) {
        List<Patient> patients = new ArrayList<>();
        String sql = """
            SELECT p.*, d.doctor_name,
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as age
            FROM Patient p 
            LEFT JOIN Doctor doc ON p.doctor_id = doc.doctor_id 
            LEFT JOIN Disease dis ON doc.disease_id = dis.disease_id
            WHERE dis.disease_name LIKE ?
            ORDER BY p.patient_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + diseaseName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("age"));
                patients.add(patient);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting patients by disease: " + e.getMessage());
        }
        
        return patients;
    }
    
    public static List<Patient> getPatientsByDoctor(String doctorName) {
        List<Patient> patients = new ArrayList<>();
        String sql = """
            SELECT p.*, d.doctor_name,
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as age
            FROM Patient p 
            LEFT JOIN Doctor d ON p.doctor_id = d.doctor_id 
            WHERE d.doctor_name LIKE ?
            ORDER BY p.patient_name
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + doctorName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("age"));
                patients.add(patient);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting patients by doctor: " + e.getMessage());
        }
        
        return patients;
    }
    
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = """
            SELECT p.*, d.doctor_name,
                   (strftime('%Y', 'now') - strftime('%Y', p.dob)) - 
                   (strftime('%m-%d', 'now') < strftime('%m-%d', p.dob)) as age
            FROM Patient p 
            LEFT JOIN Doctor d ON p.doctor_id = d.doctor_id 
            ORDER BY p.patient_id
            """;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("pf_name"),
                    rs.getString("sex"),
                    rs.getDate("dob"),
                    rs.getInt("doctor_id"),
                    rs.getString("disease_history"),
                    rs.getString("prescription")
                );
                patient.setDoctorName(rs.getString("doctor_name"));
                patient.setAge(rs.getInt("age"));
                patients.add(patient);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all patients: " + e.getMessage());
        }
        
        return patients;
    }
}