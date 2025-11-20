package gui.forms;

import model.Doctor;
import model.Disease;
import database.DoctorDAO;
import database.DiseaseDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorForm extends JDialog {
    private JTextField nameField;
    private JComboBox<Disease> diseaseCombo;
    private JButton saveButton, cancelButton;
    private boolean saved = false;
    
    public DoctorForm(JFrame parent) {
        super(parent, "Add New Doctor", true);
        initializeUI();
        loadDiseases();
        setupEventListeners();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        formPanel.add(new JLabel("Doctor Name:"));
        nameField = new JTextField(20);
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Specialization:"));
        diseaseCombo = new JComboBox<>();
        formPanel.add(diseaseCombo);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        saveButton.setBackground(new Color(0, 120, 0));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadDiseases() {
        List<Disease> diseases = DiseaseDAO.getAllDiseases();
        diseaseCombo.removeAllItems();
        
        // Add empty option
        diseaseCombo.addItem(new Disease(0, "Select Specialization", ""));
        
        for (Disease disease : diseases) {
            diseaseCombo.addItem(disease);
        }
    }
    
    private void setupEventListeners() {
        saveButton.addActionListener(e -> saveDoctor());
        cancelButton.addActionListener(e -> dispose());
        
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void saveDoctor() {
        String name = nameField.getText().trim();
        Disease selectedDisease = (Disease) diseaseCombo.getSelectedItem();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter doctor name.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        if (selectedDisease == null || selectedDisease.getDiseaseId() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a disease specialization.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            diseaseCombo.requestFocus();
            return;
        }
        
        Doctor doctor = new Doctor();
        doctor.setDoctorName(name);
        doctor.setDiseaseId(selectedDisease.getDiseaseId());
        
        if (DoctorDAO.addDoctor(doctor)) {
            JOptionPane.showMessageDialog(this,
                "Doctor added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add doctor.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}