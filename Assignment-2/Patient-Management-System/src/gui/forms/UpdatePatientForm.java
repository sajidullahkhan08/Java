package gui.forms;

import database.PatientDAO;
import model.Patient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UpdatePatientForm extends JFrame {
    private int patientId;
    private JTextArea historyArea;
    private JTextArea prescriptionArea;

    public UpdatePatientForm(int id) {
        this.patientId = id;
        loadAndShowForm();
    }

    private void loadAndShowForm() {
        try {
            Patient p = new PatientDAO().findById(patientId);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            setTitle("Update Patient Record - ID: " + patientId);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(500, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel infoPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // Patient Info (non-editable)
            gbc.gridx = 0; gbc.gridy = 0;
            infoPanel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(p.getName()), gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            infoPanel.add(new JLabel("Father Name:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(p.getFatherName()), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            infoPanel.add(new JLabel("Sex:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(p.getSex()), gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            infoPanel.add(new JLabel("DOB:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(p.getDob() != null ? p.getDob().toString() : ""), gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            infoPanel.add(new JLabel("Doctor:"), gbc);
            gbc.gridx = 1;
            infoPanel.add(new JLabel(p.getDoctorName() != null ? p.getDoctorName() : ""), gbc);

            add(new JScrollPane(infoPanel), BorderLayout.NORTH);

            // Editable: Disease History & Prescription
            JPanel editPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            editPanel.setBorder(BorderFactory.createTitledBorder("Editable Fields"));

            historyArea = new JTextArea(p.getDiseaseHistory() != null ? p.getDiseaseHistory() : "");
            prescriptionArea = new JTextArea(p.getPrescription() != null ? p.getPrescription() : "");

            editPanel.add(new JScrollPane(historyArea));
            editPanel.add(new JScrollPane(prescriptionArea));

            add(editPanel, BorderLayout.CENTER);

            // Buttons
            JPanel btnPanel = new JPanel();
            JButton saveBtn = new JButton("Save");
            saveBtn.addActionListener(e -> saveUpdates(p));
            btnPanel.add(saveBtn);

            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(e -> dispose());
            btnPanel.add(cancelBtn);

            add(btnPanel, BorderLayout.SOUTH);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            dispose();
        }
    }

    private void saveUpdates(Patient original) {
        try {
            original.setDiseaseHistory(historyArea.getText().trim());
            original.setPrescription(prescriptionArea.getText().trim());

            // We only update history & prescription in DB
            String sql = "UPDATE Patient SET Disease_History = ?, Prescription = ? WHERE Patient_ID = ?";
            try (var conn = database.DatabaseConnection.getConnection();
                 var ps = conn.prepareStatement(sql)) {
                ps.setString(1, original.getDiseaseHistory());
                ps.setString(2, original.getPrescription());
                ps.setInt(3, original.getId());
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Patient record updated successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}