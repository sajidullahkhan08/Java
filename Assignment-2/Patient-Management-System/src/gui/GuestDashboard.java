package gui;

import gui.forms.SearchResultsWindow;
import database.PatientDAO;
import model.Patient;
import utils.PasswordManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.util.List;

public class GuestDashboard extends JFrame implements Printable {
    private PatientDAO patientDAO = new PatientDAO();

    public GuestDashboard() {
        setTitle("Guest Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu searchMenu = new JMenu("Search Record");
        searchMenu.add(createItem("Search by Name", e -> searchByName()));
        searchMenu.add(createItem("Search by ID", e -> searchById()));
        searchMenu.add(createItem("Search by Age", e -> searchByAge()));
        menuBar.add(searchMenu);

        JMenu printMenu = new JMenu("Print");
        printMenu.add(createItem("Print Records", e -> printDashboard()));
        menuBar.add(printMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createItem("About Us", e -> JOptionPane.showMessageDialog(this, "Patient System v1.0")));
        helpMenu.add(createItem("Change Password", e -> changePassword()));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        toolBar.add(createButton("Search", e -> searchByName()));
        toolBar.add(createButton("Print", e -> printDashboard()));
        add(toolBar, BorderLayout.NORTH);

        add(new JLabel("Welcome, Guest!", JLabel.CENTER));
    }

    private JMenuItem createItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.addActionListener(listener);
        return btn;
    }

    private void searchByName() {
        String name = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                List<Patient> patients = patientDAO.searchByName(name.trim());
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchById() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                List<Patient> patients = patientDAO.searchById(id);
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID or error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchByAge() {
        String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
        if (ageStr != null && !ageStr.trim().isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr.trim());
                List<Patient> patients = patientDAO.searchByAge(age);
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid age or error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printDashboard() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Print job sent successfully.");
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Print error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changePassword() {
        JPasswordField oldPass = new JPasswordField();
        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();
        Object[] message = {
            "Current Password:", oldPass,
            "New Password:", newPass,
            "Confirm New Password:", confirmPass
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String old = new String(oldPass.getPassword());
            String newP = new String(newPass.getPassword());
            String confirm = new String(confirmPass.getPassword());
            if (!PasswordManager.validatePassword("guest", old)) {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!newP.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (newP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "New password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                PasswordManager.setPassword("guest", newP);
                JOptionPane.showMessageDialog(this, "Password changed successfully!");
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        this.printAll(g2d);
        return PAGE_EXISTS;
    }
}