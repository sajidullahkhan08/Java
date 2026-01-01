package ACP.Client;

import ACP.Employee.Employee;
import javax.swing.JOptionPane;
import java.io.*;

public class Client {

    static Employee[] employees = new Employee[50];
    static int empCount = 0;
    static final String DATA_FILE = "EmpDB.dat";

    public static void main(String[] args){

        JOptionPane.showMessageDialog(null,
                "Welcome to Employee Management System!\n"+
                "Max capacity: 50 employees",
                "Employee System",1);

        loadFromFile();
        showMainMenu();
    }

    private static void loadFromFile(){

        File f = new File(DATA_FILE);
        if(!f.exists()){
            JOptionPane.showMessageDialog(null,
                    "No database found.\nStarting fresh.");
            return;
        }

        try{
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(DATA_FILE));

            Employee[] temp = (Employee[])ois.readObject();
            ois.close();

            empCount = 0;
            int highestID = 8999;

            for(int i=0;i<temp.length;i++){
                if(temp[i] != null && !temp[i].isDeleted()){
                    employees[empCount++] = temp[i];
                    if(temp[i].getEmpID() > highestID)
                        highestID = temp[i].getEmpID();
                }
            }

            Employee.setNextEmpID(highestID + 1);

            JOptionPane.showMessageDialog(null,
                    "Loaded "+empCount+" employee(s) from file");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,
                    "Error loading file.\nStarting empty.");
        }
    }

    private static void saveToFile(){
        try{
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(DATA_FILE));

            Employee[] saveArr = new Employee[empCount];
            for(int i=0;i<empCount;i++)
                saveArr[i] = employees[i];

            oos.writeObject(saveArr);
            oos.close();

            JOptionPane.showMessageDialog(null,
                    "Saved "+empCount+" employee(s)");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error saving file");
        }
    }

    private static void showMainMenu(){
        while(true){
            String[] options = {
                    "Add New Employee",
                    "Update Employee",
                    "Delete Employee",
                    "Search / View",
                    "Save Data",
                    "Exit"
            };

            String choice = (String)JOptionPane.showInputDialog(
                    null,
                    "Choose option\nEmployees: "+empCount+"/50",
                    "Main Menu",
                    JOptionPane.QUESTION_MESSAGE,
                    null,options,options[0]);

            if(choice == null){
                exitProgram();
                return;
            }

            if(choice.equals("Add New Employee")) addNewEmployee();
            else if(choice.equals("Update Employee")) updateEmployee();
            else if(choice.equals("Delete Employee")) deleteEmployee();
            else if(choice.equals("Search / View")) showSearchMenu();
            else if(choice.equals("Save Data")) saveToFile();
            else if(choice.equals("Exit")) { exitProgram(); return; }
        }
    }

    private static void showSearchMenu(){
        String[] ops = {
                "By Employee ID",
                "By Name",
                "By Age",
                "By Job Category",
                "View All",
                "Back"
        };

        String ch = (String)JOptionPane.showInputDialog(
                null,"Search option","Search",
                JOptionPane.QUESTION_MESSAGE,
                null,ops,ops[0]);

        if(ch == null || ch.equals("Back")) return;

        if(ch.equals("By Employee ID")) searchByEmpID();
        else if(ch.equals("By Name")) searchByName();
        else if(ch.equals("By Age")) searchByAge();
        else if(ch.equals("By Job Category")) searchByJob();
        else if(ch.equals("View All")) viewAllEmployees();
    }

    private static void addNewEmployee(){
        if(empCount >= 50){
            JOptionPane.showMessageDialog(null,"Employee limit reached!");
            return;
        }

        Employee e = new Employee();
        if(e.setEmpInformation()){
            employees[empCount++] = e;
            JOptionPane.showMessageDialog(null,
                    "Employee added.\nTotal: "+empCount);
        }else{
            JOptionPane.showMessageDialog(null,"Employee not added.");
        }
    }

    private static void updateEmployee(){
        if(empCount == 0){
            JOptionPane.showMessageDialog(null,"No employees available");
            return;
        }

        String idStr = JOptionPane.showInputDialog("Enter Employee ID:");
        if(idStr == null) return;

        try{
            int id = Integer.parseInt(idStr);
            for(int i=0;i<empCount;i++){
                if(employees[i].getEmpID() == id && !employees[i].isDeleted()){
                    employees[i].updateEmpInformation();
                    return;
                }
            }
            JOptionPane.showMessageDialog(null,"Employee not found");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Invalid ID");
        }
    }

    private static void deleteEmployee(){
        if(empCount == 0){
            JOptionPane.showMessageDialog(null,"No employees available");
            return;
        }

        String idStr = JOptionPane.showInputDialog("Enter Employee ID:");
        if(idStr == null) return;

        try{
            int id = Integer.parseInt(idStr);
            for(int i=0;i<empCount;i++){
                if(employees[i].getEmpID() == id && !employees[i].isDeleted()){
                    int c = JOptionPane.showConfirmDialog(
                            null,"Delete this employee?",
                            "Confirm",JOptionPane.YES_NO_OPTION);
                    if(c == JOptionPane.YES_OPTION){
                        employees[i].deleteEmpInformation();
                        JOptionPane.showMessageDialog(null,"Employee deleted");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null,"Employee not found");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Invalid ID");
        }
    }

    private static void viewAllEmployees(){
        if(empCount == 0){
            JOptionPane.showMessageDialog(null,"No employees");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<empCount;i++){
            if(!employees[i].isDeleted()){
                sb.append(employees[i].toString()).append("\n\n");
            }
        }

        JOptionPane.showMessageDialog(null,
                "Employees:\n\n"+sb.toString());
    }

    private static void searchByEmpID(){
        String s = JOptionPane.showInputDialog("Enter Employee ID:");
        if(s == null) return;

        try{
            int id = Integer.parseInt(s);
            for(int i=0;i<empCount;i++){
                if(employees[i].getEmpID() == id){
                    JOptionPane.showMessageDialog(null,
                            employees[i].toString());
                    return;
                }
            }
            JOptionPane.showMessageDialog(null,"Not found");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Invalid ID");
        }
    }

    private static void searchByName(){
        String name = JOptionPane.showInputDialog("Enter name:");
        if(name == null || name.trim().isEmpty()) return;

        name = name.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<empCount;i++){
            if(!employees[i].isDeleted() &&
               employees[i].getEmployeeName().toLowerCase().contains(name)){
                sb.append(employees[i].toString()).append("\n\n");
            }
        }

        if(sb.length()>0)
            JOptionPane.showMessageDialog(null,sb.toString());
        else
            JOptionPane.showMessageDialog(null,"No match found");
    }

    private static void searchByAge(){
        String s = JOptionPane.showInputDialog("Enter age:");
        if(s == null) return;

        try{
            int age = Integer.parseInt(s);
            StringBuilder sb = new StringBuilder();

            for(int i=0;i<empCount;i++){
                if(!employees[i].isDeleted() &&
                   employees[i].calculateAge() == age){
                    sb.append(employees[i].toString()).append("\n\n");
                }
            }

            if(sb.length()>0)
                JOptionPane.showMessageDialog(null,sb.toString());
            else
                JOptionPane.showMessageDialog(null,"No employees with this age");

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Invalid age");
        }
    }

    private static void searchByJob(){
        String[] jobs = {"Teacher","Officer","Staff","Labour"};
        String j = (String)JOptionPane.showInputDialog(
                null,"Select job","Search",
                JOptionPane.QUESTION_MESSAGE,
                null,jobs,jobs[0]);

        if(j == null) return;

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<empCount;i++){
            if(!employees[i].isDeleted() &&
               employees[i].getJobCategory().equals(j)){
                sb.append(employees[i].toString()).append("\n\n");
            }
        }

        if(sb.length()>0)
            JOptionPane.showMessageDialog(null,sb.toString());
        else
            JOptionPane.showMessageDialog(null,"No employees found");
    }

    private static void exitProgram(){
        int c = JOptionPane.showConfirmDialog(
                null,"Save before exit?",
                "Exit",JOptionPane.YES_NO_CANCEL_OPTION);

        if(c == JOptionPane.CANCEL_OPTION) return;
        if(c == JOptionPane.YES_OPTION) saveToFile();

        JOptionPane.showMessageDialog(null,
                "Goodbye!\nEmployees in system: "+empCount);
        System.exit(0);
    }
}
