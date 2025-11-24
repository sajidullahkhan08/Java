package gui.forms;

import utils.PrintUtility;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SearchResultsDialog extends JDialog {
    private JTable resultsTable;
    private JButton closeButton, printButton;
    private String dialogTitle;
    
    public SearchResultsDialog(JFrame parent, String title, Object[][] data, String[] columnNames) {
        super(parent, title, true);
        this.dialogTitle = title;
        initializeUI(data, columnNames);
        pack();
        setLocationRelativeTo(parent);
        setSize(800, 400);
    }
    
    private void initializeUI(Object[][] data, String[] columnNames) {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table with non-editable model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(model);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printButton = new JButton("Print");
        closeButton = new JButton("Close");
        
        printButton.addActionListener(e -> printResults());
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add double-click listener to view details
        resultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = resultsTable.getSelectedRow();
                    if (row != -1) {
                        showDetailsDialog(row);
                    }
                }
            }
        });
    }
    
    private void printResults() {
        PrintUtility.printTable(resultsTable, dialogTitle);
    }
    
    private void showDetailsDialog(int row) {
        int modelRow = resultsTable.convertRowIndexToModel(row);
        StringBuilder details = new StringBuilder();
        
        for (int i = 0; i < resultsTable.getColumnCount(); i++) {
            String columnName = resultsTable.getColumnName(i);
            Object value = resultsTable.getModel().getValueAt(modelRow, i);
            if (value != null) {
                details.append(columnName).append(": ").append(value).append("\n");
            }
        }
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, 
            scrollPane, 
            "Record Details", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}