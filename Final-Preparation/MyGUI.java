import javax.swing.JOptionPane;

public class MyGUI {
    public static void main(String[] args) {
        JOptionPane.showInputDialog(null,"Enter Your name: ", "Input", JOptionPane.QUESTION_MESSAGE, null, null, "Type Here");
        JOptionPane.showOptionDialog(null, args, null, 0, 0, null, args, args);
        JOptionPane.showMessageDialog(null, "Hello, welcome to My GUI Application!", "My GUI", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "This is a simple GUI using JOptionPane.", "My GUI", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, "Goodbye!", "My GUI", JOptionPane.PLAIN_MESSAGE);
        JOptionPane.showMessageDialog(null, "Have a nice day!", "My GUI", JOptionPane.WARNING_MESSAGE);
        JOptionPane.showMessageDialog(null, "Thank you for using the application.", "My GUI", JOptionPane.QUESTION_MESSAGE);

        JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

        if (JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Continue", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "You chose to continue.", "My GUI", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You chose not to continue.", "My GUI", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}