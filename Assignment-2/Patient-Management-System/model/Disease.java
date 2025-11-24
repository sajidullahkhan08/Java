package model;

public class Disease {
    private int diseaseId;
    private String diseaseName;
    private String diseaseDescription;
    
    public Disease() {}
    
    public Disease(int diseaseId, String diseaseName, String diseaseDescription) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.diseaseDescription = diseaseDescription;
    }
    
    // Getters and Setters
    public int getDiseaseId() { return diseaseId; }
    public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId; }
    
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    
    public String getDiseaseDescription() { return diseaseDescription; }
    public void setDiseaseDescription(String diseaseDescription) { this.diseaseDescription = diseaseDescription; }
    
    @Override
    public String toString() {
        return diseaseName;
    }
}