import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {
    JFrame frame;
    JLabel  label; 
    JButton button;
    
    GUI() {
        frame = new JFrame("My Frame");
        label = new JLabel("Login here: ");
        button = new JButton("LOGIN");

        button.addActionListener(e -> button.setText("You are logged in"));
        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(button);

        frame.setSize(500,400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
    
}
