package gui.forms;

import model.Patient;
import model.Doctor;
import database.PatientDAO;
import database.DoctorDAO;
import utils.ValidationUtility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientForm extends JDialog {
    private JTextField patientIdField, patientNameField, fatherNameField;
    private JRadioButton maleRadio, femaleRadio;
    private JTextField dobField; // Using text field as fallback for date input
    private JComboBox<Doctor> doctorCombo;
    private JTextArea diseaseHistoryArea, prescriptionArea;
    private JButton saveButton, cancelButton;
    private boolean saved = false;
    private boolean updateMode = false;
    private Patient existingPatient;
    
    public PatientForm(JFrame parent) {
        super(parent, "Add New Patient", true);
        initializeUI();
        loadDoctors();
        setupEventListeners();
        pack();
        setLocationRelativeTo(parent);
        setSize(600, 500);
    }
    
    public PatientForm(JFrame parent, Patient patient) {
        super(parent, "Update Patient Record", true);
        this.existingPatient = patient;
        this.updateMode = true;
        initializeUI();
        loadDoctors();
        populateForm();
        setupEventListeners();
        pack();
        setLocationRelativeTo(parent);
        setSize(600, 500);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Patient ID (non-editable in update mode)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Patient ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        patientIdField = new JTextField(20);
        patientIdField.setEditable(false);
        if (!updateMode) {
            patientIdField.setText("Auto-generated");
        }
        formPanel.add(patientIdField, gbc);
        
        // Patient Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Patient Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        patientNameField = new JTextField(20);
        if (updateMode) patientNameField.setEditable(false);
        formPanel.add(patientNameField, gbc);
        
        // Father Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Father Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        fatherNameField = new JTextField(20);
        if (updateMode) fatherNameField.setEditable(false);
        formPanel.add(fatherNameField, gbc);
        
        // Sex (Radio buttons)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Sex:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        JPanel sexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup sexGroup = new ButtonGroup();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        sexGroup.add(maleRadio);
        sexGroup.add(femaleRadio);
        sexPanel.add(maleRadio);
        sexPanel.add(femaleRadio);
        if (updateMode) {
            maleRadio.setEnabled(false);
            femaleRadio.setEnabled(false);
        }
        formPanel.add(sexPanel, gbc);
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Date of Birth (DD-MM-YYYY):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        dobField = new JTextField(20);
        if (updateMode) dobField.setEditable(false);
        formPanel.add(dobField, gbc);
        
        // Doctor Name
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Doctor:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        doctorCombo = new JComboBox<>();
        if (updateMode) doctorCombo.setEnabled(false);
        formPanel.add(doctorCombo, gbc);
        
        // Disease History
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Disease History:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        diseaseHistoryArea = new JTextArea(3, 20);
        diseaseHistoryArea.setLineWrap(true);
        diseaseHistoryArea.setWrapStyleWord(true);
        JScrollPane historyScroll = new JScrollPane(diseaseHistoryArea);
        formPanel.add(historyScroll, gbc);
        
        // Prescription
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Prescription:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        gbc.weighty = 1.0;
        prescriptionArea = new JTextArea(3, 20);
        prescriptionArea.setLineWrap(true);
        prescriptionArea.setWrapStyleWord(true);
        JScrollPane prescriptionScroll = new JScrollPane(prescriptionArea);
        formPanel.add(prescriptionScroll, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton(updateMode ? "Update" : "Save");
        cancelButton = new JButton("Cancel");
        
        saveButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadDoctors() {
        List<Doctor> doctors = DoctorDAO.getAllDoctors();
        doctorCombo.removeAllItems();
        
        // Add empty option
        doctorCombo.addItem(new Doctor(0, "Select Doctor", 0));
        
        for (Doctor doctor : doctors) {
            doctorCombo.addItem(doctor);
        }
    }
    
    private void populateForm() {
        if (existingPatient != null) {
            patientIdField.setText(String.valueOf(existingPatient.getPatientId()));
            patientNameField.setText(existingPatient.getPatientName());
            fatherNameField.setText(existingPatient.getPfName());
            
            // Set sex
            if ("Male".equals(existingPatient.getSex())) {
                maleRadio.setSelected(true);
            } else if ("Female".equals(existingPatient.getSex())) {
                femaleRadio.setSelected(true);
            }
            
            // Set date of birth
            if (existingPatient.getDob() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                dobField.setText(sdf.format(existingPatient.getDob()));
            }
            
            // Set doctor
            for (int i = 0; i < doctorCombo.getItemCount(); i++) {
                Doctor doctor = doctorCombo.getItemAt(i);
                if (doctor.getDoctorId() == existingPatient.getDoctorId()) {
                    doctorCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            diseaseHistoryArea.setText(existingPatient.getDiseaseHistory());
            prescriptionArea.setText(existingPatient.getPrescription());
        }
    }
    
    private void setupEventListeners() {
        saveButton.addActionListener(e -> savePatient());
        cancelButton.addActionListener(e -> dispose());
        
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void savePatient() {
        // Validation
        if (patientNameField.getText().trim().isEmpty()) {
            showValidationError("Please enter patient name.");
            patientNameField.requestFocus();
            return;
        }
        
        if (fatherNameField.getText().trim().isEmpty()) {
            showValidationError("Please enter father's name.");
            fatherNameField.requestFocus();
            return;
        }
        
        if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
            showValidationError("Please select patient's sex.");
            return;
        }
        
        if (dobField.getText().trim().isEmpty()) {
            showValidationError("Please enter date of birth.");
            dobField.requestFocus();
            return;
        }
        
        Doctor selectedDoctor = (Doctor) doctorCombo.getSelectedItem();
        if (selectedDoctor == null || selectedDoctor.getDoctorId() == 0) {
            showValidationError("Please select a doctor.");
            doctorCombo.requestFocus();
            return;
        }
        
        // Parse date
        Date dob;
        try {
            dob = ValidationUtility.parseDate(dobField.getText().trim());
        } catch (Exception e) {
            showValidationError("Please enter date in DD-MM-YYYY format (e.g., 15-05-1990).");
            dobField.requestFocus();
            return;
        }
        
        // Create patient object
        Patient patient;
        if (updateMode) {
            patient = existingPatient;
            patient.setDiseaseHistory(diseaseHistoryArea.getText().trim());
            patient.setPrescription(prescriptionArea.getText().trim());
        } else {
            patient = new Patient();
            patient.setPatientName(patientNameField.getText().trim());
            patient.setPfName(fatherNameField.getText().trim());
            patient.setSex(maleRadio.isSelected() ? "Male" : "Female");
            patient.setDob(dob);
            patient.setDoctorId(selectedDoctor.getDoctorId());
            patient.setDiseaseHistory(diseaseHistoryArea.getText().trim());
            patient.setPrescription(prescriptionArea.getText().trim());
        }
        
        // Save to database
        boolean success;
        if (updateMode) {
            success = PatientDAO.updatePatient(patient);
        } else {
            success = PatientDAO.addPatient(patient);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                updateMode ? "Patient record updated successfully!" : "Patient added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                updateMode ? "Failed to update patient record." : "Failed to add patient.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    
    public boolean isSaved() {
        return saved;
    }
}