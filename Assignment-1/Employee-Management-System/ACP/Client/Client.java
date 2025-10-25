package ACP.Client;

import ACP.Employee.Employee;
import javax.swing.JOptionPane;

public class Client {
    private static Employee[] employees = new Employee[50];
    private static int employeeCount = 0;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
            "Welcome to the Employee Management System!\n" +
            "Maximum capacity: 50 employees.\n" +
            "Starting program...",
            "Employee Management System",
            JOptionPane.INFORMATION_MESSAGE);

        showMainMenu();
    }

    private static void showMainMenu() {
        while(true) {
            String[] options = {
                "Add New Employee",
                "Update Employee Information",
                "Delete Employee Record",
                "Search & View Employee Details",
                "Exit"
            };

            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Select an option:",
                "Main Menu",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == null) {
                exitProgram();
                break;
            }

            switch(choice) {
                case "Add New Employee":
                    JOptionPane.showMessageDialog(null, "Add Employee coming in Phase 2!.");
                    break;
                case "Update Employee Information":
                    JOptionPane.showMessageDialog(null, "Update Employee coming in Phase 4!.");
                    break;
                case "Delete Employee Record":
                    JOptionPane.showMessageDialog(null, "Delete Employee coming in Phase 4!.");
                    break;
                case "Search & View Employee Details":
                    JOptionPane.showMessageDialog(null, "Search & View Employee coming in Phase 3!");
                    break;
                case "Exit":
                    exitProgram();
                    return;
            }
        }
    }

    private static void addNewEmployee() {
        if (employeeCount >= 50) {
            JOptionPane.showMessageDialog(null,
                "Cannot add new employee!\n" +
                "Maximum capacity of 50 employees reached!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee newEmployee = new Employee();
        boolean success = newEmployee.setEmpInformation();

        if (success) {
            employees[employeeCount] = newEmployee;
            employeeCount++;
            JOptionPane.showMessageDialog(null,
                "Employee added successfully!\n" +
                "Total Employees now: " + employeeCount + "/50",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Failed to add employee!\n" +
                "Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void exitProgram() {
        JOptionPane.showMessageDialog(null,
            "Thank you for using Employee Management System!\n" +
            "Total Employees in System: " + employeeCount,
            "Goodbye!",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
