package model;

public class Doctor {
    private int id;
    private int diseaseId;
    private String name;
    private String diseaseName; // for display in search

    // Constructors
    public Doctor() {}
    public Doctor(String name, int diseaseId) {
        this.name = name;
        this.diseaseId = diseaseId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDiseaseId() { return diseaseId; }
    public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }

    @Override
    public String toString() {
        return name + " (" + diseaseName + ")";
    }
}