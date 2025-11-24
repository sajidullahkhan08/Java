package gui;

import model.User;
import database.PatientDAO;
import model.Patient;
import gui.forms.SearchResultsDialog;
import auth.AuthService;
import utils.PrintUtility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GuestMainWindow extends JFrame {
    private User currentUser;
    
    public GuestMainWindow(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Patient Management System - Guest: " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        createMenuBar();
        createToolBar();
        createMainContent();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Search Record Menu
        JMenu searchMenu = new JMenu("Search Record");
        JMenuItem searchByNameItem = new JMenuItem("Search by Name");
        JMenuItem searchByIdItem = new JMenuItem("Search by ID");
        JMenuItem searchByAgeItem = new JMenuItem("Search by Age");
        
        searchMenu.add(searchByNameItem);
        searchMenu.add(searchByIdItem);
        searchMenu.add(searchByAgeItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About Us");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        
        helpMenu.add(aboutItem);
        helpMenu.add(changePasswordItem);
        
        // Add menus to menu bar
        menuBar.add(searchMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Add event listeners
        searchByNameItem.addActionListener(e -> searchPatientByName());
        searchByIdItem.addActionListener(e -> searchPatientById());
        searchByAgeItem.addActionListener(e -> searchPatientByAge());
        
        aboutItem.addActionListener(e -> showAboutDialog());
        changePasswordItem.addActionListener(e -> changePassword());
    }
    
    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton searchBtn = new JButton(UIManager.getIcon("FileView.directoryIcon"));
        searchBtn.setToolTipText("Search Record");
        
        JButton printBtn = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
        printBtn.setToolTipText("Print");
        
        toolBar.add(searchBtn);
        toolBar.addSeparator();
        toolBar.add(printBtn);
        
        add(toolBar, BorderLayout.NORTH);
        
        // Add event listeners for toolbar buttons
        searchBtn.addActionListener(e -> searchPatientByName());
        printBtn.addActionListener(e -> printPatientList());
    }
    
    private void createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel(
            "<html><center><h1>Welcome Guest!</h1>" +
            "<p>You have limited access to search patient records.</p>" +
            "<p>Use the Search Record menu to find patients by name, ID, or age.</p></center></html>", 
            JLabel.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Ready - Guest Mode (Read Only)");
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // Search Methods (similar to Admin but without modification capabilities)
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
        
        List<Patient> patients = PatientDAO.getPatientsByName(name.trim());
        showPatientsInTable(patients, "Search Results - Patients by Name: " + name);
    }
    
    private void searchPatientByAge() {
        String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
        if (ageStr == null || ageStr.trim().isEmpty()) return;
        
        try {
            int age = Integer.parseInt(ageStr);
            List<Patient> patients = PatientDAO.getPatientsByAge(age);
            showPatientsInTable(patients, "Search Results - Patients by Age: " + age);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid age number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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
    
    private void showPatientsInTable(List<Patient> patients, String title) {
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
            Patient p = patients.get(i);
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
        PrintUtility.printTable(table, "Patient List - Guest Access - " + new java.util.Date());
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
            "GUEST ACCESS:\n" +
            "• Read-only access to patient records\n" +
            "• Search patients by name, ID, or age\n" +
            "• View patient details\n" +
            "• Print patient lists\n\n" +
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
        scrollPane.setPreferredSize(new Dimension(400, 400));
        
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
}