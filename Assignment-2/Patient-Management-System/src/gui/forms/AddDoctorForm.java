package gui.forms;

import database.DiseaseDAO;
import database.DoctorDAO;
import model.Disease;
import model.Doctor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddDoctorForm extends JFrame {
    private JTextField nameField;
    private JComboBox<Disease> diseaseCombo;

    public AddDoctorForm() {
        setTitle("Add New Doctor");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Doctor Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Disease Specialization:"));
        diseaseCombo = new JComboBox<>();
        loadDiseases();
        add(diseaseCombo);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> saveDoctor());
        add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);
    }

    private void loadDiseases() {
        try {
            List<Disease> diseases = new DiseaseDAO().getAll();
            for (Disease d : diseases) {
                diseaseCombo.addItem(d);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading diseases: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDoctor() {
        String name = nameField.getText().trim();
        Disease disease = (Disease) diseaseCombo.getSelectedItem();

        if (name.isEmpty() || disease == null) {
            JOptionPane.showMessageDialog(this, "All fields required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Doctor doctor = new Doctor(name, disease.getId());
            new DoctorDAO().save(doctor);
            JOptionPane.showMessageDialog(this, "Doctor saved!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}