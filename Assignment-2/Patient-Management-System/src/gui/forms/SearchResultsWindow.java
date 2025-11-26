package gui.forms;

import model.Patient;
import utils.TableModelHelper;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchResultsWindow extends JFrame {
    public SearchResultsWindow(List<?> results) {
        setTitle("Search Results");
        setSize(800, 400);
        setLocationRelativeTo(null);

        JTable table;
        if (!results.isEmpty() && results.get(0) instanceof Patient) {
            table = TableModelHelper.createPatientTable((List<Patient>) results);
        } else {
            table = new JTable();
        }

        add(new JScrollPane(table));
    }
}