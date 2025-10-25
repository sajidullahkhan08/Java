package ACP.Employee;

import java.io.Serializable;
import java.util.Date;
import java.swing.JOptionPane;

public class Employee implements Serializable {
    private String employeeName;
    private String fatherName;
    private int empID;
    private String jobCategory;
    private Date dateOfBirth;
    private String education;
    private int payScale;
    private String NIC;

    private static int nextEmpID = 900;

    private static final String[] VALID_JOB_CATEGORIES = {
        "Teacher",
        "Officer",
        "Staff",
        "Labor"
    };

    private static final String[] VALID_EDUCATION_LEVELS = {
        "Matric",
        "Fsc",
        "BS",
        "MS",
        "PhD"
    };

    public Employee() {
        this.empID = nextEmpID++;
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
            this.jobCategory = (String) javax.swing.JOptionPane.showInputDialog(
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
            this.education = (String) javax.swing.JOptionPane.showInputDialog(
                null,
                "Select Education Level:",
                "Education",
                JOptionPane.QUESTION_MESSAGE,
                null,
                VALID_EDUCATION_LEVELS
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
            if(payScaleStr == null) {
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
            String dobStr = JOptionPane.showInputDialog("Enter Date of Birth (DD-MM-YYYY):");
            if (dobStr == null || dobStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Date of Birth cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            this.dateOfBirth = new Date();

            this.NIC = JOptionPane.showInputDialog("Enter NIC Number:");
            if (this.NIC == null || this.NIC.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "NIC cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            JOptionPane.showMessageDialog(
                null,
                "Employee added successfully!\n" +
                "Employee ID: " + this.empID, "\n" +
                "Name: " + this.employeeName,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error setting employee information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validateEducationForJobCategory() {
        int educationIndex = getEducationIndex(this.education);

        switch (this.jobCategory) {
            case "Teacher":
                if (educationIndex < getEducationIndex("MS")) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Teachers must have at least an MS degree education!\n" +
                        "Current Education: " + this.education,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Officer":
                if (educationIndex < getEducationIndex("BS")) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Officers must have at least a BS degree education!\n" +
                        "Current Education: " + this.education,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Staff":
                if (educationIndex < getEducationIndex("Fsc")) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Staff must have at least an Fsc education!\n" +
                        "Current Education: " + this.education,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Labor":
                if (educationIndex < getEducationIndex("Matric")) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Labors must have at least a Matric education!\n" +
                        "Current Education: " + this.education,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
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
                    JOptionPane.showMessageDialog(
                        null, 
                        "Teachers must have a Pay Scale of at least 18!\n" +
                        "Current Pay Scale: " + this.payScale,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Officer":
                if (this.payScale < 17) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Officers must have a Pay Scale of at least 17!\n" +
                        "Current Pay Scale: " + this.payScale,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Staff":
                if (this.payScale < 11 || this.payScale > 16) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Staff must have a Pay Scale between 11 and 16!\n" +
                        "Current Pay Scale: " + this.payScale,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                break;
            case "Labor":
                if (this.payScale < 1 || this.payScale > 10) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Labors must have a Pay Scale between 1 and 10!\n" +
                        "Current Pay Scale: " + this.payScale,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
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

    public String getEmployeeName() { reurn employeeName; }
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

    public String toString() {
        return "Employee ID: " + empID + "\n" +
               "Name: " + employeeName + "\n" +
               "Father's Name: " + fatherName + "\n" +
               "Job Category: " + jobCategory + "\n" +
               "Date of Birth: " + dateOfBirth + "\n" +
               "Education: " + education + "\n" +
               "Pay Scale: " + payScale + "\n" +
               "NIC: " + NIC;
    }
}
