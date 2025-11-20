package gui;

import model.User;
import gui.forms.DiseaseForm;
import gui.forms.DoctorForm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        addPatientItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Patient form coming soon!"));
        
        aboutItem.addActionListener(e -> showAboutDialog());
        changePasswordItem.addActionListener(e -> changePassword());
    }
    
    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addPatientBtn = new JButton(UIManager.getIcon("FileView.fileIcon"));
        addPatientBtn.setToolTipText("Add New Patient");
        
        JButton searchPatientBtn = new JButton(UIManager.getIcon("FileView.directoryIcon"));
        searchPatientBtn.setToolTipText("Search Patient Record");
        
        JButton addDoctorBtn = new JButton(UIManager.getIcon("FileView.computerIcon"));
        addDoctorBtn.setToolTipText("Add New Doctor");
        
        JButton printBtn = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
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
        addPatientBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Patient form coming soon!"));
        addDoctorBtn.addActionListener(e -> openDoctorForm());
        searchPatientBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Search functionality coming soon!"));
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Print functionality coming soon!"));
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
        JOptionPane.showMessageDialog(this,
            "<html><center><h2>Patient Management System</h2>" +
            "<p>Version 1.0</p>" +
            "<p>Developed for Hospital Management</p>" +
            "<p>© 2024 All Rights Reserved</p></center></html>",
            "About Us",
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
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "New passwords do not match!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // For now, show a message (will implement actual password change later)
            JOptionPane.showMessageDialog(this, 
                "Password change functionality will be implemented in Phase 3.", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}