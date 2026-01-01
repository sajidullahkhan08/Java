package client;

import common.Request;
import common.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfileWindow extends JFrame {
    private Client client;
    private User user;
    private User currentUser;
    private boolean isEditable;
    private JTextField statusField;
    private JButton updateButton;
    private JButton uploadPicButton;
    private JLabel profilePicLabel;
    private byte[] profilePic;

    public ProfileWindow(Client client, User user, User currentUser, boolean isEditable) {
        this.client = client;
        this.user = user;
        this.currentUser = currentUser;
        this.isEditable = isEditable;
        initializeUI();
    }

    // Backward compatibility for own profile editing
    public ProfileWindow(Client client, User user) {
        this(client, user, user, true);
    }

    private void initializeUI() {
        setTitle(isEditable ? "Edit Profile" : user.getUsername() + "'s Profile");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Profile picture panel
        JPanel picPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        picPanel.setBackground(Color.WHITE);
        profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(100, 100));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        profilePicLabel.setHorizontalAlignment(JLabel.CENTER);
        profilePicLabel.setText("No Picture");

        if (user.getProfilePic() != null && user.getProfilePic().length > 0) {
            ImageIcon icon = new ImageIcon(user.getProfilePic());
            Image img = icon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(img));
            profilePicLabel.setText("");
        }

        picPanel.add(profilePicLabel);

        // Info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(user.getUsername(), 15);
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(240, 240, 240));
        infoPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusField = new JTextField(user.getStatusMessage() != null ? user.getStatusMessage() : "Available", 15);
        statusField.setEditable(isEditable);
        if (!isEditable) {
            statusField.setBackground(new Color(240, 240, 240));
        }
        infoPanel.add(statusField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Online Status:"), gbc);
        gbc.gridx = 1;
        JTextField statusField2 = new JTextField(user.getStatus(), 15);
        statusField2.setEditable(false);
        statusField2.setBackground(new Color(240, 240, 240));
        infoPanel.add(statusField2, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        if (isEditable) {
            uploadPicButton = new JButton("Change Picture");
            uploadPicButton.setBackground(new Color(255, 165, 0));
            uploadPicButton.setForeground(Color.WHITE);
            uploadPicButton.addActionListener(e -> uploadPic());
            buttonPanel.add(uploadPicButton);

            updateButton = new JButton("Update Profile");
            updateButton.setBackground(new Color(70, 130, 180));
            updateButton.setForeground(Color.WHITE);
            updateButton.addActionListener(e -> updateProfile());
            buttonPanel.add(updateButton);
        } else {
            JButton closeButton = new JButton("Close");
            closeButton.setBackground(new Color(169, 169, 169));
            closeButton.setForeground(Color.WHITE);
            closeButton.addActionListener(e -> dispose());
            buttonPanel.add(closeButton);
        }

        mainPanel.add(picPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void uploadPic() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                profilePic = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(profilePic);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile() {
        user.setStatusMessage(statusField.getText());
        if (profilePic != null) {
            user.setProfilePic(profilePic);
        }
        Request request = new Request();
        request.setType("updateProfile");
        request.setUser(user);
        client.sendRequest(request);
        JOptionPane.showMessageDialog(this, "Profile updated");
        dispose();
    }
}