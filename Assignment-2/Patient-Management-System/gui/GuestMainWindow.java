package gui;

import model.User;
import javax.swing.*;

public class GuestMainWindow extends JFrame {
    private User currentUser;
    
    public GuestMainWindow(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Patient Management System - Guest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Temporary content
        JLabel welcomeLabel = new JLabel(
            "Welcome Guest: " + currentUser.getUsername() + 
            "\n\nLimited Guest functionality will be implemented in Phase 2-4.", 
            JLabel.CENTER
        );
        welcomeLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        
        add(welcomeLabel);
    }
}