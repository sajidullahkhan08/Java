package gui.forms;

import model.Disease;
import database.DiseaseDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DiseaseForm extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton saveButton, cancelButton;
    private boolean saved = false;
    
    public DiseaseForm(JFrame parent) {
        super(parent, "Add New Disease", true);
        initializeUI();
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
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Disease Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Disease Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Disease Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        saveButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupEventListeners() {
        saveButton.addActionListener(e -> saveDisease());
        cancelButton.addActionListener(e -> dispose());
        
        // Close on escape key
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void saveDisease() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a disease name.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        Disease disease = new Disease();
        disease.setDiseaseName(name);
        disease.setDiseaseDescription(description);
        
        if (DiseaseDAO.addDisease(disease)) {
            JOptionPane.showMessageDialog(this,
                "Disease added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add disease. It might already exist.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}