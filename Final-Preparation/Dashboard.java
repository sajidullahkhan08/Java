import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {
    Dashboard() {
        JFrame f = new JFrame("ShopEase Dashboard");
        JButton browse = new JButton("Browse Products");
        JButton cart = new JButton("View Cart");

        browse.setBounds(50, 50, 150, 30);
        cart.setBounds(50, 90, 150, 30);

        f.add(browse);
        f.add(cart);

        f.setSize(300, 200);
        f.setLayout(null);
        f.setVisible(true);
    }
}
