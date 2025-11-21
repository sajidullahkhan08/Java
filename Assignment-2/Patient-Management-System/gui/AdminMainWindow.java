package gui;

import model.Disease;
import model.Doctor;
import model.Patient;
import model.User;
import gui.forms.DiseaseForm;
import gui.forms.DoctorForm;
import gui.forms.PatientForm;
import gui.forms.SearchResultsDialog;

import javax.swing.*;

import auth.AuthService;
import database.DiseaseDAO;
import database.DoctorDAO;
import database.PatientDAO;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminMainWindow extends JFrame {
    private User currentUser;
    
    public AdminMainWindow(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Patient Management System - Administrator: " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set application icon (using standard Java icons)
        // setIconImage(getToolkit().getImage(ClassLoader.getSystemResource("icons/application.png")));
        
        createMenuBar();
        createToolBar();
        createMainContent();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Manage Record Menu
        JMenu manageMenu = new JMenu("Manage Record");
        
        JMenuItem addPatientItem = new JMenuItem("Add New Patient");
        JMenuItem addDoctorItem = new JMenuItem("Add New Doctor");
        JMenuItem addDiseaseItem = new JMenuItem("Add New Disease");
        JMenuItem deletePatientItem = new JMenuItem("Delete Patient Record");
        
        // Update Record Submenu
        JMenu updateSubMenu = new JMenu("Update Record");
        JMenuItem updatePatientItem = new JMenuItem("Update Patient");
        JMenuItem updateDoctorItem = new JMenuItem("Update Doctor Record");
        updateSubMenu.add(updatePatientItem);
        updateSubMenu.add(updateDoctorItem);
        
        manageMenu.add(addPatientItem);
        manageMenu.add(addDoctorItem);
        manageMenu.add(addDiseaseItem);
        manageMenu.addSeparator();
        manageMenu.add(updateSubMenu);
        manageMenu.addSeparator();
        manageMenu.add(deletePatientItem);
        
        // Search Record Menu
        JMenu searchMenu = new JMenu("Search Record");
        
        JMenuItem searchPatientByNameItem = new JMenuItem("Search Patient by Name");
        JMenuItem searchPatientByIdItem = new JMenuItem("Search Patient by ID");
        JMenuItem searchPatientByAgeItem = new JMenuItem("Search Patient by Age");
        JMenuItem searchPatientByDiseaseItem = new JMenuItem("Search Patient by Disease");
        JMenuItem searchPatientByDoctorItem = new JMenuItem("Search Patient by Doctor");
        JMenuItem searchDoctorByNameItem = new JMenuItem("Search Doctor by Name");
        JMenuItem searchDoctorBySpecializationItem = new JMenuItem("Search Doctor by Disease Specialization");
        
        searchMenu.add(searchPatientByNameItem);
        searchMenu.add(searchPatientByIdItem);
        searchMenu.add(searchPatientByAgeItem);
        searchMenu.add(searchPatientByDiseaseItem);
        searchMenu.add(searchPatientByDoctorItem);
        searchMenu.addSeparator();
        searchMenu.add(searchDoctorByNameItem);
        searchMenu.add(searchDoctorBySpecializationItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About Us");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        
        helpMenu.add(aboutItem);
        helpMenu.add(changePasswordItem);
        
        // Add menus to menu bar
        menuBar.add(manageMenu);
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Add event listeners
        addDiseaseItem.addActionListener(e -> openDiseaseForm());
        addDoctorItem.addActionListener(e -> openDoctorForm());
        addPatientItem.addActionListener(e -> addNewPatient());
        updatePatientItem.addActionListener(e -> updatePatientRecord());
        deletePatientItem.addActionListener(e -> deletePatientRecord());

        searchPatientByIdItem.addActionListener(e -> searchPatientById());
        searchPatientByNameItem.addActionListener(e -> searchPatientByName());
        searchPatientByAgeItem.addActionListener(e -> searchPatientByAge());
        searchPatientByDiseaseItem.addActionListener(e -> searchPatientByDisease());
        searchPatientByDoctorItem.addActionListener(e -> searchPatientByDoctor());
        searchDoctorBySpecializationItem.addActionListener(e -> searchDoctorBySpecialization());
        searchDoctorByNameItem.addActionListener(e -> searchDoctorByName());

        
        aboutItem.addActionListener(e -> showAboutDialog());
        changePasswordItem.addActionListener(e -> changePassword());
    }
    
    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton addPatientBtn = new JButton("Add Patient");
        addPatientBtn.setForeground(Color.BLACK);
        addPatientBtn.setFocusPainted(false);
        addPatientBtn.addActionListener(e -> addNewPatient());

        JButton searchPatientBtn = new JButton("Search");
        searchPatientBtn.setFocusPainted(false);
        searchPatientBtn.setToolTipText("Search Patient Record");

        JButton addDoctorBtn = new JButton("Add Doctor");
        addDoctorBtn.setFocusPainted(false);
        addDoctorBtn.setToolTipText("Add New Doctor");

        JButton printBtn = new JButton("Print");
        printBtn.setFocusPainted(false);
        printBtn.setToolTipText("Print");

        toolBar.add(addPatientBtn);
        toolBar.addSeparator();
        toolBar.add(searchPatientBtn);
        toolBar.addSeparator();
        toolBar.add(addDoctorBtn);
        toolBar.addSeparator();
        toolBar.add(printBtn);

        add(toolBar, BorderLayout.NORTH);

        // Add event listeners for toolbar buttons
        addPatientBtn.addActionListener(e -> addNewPatient());
        addDoctorBtn.addActionListener(e -> openDoctorForm());
        searchPatientBtn.addActionListener(e -> openSearchDialog());
        printBtn.addActionListener(e -> showPrintDialog());
    }
    
    private void createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel(
            "<html><center><h1>Welcome Administrator!</h1>" +
            "<p>You have full access to manage patients, doctors, and diseases.</p>" +
            "<p>Use the menu bar or toolbar to perform operations.</p></center></html>", 
            JLabel.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Ready - Administrator Mode");
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void openDiseaseForm() {
        DiseaseForm diseaseForm = new DiseaseForm(this);
        diseaseForm.setVisible(true);
        if (diseaseForm.isSaved()) {
            JOptionPane.showMessageDialog(this, 
                "Disease was added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void openDoctorForm() {
        DoctorForm doctorForm = new DoctorForm(this);
        doctorForm.setVisible(true);
        if (doctorForm.isSaved()) {
            JOptionPane.showMessageDialog(this, 
                "Doctor was added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAboutDialog() {
        JTextArea aboutText = new JTextArea();
        aboutText.setText(
            "PATIENT MANAGEMENT SYSTEM\n\n" +
            "Version: 2.0\n" +
            "Developed By: Your Name\n" +
            "License: Educational Use\n\n" +
            "FEATURES:\n" +
            "• Patient Record Management\n" +
            "• Doctor and Disease Management\n" +
            "• Advanced Search Capabilities\n" +
            "• Secure User Authentication\n" +
            "• Role-based Access Control\n" +
            "• Printing and Reporting\n\n" +
            "TECHNOLOGIES:\n" +
            "• Java Swing for GUI\n" +
            "• SQLite Database\n" +
            "• JDBC for Data Access\n" +
            "• MVC Architecture\n\n" +
            "© 2024 All Rights Reserved"
        );
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        aboutText.setBackground(UIManager.getColor("Panel.background"));
        
        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "About Patient Management System", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void changePassword() {
        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Old Password:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validation
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "All fields are required!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "New passwords do not match!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newPassword.length() < 4) {
                JOptionPane.showMessageDialog(this, 
                    "New password must be at least 4 characters long!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Change password in database
            if (AuthService.changePassword(currentUser.getUsername(), oldPassword, newPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Password changed successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to change password. Please check your old password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

        // Search Methods
    private void searchPatientById() {
        String patientIdStr = JOptionPane.showInputDialog(this, "Enter Patient ID:");
        if (patientIdStr == null || patientIdStr.trim().isEmpty()) return;
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            Patient patient = PatientDAO.getPatientById(patientId);
            
            if (patient != null) {
                showPatientDetails(patient);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No patient found with ID: " + patientId, 
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid patient ID number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchPatientByName() {
        String name = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        if (name == null || name.trim().isEmpty()) return;
        
        List patients = PatientDAO.getPatientsByName(name.trim());
        showPatientsInTable(patients, "Search Results - Patients by Name: " + name);
    }
    
    private void searchPatientByAge() {
        String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
        if (ageStr == null || ageStr.trim().isEmpty()) return;
        
        try {
            int age = Integer.parseInt(ageStr);
            List patients = PatientDAO.getPatientsByAge(age);
            showPatientsInTable(patients, "Search Results - Patients by Age: " + age);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid age number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchPatientByDisease() {
        String diseaseName = JOptionPane.showInputDialog(this, "Enter Disease Name:");
        if (diseaseName == null || diseaseName.trim().isEmpty()) return;
        
        List patients = PatientDAO.getPatientsByDisease(diseaseName.trim());
        showPatientsInTable(patients, "Search Results - Patients by Disease: " + diseaseName);
    }
    
    private void searchPatientByDoctor() {
        String doctorName = JOptionPane.showInputDialog(this, "Enter Doctor Name:");
        if (doctorName == null || doctorName.trim().isEmpty()) return;
        
        List patients = PatientDAO.getPatientsByDoctor(doctorName.trim());
        showPatientsInTable(patients, "Search Results - Patients by Doctor: " + doctorName);
    }
    

    
    private void showPatientDetails(Patient patient) {
        StringBuilder details = new StringBuilder();
        details.append("Patient ID: ").append(patient.getPatientId()).append("\n");
        details.append("Name: ").append(patient.getPatientName()).append("\n");
        details.append("Father's Name: ").append(patient.getPfName()).append("\n");
        details.append("Sex: ").append(patient.getSex()).append("\n");
        details.append("Age: ").append(patient.getAge()).append(" years\n");
        details.append("Doctor: ").append(patient.getDoctorName()).append("\n");
        details.append("Disease History: ").append(patient.getDiseaseHistory()).append("\n");
        details.append("Prescription: ").append(patient.getPrescription()).append("\n");
        
        JOptionPane.showMessageDialog(this, 
            details.toString(), 
            "Patient Details - ID: " + patient.getPatientId(), 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showPatientsInTable(List patients, String title) {
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No patients found.", 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columnNames = {"Patient ID", "Name", "Father Name", "Sex", "Age", "Doctor", "Disease History", "Prescription"};
        Object[][] data = new Object[patients.size()][8];
        
        for (int i = 0; i < patients.size(); i++) {
            Patient p = (Patient) patients.get(i);
            data[i][0] = p.getPatientId();
            data[i][1] = p.getPatientName();
            data[i][2] = p.getPfName();
            data[i][3] = p.getSex();
            data[i][4] = p.getAge();
            data[i][5] = p.getDoctorName();
            data[i][6] = p.getDiseaseHistory();
            data[i][7] = p.getPrescription();
        }
        
        SearchResultsDialog dialog = new SearchResultsDialog(this, title, data, columnNames);
        dialog.setVisible(true);
    }
    
    private void deletePatientRecord() {
        String patientIdStr = JOptionPane.showInputDialog(this, "Enter Patient ID to delete:");
        if (patientIdStr == null || patientIdStr.trim().isEmpty()) return;
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            Patient patient = PatientDAO.getPatientById(patientId);
            
            if (patient == null) {
                JOptionPane.showMessageDialog(this, 
                    "No patient found with ID: " + patientId, 
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient:\n" +
                "ID: " + patient.getPatientId() + "\n" +
                "Name: " + patient.getPatientName() + "\n\n" +
                "This action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (PatientDAO.deletePatient(patientId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Patient record deleted successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete patient record.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid patient ID number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePatientRecord() {
        String patientIdStr = JOptionPane.showInputDialog(this, "Enter Patient ID to update:");
        if (patientIdStr == null || patientIdStr.trim().isEmpty()) return;
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            Patient patient = PatientDAO.getPatientById(patientId);
            
            if (patient != null) {
                PatientForm patientForm = new PatientForm(this, patient);
                patientForm.setVisible(true);
                if (patientForm.isSaved()) {
                    JOptionPane.showMessageDialog(this, 
                        "Patient record updated successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No patient found with ID: " + patientId, 
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid patient ID number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addNewPatient() {
        PatientForm patientForm = new PatientForm(this);
        patientForm.setVisible(true);
        if (patientForm.isSaved()) {
            JOptionPane.showMessageDialog(this,
                "Patient was added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openSearchDialog() {
        String[] options = {"Search Patient by Name", "Search Patient by ID", "Search Patient by Age", "Search Patient by Disease", "Search Patient by Doctor", "Search Doctor by Name", "Search Doctor by Specialization", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
            "Select a search option:",
            "Search Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        switch (choice) {
            case 0:
                searchPatientByName();
                break;
            case 1:
                searchPatientById();
                break;
            case 2:
                searchPatientByAge();
                break;
            case 3:
                searchPatientByDisease();
                break;
            case 4:
                searchPatientByDoctor();
                break;
            case 5:
                searchDoctorByName();
                break;
            case 6:
                searchDoctorBySpecialization();
                break;
        }
    }

        private void searchDoctorByName() {
        String name = JOptionPane.showInputDialog(this, "Enter Doctor Name:");
        if (name == null || name.trim().isEmpty()) return;
        
        List<Doctor> doctors = DoctorDAO.getDoctorsByName(name.trim());
        showDoctorsInTable(doctors, "Search Results - Doctors by Name: " + name);
    }
    
    private void searchDoctorBySpecialization() {
        String specialization = JOptionPane.showInputDialog(this, "Enter Disease Specialization:");
        if (specialization == null || specialization.trim().isEmpty()) return;
        
        List<Doctor> doctors = DoctorDAO.getDoctorsBySpecialization(specialization.trim());
        showDoctorsInTable(doctors, "Search Results - Doctors by Specialization: " + specialization);
    }
    
    private void showDoctorsInTable(List<Doctor> doctors, String title) {
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No doctors found.", 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columnNames = {"Doctor ID", "Doctor Name", "Specialization"};
        Object[][] data = new Object[doctors.size()][3];
        
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            data[i][0] = d.getDoctorId();
            data[i][1] = d.getDoctorName();
            data[i][2] = d.getDiseaseName();
        }
        
        SearchResultsDialog dialog = new SearchResultsDialog(this, title, data, columnNames);
        dialog.setVisible(true);
    }

        private void showPrintDialog() {
        String[] options = {"Print Patient List", "Print Doctor List", "Print Disease List", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
            "What would you like to print?",
            "Print Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0:
                printPatientList();
                break;
            case 1:
                printDoctorList();
                break;
            case 2:
                printDiseaseList();
                break;
        }
    }
    
    private void printPatientList() {
        List<Patient> patients = PatientDAO.getAllPatients();
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients to print.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columnNames = {"Patient ID", "Name", "Father Name", "Sex", "Age", "Doctor"};
        Object[][] data = new Object[patients.size()][6];
        
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getPatientId();
            data[i][1] = p.getPatientName();
            data[i][2] = p.getPfName();
            data[i][3] = p.getSex();
            data[i][4] = p.getAge();
            data[i][5] = p.getDoctorName();
        }
        
        JTable table = new JTable(data, columnNames);
        utils.PrintUtility.printTable(table, "Patient List - " + new java.util.Date());
    }
    
    private void printDoctorList() {
        List<Doctor> doctors = DoctorDAO.getAllDoctors();
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No doctors to print.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columnNames = {"Doctor ID", "Doctor Name", "Specialization"};
        Object[][] data = new Object[doctors.size()][3];
        
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            data[i][0] = d.getDoctorId();
            data[i][1] = d.getDoctorName();
            data[i][2] = d.getDiseaseName();
        }
        
        JTable table = new JTable(data, columnNames);
        utils.PrintUtility.printTable(table, "Doctor List - " + new java.util.Date());
    }
    
    private void printDiseaseList() {
        List<Disease> diseases = DiseaseDAO.getAllDiseases();
        if (diseases.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No diseases to print.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] columnNames = {"Disease ID", "Disease Name", "Description"};
        Object[][] data = new Object[diseases.size()][3];
        
        for (int i = 0; i < diseases.size(); i++) {
            Disease d = diseases.get(i);
            data[i][0] = d.getDiseaseId();
            data[i][1] = d.getDiseaseName();
            data[i][2] = d.getDiseaseDescription();
        }
        
        JTable table = new JTable(data, columnNames);
        utils.PrintUtility.printTable(table, "Disease List - " + new java.util.Date());
    }
}