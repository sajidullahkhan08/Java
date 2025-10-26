package ACP.Employee;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class Employee implements Serializable {
    // Data members
    private String employeeName;
    private String fatherName;
    private int empID;
    private String jobCategory;
    private Date dateOfBirth;
    private String education;
    private int payScale;
    private String NIC;
    
    private static int nextEmpID = 9000;
    
    private static final String[] VALID_JOB_CATEGORIES = {"Teacher", "Officer", "Staff", "Labour"};
    private static final String[] VALID_EDUCATION_LEVELS = {"Matric", "FSc", "BS", "MS", "PhD"};
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    public Employee() {
        this.empID = nextEmpID++;
    }
    
    public Employee(int empID) {
        this.empID = empID;
        if (empID >= nextEmpID) {
            nextEmpID = empID + 1;
        }
    }
    
    public boolean setEmpInformation() {
        try {
            this.employeeName = JOptionPane.showInputDialog("Enter Employee Name:");
            if (this.employeeName == null || this.employeeName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Employee Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            this.fatherName = JOptionPane.showInputDialog("Enter Father's Name:");
            if (this.fatherName == null || this.fatherName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Father's Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            this.jobCategory = (String) JOptionPane.showInputDialog(
                null,
                "Select Job Category:",
                "Job Category",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_JOB_CATEGORIES,
                VALID_JOB_CATEGORIES[0]
            );
            
            if (this.jobCategory == null) {
                JOptionPane.showMessageDialog(null, "Job Category selection cancelled!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            this.education = (String) JOptionPane.showInputDialog(
                null,
                "Select Education Level:",
                "Education",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_EDUCATION_LEVELS,
                VALID_EDUCATION_LEVELS[0]
            );
            
            if (this.education == null) {
                JOptionPane.showMessageDialog(null, "Education selection cancelled!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (!validateEducationForJobCategory()) {
                return false;
            }
            
            String payScaleStr = JOptionPane.showInputDialog("Enter Pay Scale (1-20):");
            if (payScaleStr == null) {
                JOptionPane.showMessageDialog(null, "Pay Scale input cancelled!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            try {
                this.payScale = Integer.parseInt(payScaleStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Pay Scale must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (!validatePayScaleForJobCategory()) {
                return false;
            }
            
            if (!setDateOfBirthFromInput()) {
                return false;
            }
            
            this.NIC = JOptionPane.showInputDialog("Enter NIC Number:");
            if (this.NIC == null || this.NIC.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "NIC cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            JOptionPane.showMessageDialog(null, 
                "Employee added successfully!\n" +
                "Employee ID: " + this.empID + "\n" +
                "Name: " + this.employeeName +
                "\nAge: " + calculateAge() + " years",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error setting employee information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateEmpInformation() {
        JOptionPane.showMessageDialog(null, 
            "Current Employee Information:\n" + this.toString(),
            "Current Information", 
            JOptionPane.INFORMATION_MESSAGE);
        
        try {
            String newJobCategory = (String) JOptionPane.showInputDialog(
                null,
                "Select New Job Category:\nCurrent: " + this.jobCategory,
                "Update Job Category",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_JOB_CATEGORIES,
                this.jobCategory
            );
            
            if (newJobCategory == null) {
                JOptionPane.showMessageDialog(null, "Update cancelled!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            String newEducation = (String) JOptionPane.showInputDialog(
                null,
                "Select New Education Level:\nCurrent: " + this.education,
                "Update Education",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_EDUCATION_LEVELS,
                this.education
            );
            
            if (newEducation == null) {
                JOptionPane.showMessageDialog(null, "Update cancelled!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            String payScaleStr = JOptionPane.showInputDialog(
                "Enter New Pay Scale (1-20):\nCurrent: " + this.payScale);
            
            if (payScaleStr == null) {
                JOptionPane.showMessageDialog(null, "Update cancelled!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            int newPayScale;
            try {
                newPayScale = Integer.parseInt(payScaleStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Pay Scale must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            String oldJobCategory = this.jobCategory;
            String oldEducation = this.education;
            int oldPayScale = this.payScale;
            
            this.jobCategory = newJobCategory;
            this.education = newEducation;
            this.payScale = newPayScale;
            
            if (!validateEducationForJobCategory()) {
                this.jobCategory = oldJobCategory;
                this.education = oldEducation;
                this.payScale = oldPayScale;
                return false;
            }
            
            if (!validatePayScaleForJobCategory()) {
                this.jobCategory = oldJobCategory;
                this.education = oldEducation;
                this.payScale = oldPayScale;
                return false;
            }
            
            JOptionPane.showMessageDialog(null, 
                "Employee information updated successfully!\n" +
                "Employee ID: " + this.empID + "\n" +
                "Name: " + this.employeeName +
                "\n\nUpdated Fields:" +
                "\nJob Category: " + oldJobCategory + " → " + this.jobCategory +
                "\nEducation: " + oldEducation + " → " + this.education +
                "\nPay Scale: " + oldPayScale + " → " + this.payScale,
                "Update Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating employee information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void deleteEmpInformation() {
        this.employeeName = null;
        this.fatherName = null;
        this.jobCategory = null;
        this.dateOfBirth = null;
        this.education = null;
        this.payScale = 0;
        this.NIC = null;
    }
    
    public boolean isDeleted() {
        return this.employeeName == null && 
               this.fatherName == null && 
               this.jobCategory == null &&
               this.dateOfBirth == null &&
               this.education == null &&
               this.payScale == 0 &&
               this.NIC == null;
    }
    
    private boolean setDateOfBirthFromInput() {
        while (true) {
            String dobStr = JOptionPane.showInputDialog(
                "Enter Date of Birth (DD-MM-YYYY):\n" +
                "Example: 15-05-1990");
            
            if (dobStr == null) {
                return false;
            }
            
            if (dobStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Date of Birth cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            try {
                dateFormat.setLenient(false);
                this.dateOfBirth = dateFormat.parse(dobStr.trim());
                
                if (this.dateOfBirth.after(new Date())) {
                    JOptionPane.showMessageDialog(null, "Date of Birth cannot be in the future!", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                return true;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Invalid date format!\n" +
                    "Please use DD-MM-YYYY format.\n" +
                    "Example: 15-05-1990", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public int calculateAge() {
        if (this.dateOfBirth == null) {
            return 0;
        }
        
        Calendar dob = Calendar.getInstance();
        dob.setTime(this.dateOfBirth);
        Calendar today = Calendar.getInstance();
        
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        
        return age;
    }
    
    private boolean validateEducationForJobCategory() {
        int educationIndex = getEducationIndex(this.education);
        
        switch (this.jobCategory) {
            case "Teacher":
                if (educationIndex < getEducationIndex("MS")) {
                    JOptionPane.showMessageDialog(null, 
                        "Teacher must have at least MS education!\n" +
                        "Current education: " + this.education,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Officer":
                if (educationIndex < getEducationIndex("BS")) {
                    JOptionPane.showMessageDialog(null, 
                        "Officer must have at least BS education!\n" +
                        "Current education: " + this.education,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Staff":
                if (educationIndex < getEducationIndex("FSc")) {
                    JOptionPane.showMessageDialog(null, 
                        "Staff must have at least FSc education!\n" +
                        "Current education: " + this.education,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Labour":
                if (educationIndex < getEducationIndex("Matric")) {
                    JOptionPane.showMessageDialog(null, 
                        "Labour must have at least Matric education!\n" +
                        "Current education: " + this.education,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
        }
        
        return true;
    }
    
    private boolean validatePayScaleForJobCategory() {
        switch (this.jobCategory) {
            case "Teacher":
                if (this.payScale < 18) {
                    JOptionPane.showMessageDialog(null, 
                        "Teacher pay scale cannot be less than 18!\n" +
                        "Current pay scale: " + this.payScale,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Officer":
                if (this.payScale < 17) {
                    JOptionPane.showMessageDialog(null, 
                        "Officer pay scale cannot be less than 17!\n" +
                        "Current pay scale: " + this.payScale,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Staff":
                if (this.payScale < 11 || this.payScale > 16) {
                    JOptionPane.showMessageDialog(null, 
                        "Staff pay scale must be between 11 and 16!\n" +
                        "Current pay scale: " + this.payScale,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
                
            case "Labour":
                if (this.payScale < 1 || this.payScale > 10) {
                    JOptionPane.showMessageDialog(null, 
                        "Labour pay scale must be between 1 and 10!\n" +
                        "Current pay scale: " + this.payScale,
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
        }
        
        return true;
    }
    
    private int getEducationIndex(String education) {
        for (int i = 0; i < VALID_EDUCATION_LEVELS.length; i++) {
            if (VALID_EDUCATION_LEVELS[i].equals(education)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String toString() {
        if (isDeleted()) {
            return "Employee ID: " + empID + " [DELETED]";
        }
        
        String formattedDob = "Not set";
        if (dateOfBirth != null) {
            formattedDob = dateFormat.format(dateOfBirth);
        }
        
        return "Employee ID: " + empID + 
               "\nName: " + employeeName +
               "\nFather Name: " + fatherName +
               "\nJob Category: " + jobCategory +
               "\nDate of Birth: " + formattedDob +
               "\nAge: " + calculateAge() + " years" +
               "\nEducation: " + education +
               "\nPay Scale: " + payScale +
               "\nNIC: " + NIC;
    }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public int getEmpID() { return empID; }
    public String getJobCategory() { return jobCategory; }
    public void setJobCategory(String jobCategory) { this.jobCategory = jobCategory; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public int getPayScale() { return payScale; }
    public void setPayScale(int payScale) { this.payScale = payScale; }
    public String getNIC() { return NIC; }
    public void setNIC(String NIC) { this.NIC = NIC; }
    
    public static void resetEmpIDCounter() {
        nextEmpID = 9000;
    }
    
    public static void setNextEmpID(int nextID) {
        nextEmpID = nextID;
    }
}