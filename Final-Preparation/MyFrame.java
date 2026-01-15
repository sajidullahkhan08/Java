import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class MyFrame {
    JFrame frame;
    JButton btn, btn1;
    JLabel label;

    MyFrame() {
        frame = new JFrame("Inner Class Demo");
        btn = new JButton("Click");
        btn1 = new JButton("Reset");
        label = new JLabel("Hello, Swing!", SwingConstants.CENTER);
        btn.addActionListener(new ButtonHandler());

        btn1.addActionListener(new ButtonHandler() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Reset Clicked!");
            }
        });

        frame.add(label, BorderLayout.CENTER);
        frame.add(btn, BorderLayout.SOUTH);
        frame.add(btn1, BorderLayout.NORTH);
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "Clicked!");
        }
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
