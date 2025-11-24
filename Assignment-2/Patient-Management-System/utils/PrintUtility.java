package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class PrintUtility implements Printable {
    private Component component;
    
    public PrintUtility(Component component) {
        this.component = component;
    }
    
    public static void printComponent(Component component) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new PrintUtility(component));
        
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null,
                    "Print failed: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
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
        g2d.scale(0.7, 0.7); // Scale to fit page
        
        // Prevent the component from changing size when printed
        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            boolean doubleBuffered = jComponent.isDoubleBuffered();
            jComponent.setDoubleBuffered(false);
            component.printAll(g2d);
            jComponent.setDoubleBuffered(doubleBuffered);
        } else {
            component.printAll(g2d);
        }
        
        return PAGE_EXISTS;
    }
    
    public static void printTable(JTable table, String title) {
        // Create a printable table
        JTable printableTable = new JTable(table.getModel());
        printableTable.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Create a panel with title and table
        JPanel printPanel = new JPanel(new BorderLayout());
        
        // Add title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        printPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Add table
        JScrollPane scrollPane = new JScrollPane(printableTable);
        printPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add print date
        JLabel dateLabel = new JLabel("Printed on: " + new java.util.Date(), JLabel.LEFT);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        printPanel.add(dateLabel, BorderLayout.SOUTH);
        
        printPanel.setPreferredSize(new Dimension(600, 400));
        
        // Print the panel
        printComponent(printPanel);
    }
}