package model;

import java.util.Date;

public class Patient {
    private int patientId;
    private String patientName;
    private String pfName; // Father's name
    private String sex;
    private Date dob;
    private int doctorId;
    private String diseaseHistory;
    private String prescription;
    
    // For display purposes
    private String doctorName;
    private int age;
    
    public Patient() {}
    
    public Patient(int patientId, String patientName, String pfName, String sex, Date dob, 
                   int doctorId, String diseaseHistory, String prescription) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.pfName = pfName;
        this.sex = sex;
        this.dob = dob;
        this.doctorId = doctorId;
        this.diseaseHistory = diseaseHistory;
        this.prescription = prescription;
    }
    
    // Getters and Setters
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getPfName() { return pfName; }
    public void setPfName(String pfName) { this.pfName = pfName; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getDiseaseHistory() { return diseaseHistory; }
    public void setDiseaseHistory(String diseaseHistory) { this.diseaseHistory = diseaseHistory; }
    
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}