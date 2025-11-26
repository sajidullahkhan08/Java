package utils;

import model.Patient;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModelHelper {
    public static JTable createPatientTable(List<Patient> patients) {
        String[] columns = {"ID", "Name", "Father", "Sex", "DOB", "Doctor", "History", "Prescription"};
        Object[][] data = new Object[patients.size()][];
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            data[i] = new Object[]{
                p.getId(),
                p.getName(),
                p.getFatherName(),
                p.getSex(),
                p.getDob() != null ? p.getDob().toString() : "",
                p.getDoctorName(),
                p.getDiseaseHistory(),
                p.getPrescription()
            };
        }
        return new JTable(data, columns);
    }
}