import javax.swing.*;
import java.awt.event.*;

public class LoginGUI {
    public static void main(String[] args) {

        JFrame frame = new JFrame("ShopEase Login");
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Login");

        user.setBounds(100, 50, 150, 30);
        pass.setBounds(100, 90, 150, 30);
        login.setBounds(120, 130, 100, 30);

        frame.add(user);
        frame.add(pass);
        frame.add(login);

        frame.setSize(350, 250);
        frame.setLayout(null);
        frame.setVisible(true);

        login.addActionListener(e -> {
            String u = user.getText();
            String p = new String(pass.getPassword());

            if (u.equals("customer") && p.equals("12345")) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
                new Dashboard();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials");
            }
        });
    }
}
