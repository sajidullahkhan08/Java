package model;

public class Doctor {
    private int doctorId;
    private String doctorName;
    private int diseaseId;
    private String diseaseName; // For display purposes
    
    public Doctor() {}
    
    public Doctor(int doctorId, String doctorName, int diseaseId) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.diseaseId = diseaseId;
    }
    
    // Getters and Setters
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public int getDiseaseId() { return diseaseId; }
    public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId; }
    
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    
    @Override
    public String toString() {
        return doctorName + (diseaseName != null ? " (" + diseaseName + ")" : "");
    }
}