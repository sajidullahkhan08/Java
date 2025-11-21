package auth;

import model.User;
import gui.AdminMainWindow;
import gui.GuestMainWindow;
import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton, cancelButton;
    
    public LoginWindow() {
        initializeUI();
        setupEventListeners();
    }
    
    private void initializeUI() {
        setTitle("Patient Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Patient Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 100, 0)); // Dark green
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Login Credentials"));
        
        // Form components
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        
        JLabel userTypeLabel = new JLabel("User Type:");
        String[] userTypes = {"Administrator", "Guest"};
        userTypeCombo = new JComboBox<>(userTypes);
        
        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(userTypeLabel);
        formPanel.add(userTypeCombo);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        
        // Style buttons - use default system look and feel for consistency
        loginButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Test credentials label (for development)
        JLabel testLabel = new JLabel("Test: admin/admin123 (Admin) or guest/guest123 (Guest)", JLabel.CENTER);
        testLabel.setForeground(Color.GRAY);
        testLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        mainPanel.add(testLabel, BorderLayout.NORTH);
        
        // Make Enter key trigger login
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(e -> performLogin());
        
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                DBConnection.closeConnection();
                System.exit(0);
            }
        });
        
        // Also close database connection on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show loading
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loginButton.setEnabled(false);
        
        // Perform authentication in background (simulated)
        Timer timer = new Timer(1000, e -> {
            User user = AuthService.authenticate(username, password, userType);
            
            if (user != null) {
                // Login successful
                JOptionPane.showMessageDialog(LoginWindow.this,
                    "Login successful! Welcome " + user.getUserType(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Open appropriate main window
                openMainWindow(user);
                
            } else {
                // Login failed
                JOptionPane.showMessageDialog(LoginWindow.this,
                    "Invalid credentials or user type mismatch!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                
                // Reset UI
                setCursor(Cursor.getDefaultCursor());
                loginButton.setEnabled(true);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });
        
        timer.setRepeats(false);
        timer.start();
    }
    
    private void openMainWindow(User user) {
        dispose(); // Close login window
        
        switch (user.getUserType()) {
            case "Administrator":
                new AdminMainWindow(user).setVisible(true);
                break;
            case "Guest":
                new GuestMainWindow(user).setVisible(true);
                break;
        }
    }
    
    public static void main(String[] args) {
    // Set system look and feel
    try {
        // Option 1: Use cross-platform look and feel
        // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        
        // Option 2: Use system look and feel (recommended)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
    } catch (Exception e) {
        System.err.println("Error setting look and feel: " + e.getMessage());
        // Continue with default look and feel
    }
    
    // Initialize database connection
    DBConnection.getConnection();
    
    // Show login window
    SwingUtilities.invokeLater(() -> {
        new LoginWindow().setVisible(true);
    });
}
}