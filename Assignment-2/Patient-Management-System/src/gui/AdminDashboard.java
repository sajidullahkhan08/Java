package gui;

import gui.forms.*;
import database.PatientDAO;
import model.Patient;
import utils.PasswordManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.util.List;

public class AdminDashboard extends JFrame implements Printable {
    private PatientDAO patientDAO = new PatientDAO();

    public AdminDashboard() {
        setTitle("Administrator Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Manage Record Menu
        JMenu manageMenu = new JMenu("Manage Record");
        manageMenu.add(createMenuItem("Add New Patient", e -> new AddPatientForm().setVisible(true)));
        manageMenu.add(createMenuItem("Add New Doctor", e -> new AddDoctorForm().setVisible(true)));
        manageMenu.add(createMenuItem("Add New Disease", e -> new AddDiseaseForm().setVisible(true)));
        manageMenu.addSeparator();

        manageMenu.add(createMenuItem("Delete Patient Record", e -> deletePatientRecord()));
        
        JMenu updateMenu = new JMenu("Update Record");
        updateMenu.add(createMenuItem("Update Patient", e -> updatePatient()));
        updateMenu.add(createMenuItem("Update Doctor Record", e -> JOptionPane.showMessageDialog(this, "Update Doctor (not implemented yet)")));
        manageMenu.add(updateMenu);
        menuBar.add(manageMenu);

        // Search Record Menu
        JMenu searchMenu = new JMenu("Search Record");
        searchMenu.add(createMenuItem("Search Patient by Name", e -> searchPatientByName()));
        searchMenu.add(createMenuItem("Search Patient by ID", e -> searchPatientById()));
        searchMenu.add(createMenuItem("Search Patient by Age", e -> searchPatientByAge()));
        searchMenu.add(createMenuItem("Search Patient by Disease", e -> JOptionPane.showMessageDialog(this, "Search by Disease (not implemented)")));
        searchMenu.add(createMenuItem("Search Patient by Doctor", e -> JOptionPane.showMessageDialog(this, "Search by Doctor (not implemented)")));
        searchMenu.add(createMenuItem("Search Doctor by Name", e -> JOptionPane.showMessageDialog(this, "Search Doctor by Name (not implemented)")));
        searchMenu.add(createMenuItem("Search Doctor by Disease Specialization", e -> JOptionPane.showMessageDialog(this, "Search Doctor by Disease (not implemented)")));
        menuBar.add(searchMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createMenuItem("About Us", e -> JOptionPane.showMessageDialog(this, "Patient Management System v1.0\nFor Assignment No.2")));
        helpMenu.add(createMenuItem("Change Password", e -> changePassword()));
        menuBar.add(helpMenu);

        // ToolBar
        JToolBar toolBar = new JToolBar();
        toolBar.add(createToolbarButton("Add Patient", e -> new AddPatientForm().setVisible(true)));
        toolBar.add(createToolbarButton("Search Patient", e -> searchPatientByName()));
        toolBar.add(createToolbarButton("Add Doctor", e -> new AddDoctorForm().setVisible(true)));
        toolBar.add(createToolbarButton("Print", e -> printDashboard()));
        add(toolBar, BorderLayout.NORTH);

        add(new JLabel("Welcome, Administrator!", JLabel.CENTER), BorderLayout.CENTER);
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }

    private JButton createToolbarButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.addActionListener(listener);
        return btn;
    }

    // === NEW: Delete Patient ===
    private void deletePatientRecord() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID to delete:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr.trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Patient ID " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                patientDAO.deleteById(id);
                JOptionPane.showMessageDialog(this, "Patient record deleted successfully.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // === Update Patient ===
    private void updatePatient() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID to update:");
        if (idStr == null || idStr.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr.trim());
            new UpdatePatientForm(id).setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Search Methods ===
    private void searchPatientById() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                List<Patient> patients = patientDAO.searchById(id);
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchPatientByName() {
        String name = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                List<Patient> patients = patientDAO.searchByName(name.trim());
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchPatientByAge() {
        String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
        if (ageStr != null && !ageStr.trim().isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr.trim());
                List<Patient> patients = patientDAO.searchByAge(age);
                new SearchResultsWindow(patients).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            if (!PasswordManager.validatePassword("admin", old)) {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!newP.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (newP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "New password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                PasswordManager.setPassword("admin", newP);
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