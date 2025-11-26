package gui.forms;

import database.DoctorDAO;
import database.PatientDAO;
import model.Doctor;
import model.Patient;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddPatientForm extends JFrame {
    private JTextField idField, nameField, fatherField, dobField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<Doctor> doctorCombo;
    private JTextArea historyArea, prescriptionArea;

    public AddPatientForm() {
        setTitle("Add New Patient");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Top: Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Patient ID (non-editable)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        idField.setEditable(false);
        idField.setText("Auto"); // will be auto-incremented
        formPanel.add(idField, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Patient Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Father Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Father Name:"), gbc);
        gbc.gridx = 1;
        fatherField = new JTextField(20);
        formPanel.add(fatherField, gbc);

        // Sex
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        JPanel sexPanel = new JPanel();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        maleRadio.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(maleRadio);
        group.add(femaleRadio);
        sexPanel.add(maleRadio);
        sexPanel.add(femaleRadio);
        formPanel.add(sexPanel, gbc);

        // DOB (text field)
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("DOB (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        dobField = new JTextField(15);
        formPanel.add(dobField, gbc);

        // Doctor
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        doctorCombo = new JComboBox<>();
        loadDoctors();
        formPanel.add(doctorCombo, gbc);

        // Disease History
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Disease History:"), gbc);
        gbc.gridx = 1;
        historyArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(historyArea), gbc);

        // Prescription
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Prescription:"), gbc);
        gbc.gridx = 1;
        prescriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(prescriptionArea), gbc);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> savePatient());
        btnPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadDoctors() {
        try {
            List<Doctor> doctors = new DoctorDAO().getAllWithDiseaseName();
            for (Doctor d : doctors) {
                doctorCombo.addItem(d);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePatient() {
        String name = nameField.getText().trim();
        String father = fatherField.getText().trim();
        String sex = maleRadio.isSelected() ? "Male" : "Female";
        String dobText = dobField.getText().trim();
        Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
        String history = historyArea.getText().trim();
        String prescription = prescriptionArea.getText().trim();

        if (name.isEmpty() || doctor == null) {
            JOptionPane.showMessageDialog(this, "Name and Doctor are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate dob = null;
        if (!dobText.isEmpty()) {
            try {
                dob = LocalDate.parse(dobText);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            Patient p = new Patient();
            p.setName(name);
            p.setFatherName(father);
            p.setSex(sex);
            p.setDob(dob);
            p.setDoctorId(doctor.getId());
            p.setDiseaseHistory(history);
            p.setPrescription(prescription);

            new PatientDAO().save(p);
            JOptionPane.showMessageDialog(this, "Patient saved successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}