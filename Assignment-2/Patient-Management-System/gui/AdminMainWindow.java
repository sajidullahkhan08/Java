package gui;

import model.User;
import javax.swing.*;

public class AdminMainWindow extends JFrame {
    private User currentUser;
    
    public AdminMainWindow(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Patient Management System - Administrator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Temporary content
        JLabel welcomeLabel = new JLabel(
            "Welcome Administrator: " + currentUser.getUsername() + 
            "\n\nFull Admin functionality will be implemented in Phase 2-4.", 
            JLabel.CENTER
        );
        welcomeLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        
        add(welcomeLabel);
    }
}