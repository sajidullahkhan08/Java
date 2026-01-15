import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

class App {
    JFrame frame;
    JButton btn;
    JLabel label;

    App() {
        frame = new JFrame("App");
        btn = new JButton("Click");
        label = new JLabel("Ready");

        btn.addActionListener(e -> label.setText("Clicked"));

        frame.setLayout(new FlowLayout());
        frame.add(btn);
        frame.add(label);

        frame.setSize(300,200);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
