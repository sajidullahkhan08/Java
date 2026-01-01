package ACP.Employee;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class Employee implements Serializable {

    String employeeName,fatherName,jobCategory,education,nic;
    int empID,payScale;
    Date dob;

    static int nextEmpID = 9000;

    static final String[] JOBS = {"Teacher","Officer","Staff","Labour"};
    static final String[] EDU = {"Matric","FSc","BS","MS","PhD"};

    static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public Employee(){
        empID = nextEmpID++;
    }

    public Employee(int id){
        empID = id;
        if(id >= nextEmpID)
            nextEmpID = id + 1;
    }

    public boolean setEmpInformation(){
        try{
            employeeName = JOptionPane.showInputDialog("Enter Employee Name:");
            if(employeeName == null || employeeName.trim().length()==0){
                JOptionPane.showMessageDialog(null,"Employee Name cannot be empty!");
                return false;
            }

            fatherName = JOptionPane.showInputDialog("Enter Father's Name:");
            if(fatherName == null || fatherName.trim().isEmpty()){
                JOptionPane.showMessageDialog(null,"Father Name required!");
                return false;
            }

            jobCategory = (String)JOptionPane.showInputDialog(
                    null,"Select Job Category","Job",
                    JOptionPane.QUESTION_MESSAGE,null,JOBS,JOBS[0]);

            if(jobCategory == null) return false;

            education = (String)JOptionPane.showInputDialog(
                    null,"Select Education","Education",
                    JOptionPane.QUESTION_MESSAGE,null,EDU,EDU[0]);

            if(education == null) return false;

            if(!checkEducation()) return false;

            String ps = JOptionPane.showInputDialog("Enter Pay Scale:");
            if(ps == null) return false;

            try{
                payScale = Integer.parseInt(ps);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Pay scale must be number");
                return false;
            }

            if(!checkPayScale()) return false;

            if(!setDOB()) return false;

            nic = JOptionPane.showInputDialog("Enter NIC:");
            if(nic == null || nic.trim().isEmpty()){
                JOptionPane.showMessageDialog(null,"NIC required");
                return false;
            }

            JOptionPane.showMessageDialog(null,
                    "Employee Added\nID: "+empID+
                    "\nName: "+employeeName+
                    "\nAge: "+calculateAge());
            return true;

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error: "+e.getMessage());
            return false;
        }
    }

    public boolean updateEmpInformation(){

        JOptionPane.showMessageDialog(null,"Current Info:\n"+toString());

        try{
            String newJob = (String)JOptionPane.showInputDialog(
                    null,"New Job","Update",
                    JOptionPane.QUESTION_MESSAGE,null,JOBS,jobCategory);

            if(newJob == null) return false;

            String newEdu = (String)JOptionPane.showInputDialog(
                    null,"New Education","Update",
                    JOptionPane.QUESTION_MESSAGE,null,EDU,education);

            if(newEdu == null) return false;

            String newPayStr = JOptionPane.showInputDialog("New Pay Scale:");
            if(newPayStr == null) return false;

            int newPay;
            try{
                newPay = Integer.parseInt(newPayStr);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Invalid pay scale");
                return false;
            }

            // backup
            String oldJob = jobCategory;
            String oldEdu = education;
            int oldPay = payScale;

            jobCategory = newJob;
            education = newEdu;
            payScale = newPay;

            if(!checkEducation() || !checkPayScale()){
                jobCategory = oldJob;
                education = oldEdu;
                payScale = oldPay;
                return false;
            }

            JOptionPane.showMessageDialog(null,"Employee Updated Successfully");
            return true;

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Update failed");
            return false;
        }
    }

    public void deleteEmpInformation(){
        employeeName = null;
        fatherName = null;
        jobCategory = null;
        education = null;
        dob = null;
        payScale = 0;
        nic = null;
    }

    public boolean isDeleted(){
        return employeeName == null && fatherName == null &&
               jobCategory == null && education == null &&
               dob == null && payScale == 0 && nic == null;
    }

    private boolean setDOB(){
        while(true){
            String in = JOptionPane.showInputDialog("Enter DOB (DD-MM-YYYY)");
            if(in == null) return false;
            try{
                df.setLenient(false);
                dob = df.parse(in.trim());
                if(dob.after(new Date())){
                    JOptionPane.showMessageDialog(null,"DOB cannot be future");
                }else return true;
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Invalid date format");
            }
        }
    }

    public int calculateAge(){
        if(dob == null) return 0;
        Calendar b = Calendar.getInstance();
        b.setTime(dob);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - b.get(Calendar.YEAR);
        if(now.get(Calendar.DAY_OF_YEAR) < b.get(Calendar.DAY_OF_YEAR)) age--;
        return age;
    }

    private boolean checkEducation(){
        int idx = eduIndex(education);

        if(jobCategory.equals("Teacher") && idx < eduIndex("MS")){
            JOptionPane.showMessageDialog(null,"Teacher needs MS or above");
            return false;
        }
        if(jobCategory.equals("Officer") && idx < eduIndex("BS")){
            JOptionPane.showMessageDialog(null,"Officer needs BS or above");
            return false;
        }
        if(jobCategory.equals("Staff") && idx < eduIndex("FSc")){
            JOptionPane.showMessageDialog(null,"Staff needs FSc or above");
            return false;
        }
        if(jobCategory.equals("Labour") && idx < eduIndex("Matric")){
            JOptionPane.showMessageDialog(null,"Labour needs Matric");
            return false;
        }
        return true;
    }

    private boolean checkPayScale(){
        if(jobCategory.equals("Teacher")) return payScale >= 18;
        if(jobCategory.equals("Officer")) return payScale >= 17;
        if(jobCategory.equals("Staff")) return payScale >= 11 && payScale <= 16;
        if(jobCategory.equals("Labour")) return payScale >= 1 && payScale <= 10;
        return true;
    }

    private int eduIndex(String e){
        for(int i=0;i<EDU.length;i++){
            if(EDU[i].equals(e)) return i;
        }
        return -1;
    }

    public String toString(){
        if(isDeleted()) return "Employee ID: "+empID+" [DELETED]";
        String d = (dob==null)?"Not set":df.format(dob);
        return "ID: "+empID+
                "\nName: "+employeeName+
                "\nFather: "+fatherName+
                "\nJob: "+jobCategory+
                "\nDOB: "+d+
                "\nAge: "+calculateAge()+
                "\nEducation: "+education+
                "\nPayScale: "+payScale+
                "\nNIC: "+nic;
    }

    // getters / setters (kept minimal)
    public int getEmpID(){return empID;}
    public String getEmployeeName(){return employeeName;}
    public void setEmployeeName(String n){employeeName=n;}
    public String getJobCategory(){return jobCategory;}

    public static void resetEmpIDCounter(){ nextEmpID = 9000; }
    public static void setNextEmpID(int id){ nextEmpID = id; }
}
