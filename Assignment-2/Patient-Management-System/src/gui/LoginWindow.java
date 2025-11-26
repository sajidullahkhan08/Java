package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeBox;

    public LoginWindow() {
        setTitle("Login - Patient Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("User Type:"));
        userTypeBox = new JComboBox<>(new String[]{"Administrator", "Guest"});
        add(userTypeBox);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(new LoginListener());
        add(new JLabel()); // spacer
        add(loginBtn);
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String type = (String) userTypeBox.getSelectedItem();

            // 🔐 Simple hard-coded login (replace with real auth later)
            if ("admin".equals(user) && "admin".equals(pass) && "Administrator".equals(type)) {
                new AdminDashboard().setVisible(true);
                dispose();
            } else if ("guest".equals(user) && "guest".equals(pass) && "Guest".equals(type)) {
                new GuestDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginWindow.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}