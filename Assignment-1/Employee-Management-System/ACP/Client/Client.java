package ACP.Client;

import ACP.Employee.Employee;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.Arrays;

public class Client {
    private static Employee[] employees = new Employee[50];
    private static int employeeCount = 0;
    private static final String DATA_FILE = "EmpDB.dat";
    
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, 
            "Welcome to Employee Management System!\n" +
            "Maximum capacity: 50 employees\n" +
            "Starting program...", 
            "Employee System", 
            JOptionPane.INFORMATION_MESSAGE);
        
        loadFromFile();
        
        showMainMenu();
    }
    
    private static void loadFromFile() {
        File file = new File(DATA_FILE);
        
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, 
                "No existing employee database found.\n" +
                "Starting with empty system.",
                "First Run", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Employee[] loadedEmployees = (Employee[]) ois.readObject();
            
            employeeCount = 0;
            int highestEmpID = 8999;
            
            for (Employee emp : loadedEmployees) {
                if (emp != null && !emp.isDeleted()) {
                    employees[employeeCount] = emp;
                    employeeCount++;
                    
                    if (emp.getEmpID() > highestEmpID) {
                        highestEmpID = emp.getEmpID();
                    }
                }
            }
            
            Employee.setNextEmpID(highestEmpID + 1);
            
            JOptionPane.showMessageDialog(null, 
                "Successfully loaded " + employeeCount + " employee(s) from database.",
                "Data Loaded", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Database file not found. Starting with empty system.",
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error reading database file: " + e.getMessage() + 
                "\nStarting with empty system.",
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Database file format error: " + e.getMessage() + 
                "\nStarting with empty system.",
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Unexpected error loading data: " + e.getMessage() + 
                "\nStarting with empty system.",
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            Employee[] employeesToSave = new Employee[employeeCount];
            for (int i = 0; i < employeeCount; i++) {
                employeesToSave[i] = employees[i];
            }
            
            oos.writeObject(employeesToSave);
            oos.flush();
            
            JOptionPane.showMessageDialog(null, 
                "Successfully saved " + employeeCount + " employee(s) to database.",
                "Data Saved", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error saving database: " + e.getMessage(),
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Unexpected error saving data: " + e.getMessage(),
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void showMainMenu() {
        while (true) {
            String[] options = {
                "Add New Employee",
                "Update Employee Information", 
                "Delete Employee Record",
                "Search & View Employee",
                "Save Data to File",
                "Exit"
            };
            
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Select an option:\nCurrent employees: " + employeeCount + "/50",
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
            
            switch (choice) {
                case "Add New Employee":
                    addNewEmployee();
                    break;
                case "Update Employee Information":
                    updateEmployee();
                    break;
                case "Delete Employee Record":
                    deleteEmployee();
                    break;
                case "Search & View Employee":
                    showSearchMenu();
                    break;
                case "Save Data to File":
                    saveToFile();
                    break;
                case "Exit":
                    exitProgram();
                    return;
            }
        }
    }
    
    private static void showSearchMenu() {
        String[] searchOptions = {
            "Search by Employee ID",
            "Search by Employee Name", 
            "Search by Age",
            "Search by Job Category",
            "View All Employees",
            "Back to Main Menu"
        };
        
        String searchChoice = (String) JOptionPane.showInputDialog(
            null,
            "Select search method:",
            "Search Employee",
            JOptionPane.QUESTION_MESSAGE,
            null,
            searchOptions,
            searchOptions[0]
        );
        
        if (searchChoice == null || searchChoice.equals("Back to Main Menu")) {
            return;
        }
        
        switch (searchChoice) {
            case "Search by Employee ID":
                searchByEmpID();
                break;
            case "Search by Employee Name":
                searchByName();
                break;
            case "Search by Age":
                searchByAge();
                break;
            case "Search by Job Category":
                searchByJobCategory();
                break;
            case "View All Employees":
                viewAllEmployees();
                break;
        }
    }
    
    private static void viewAllEmployees() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder allEmployees = new StringBuilder();
        allEmployees.append("=== ALL EMPLOYEES ===\n\n");
        
        for (int i = 0; i < employeeCount; i++) {
            if (!employees[i].isDeleted()) {
                allEmployees.append("--- Employee ").append(i + 1).append(" ---\n");
                allEmployees.append(employees[i].toString()).append("\n\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, 
            "Total active employees: " + employeeCount + "\n\n" + allEmployees.toString(),
            "All Employees", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void updateEmployee() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system to update!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String empIDStr = JOptionPane.showInputDialog("Enter Employee ID to update:");
        if (empIDStr == null) return;
        
        try {
            int searchID = Integer.parseInt(empIDStr);
            Employee foundEmployee = null;
            int foundIndex = -1;
            
            for (int i = 0; i < employeeCount; i++) {
                if (employees[i].getEmpID() == searchID && !employees[i].isDeleted()) {
                    foundEmployee = employees[i];
                    foundIndex = i;
                    break;
                }
            }
            
            if (foundEmployee != null) {
                boolean success = foundEmployee.updateEmpInformation();
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Employee record updated successfully!\n" +
                        "Employee ID: " + foundEmployee.getEmpID(),
                        "Update Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No active employee found with ID: " + searchID + "\n" +
                    "Note: Deleted employees cannot be updated.",
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Employee ID number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void deleteEmployee() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system to delete!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String empIDStr = JOptionPane.showInputDialog("Enter Employee ID to delete:");
        if (empIDStr == null) return;
        
        try {
            int searchID = Integer.parseInt(empIDStr);
            Employee foundEmployee = null;
            int foundIndex = -1;
            
            for (int i = 0; i < employeeCount; i++) {
                if (employees[i].getEmpID() == searchID && !employees[i].isDeleted()) {
                    foundEmployee = employees[i];
                    foundIndex = i;
                    break;
                }
            }
            
            if (foundEmployee != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this employee?\n\n" +
                    foundEmployee.toString() +
                    "\n\nThis action cannot be undone!",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    String deletedName = foundEmployee.getEmployeeName();
                    foundEmployee.deleteEmpInformation();
                    
                    JOptionPane.showMessageDialog(null, 
                        "Employee record deleted successfully!\n" +
                        "Name: " + deletedName + "\n" +
                        "Employee ID: " + searchID + " (marked as deleted)",
                        "Deletion Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No active employee found with ID: " + searchID + "\n" +
                    "Note: Already deleted employees cannot be deleted again.",
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Employee ID number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void searchByEmpID() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String empIDStr = JOptionPane.showInputDialog("Enter Employee ID to search:");
        if (empIDStr == null) return;
        
        try {
            int searchID = Integer.parseInt(empIDStr);
            boolean found = false;
            
            for (int i = 0; i < employeeCount; i++) {
                if (employees[i].getEmpID() == searchID) {
                    if (employees[i].isDeleted()) {
                        JOptionPane.showMessageDialog(null, 
                            "Employee with ID " + searchID + " was found but is DELETED.",
                            "Deleted Employee", 
                            JOptionPane.WARNING_MESSAGE);
                    } else {
                        displayEmployee(employees[i], "Employee Found by ID");
                    }
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(null, "No employee found with ID: " + searchID, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Employee ID number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void searchByName() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String searchName = JOptionPane.showInputDialog("Enter Employee Name to search:");
        if (searchName == null || searchName.trim().isEmpty()) return;
        
        searchName = searchName.trim().toLowerCase();
        StringBuilder results = new StringBuilder();
        int foundCount = 0;
        
        for (int i = 0; i < employeeCount; i++) {
            if (!employees[i].isDeleted() && 
                employees[i].getEmployeeName().toLowerCase().contains(searchName)) {
                results.append("--- Employee ").append(++foundCount).append(" ---\n");
                results.append(employees[i].toString()).append("\n\n");
            }
        }
        
        if (foundCount > 0) {
            JOptionPane.showMessageDialog(null, 
                "Found " + foundCount + " employee(s) matching: '" + searchName + "'\n\n" + results.toString(),
                "Search Results by Name", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No active employees found with name containing: '" + searchName + "'", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void searchByAge() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String ageStr = JOptionPane.showInputDialog("Enter Age to search:");
        if (ageStr == null) return;
        
        try {
            int searchAge = Integer.parseInt(ageStr);
            StringBuilder results = new StringBuilder();
            int foundCount = 0;
            
            for (int i = 0; i < employeeCount; i++) {
                if (!employees[i].isDeleted() && employees[i].calculateAge() == searchAge) {
                    results.append("--- Employee ").append(++foundCount).append(" ---\n");
                    results.append(employees[i].toString()).append("\n\n");
                }
            }
            
            if (foundCount > 0) {
                JOptionPane.showMessageDialog(null, 
                    "Found " + foundCount + " employee(s) with age: " + searchAge + "\n\n" + results.toString(),
                    "Search Results by Age", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No active employees found with age: " + searchAge, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid age number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void searchByJobCategory() {
        if (employeeCount == 0) {
            JOptionPane.showMessageDialog(null, "No employees in the system!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] categories = {"Teacher", "Officer", "Staff", "Labour"};
        String searchCategory = (String) JOptionPane.showInputDialog(
            null,
            "Select Job Category to search:",
            "Search by Job Category",
            JOptionPane.QUESTION_MESSAGE,
            null,
            categories,
            categories[0]
        );
        
        if (searchCategory == null) return;
        
        StringBuilder results = new StringBuilder();
        int foundCount = 0;
        
        for (int i = 0; i < employeeCount; i++) {
            if (!employees[i].isDeleted() && employees[i].getJobCategory().equals(searchCategory)) {
                results.append("--- Employee ").append(++foundCount).append(" ---\n");
                results.append(employees[i].toString()).append("\n\n");
            }
        }
        
        if (foundCount > 0) {
            JOptionPane.showMessageDialog(null, 
                "Found " + foundCount + " employee(s) in category: '" + searchCategory + "'\n\n" + results.toString(),
                "Search Results by Job Category", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No active employees found in category: '" + searchCategory + "'", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void displayEmployee(Employee emp, String title) {
        JOptionPane.showMessageDialog(null, emp.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void addNewEmployee() {
        if (employeeCount >= 50) {
            JOptionPane.showMessageDialog(null, 
                "Cannot add new employee!\n" +
                "Maximum capacity (50 employees) reached!",
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
                "Total employees now: " + employeeCount + "/50",
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
        int saveChoice = JOptionPane.showConfirmDialog(
            null,
            "Do you want to save all changes to the database before exiting?",
            "Save Before Exit",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (saveChoice == JOptionPane.YES_OPTION) {
            saveToFile();
        } else if (saveChoice == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        JOptionPane.showMessageDialog(null, 
            "Thank you for using Employee Management System!\n" +
            "Total employees in system: " + employeeCount,
            "Goodbye", 
            JOptionPane.INFORMATION_MESSAGE);
        
        System.exit(0);
    }
}