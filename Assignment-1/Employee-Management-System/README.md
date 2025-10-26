# Employee Management System

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [System Architecture](#system-architecture)
3. [Class Structure](#class-structure)
4. [Package Organization](#package-organization)
5. [Data Members & Validation](#data-members--validation)
6. [Functionality Implementation](#functionality-implementation)
7. [File Persistence](#file-persistence)
8. [User Interface](#user-interface)
9. [Business Logic](#business-logic)
10. [Error Handling](#error-handling)
11. [Compilation & Execution](#compilation--execution)
12. [Usage Guide](#usage-guide)

## 🎯 Project Overview

The **Employee Management System** is a comprehensive Java application designed to manage employee records with full CRUD (Create, Read, Update, Delete) operations. The system features a graphical user interface using Swing dialogs, persistent data storage through Java serialization, and robust validation of business rules.

### Key Features
- **Employee Record Management**: Add, update, delete, and search employee records
- **Data Validation**: Enforce education and pay scale rules based on job categories
- **Persistent Storage**: Automatic saving/loading of data using serialization
- **User-Friendly Interface**: Graphical dialogs for all user interactions
- **Array-Based Storage**: Manual array management without using Collections framework
- **Auto-generated IDs**: Unique employee IDs starting from 9000

## 🏗️ System Architecture

### High-Level Design
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client Class  │────│   Employee Class │────│  File System    │
│   (Controller)  │    │    (Model)       │    │  (Persistence)  │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                       │
         │ 1. Manages Array      │ 2. Encapsulates Data  │ 3. Serializes Objects
         │ 4. Handles UI         │ 5. Validates Rules    │ 6. Stores to EmpDB.dat
```

### Data Flow
1. **User Input** → Client class receives input via JOptionPane
2. **Business Logic** → Employee class validates and processes data
3. **Storage** → Serialization saves objects to binary file
4. **Retrieval** → Deserialization loads objects on startup

## 📁 Package Organization

### Directory Structure
```
ProjectRoot/
├── ACP/
│   ├── Employee/
│   │   └── Employee.java
│   └── Client/
│       └── Client.java
└── EmpDB.dat (generated at runtime)
```

### Package Details
- **`ACP.Employee`**: Contains the `Employee` class representing the data model
- **`ACP.Client`**: Contains the `Client` class with main method and application logic

## 🏛️ Class Structure

### Employee Class (`ACP.Employee.Employee`)

#### Data Members
```java
private String employeeName;      // Employee's full name
private String fatherName;        // Father's name
private int empID;                // Auto-generated unique identifier
private String jobCategory;       // Teacher, Officer, Staff, Labour
private Date dateOfBirth;         // Date object for birth date
private String education;         // Matric, FSc, BS, MS, PhD
private int payScale;             // Numeric pay scale value
private String NIC;               // National Identity Card number

// Static members
private static int nextEmpID = 9000;  // Auto-incrementing ID counter
private static final String[] VALID_JOB_CATEGORIES = {...};
private static final String[] VALID_EDUCATION_LEVELS = {...};
```

#### Key Methods

**1. Constructor**
```java
public Employee()
```
- Auto-generates EmpID using `nextEmpID++`
- Ensures unique IDs across application sessions

**2. setEmpInformation()**
```java
public boolean setEmpInformation()
```
- Collects all employee data via input dialogs
- Validates education and pay scale rules
- Returns boolean indicating success/failure
- Handles user cancellation gracefully

**3. updateEmpInformation()**
```java
public boolean updateEmpInformation()
```
- Allows updating only: education, pay scale, job category
- Shows current values before update
- Validates new values against business rules
- Provides before/after comparison

**4. deleteEmpInformation()**
```java
public void deleteEmpInformation()
```
- Resets all fields to null/zero values
- Preserves EmpID for tracking
- Marks record as logically deleted

**5. Validation Methods**
```java
private boolean validateEducationForJobCategory()
private boolean validatePayScaleForJobCategory()
```
- Enforce business rules based on job category
- Provide user-friendly error messages

### Client Class (`ACP.Client.Client`)

#### Data Members
```java
private static Employee[] employees = new Employee[50];  // Fixed-size array
private static int employeeCount = 0;                    // Current record count
private static final String DATA_FILE = "EmpDB.dat";     // Persistence file
```

#### Key Methods

**1. Main Method & Initialization**
```java
public static void main(String[] args)
private static void loadFromFile()
```
- Entry point of application
- Loads existing data from `EmpDB.dat`
- Handles first-run scenario

**2. Menu Management**
```java
private static void showMainMenu()
private static void showSearchMenu()
```
- Provides hierarchical menu navigation
- Uses JOptionPane for user interaction
- Handles menu cancellation

**3. CRUD Operations**
```java
private static void addNewEmployee()
private static void updateEmployee()
private static void deleteEmployee()
```
- Implements core business functionality
- Manages array operations manually
- Provides user feedback

**4. Search Operations**
```java
private static void searchByEmpID()
private static void searchByName()
private static void searchByAge()
private static void searchByJobCategory()
private static void viewAllEmployees()
```
- Multiple search criteria support
- Partial name matching
- Age-based filtering
- Category-based grouping

**5. Persistence**
```java
private static void saveToFile()
private static void exitProgram()
```
- Implements Java serialization
- Handles file I/O exceptions
- Provides save confirmation on exit

## 📊 Data Members & Validation

### Employee Data Validation Rules

#### Job Category Constraints
| Category | Minimum Education | Pay Scale Range | Validation Logic |
|----------|------------------|-----------------|------------------|
| Teacher  | MS               | 18+             | `educationIndex >= MS_INDEX` |
| Officer  | BS               | 17+             | `educationIndex >= BS_INDEX` |
| Staff    | FSc              | 11-16           | `educationIndex >= FSC_INDEX` |
| Labour   | Matric           | 1-10            | `educationIndex >= MATRIC_INDEX` |

#### Education Hierarchy
```java
// Index-based validation (higher index = higher education)
0: "Matric"  // Lowest
1: "FSc"
2: "BS" 
3: "MS"
4: "PhD"     // Highest
```

#### Input Validation
- **Required Fields**: All fields must be non-empty
- **Date Format**: Strict DD-MM-YYYY parsing with future date prevention
- **Numeric Validation**: Pay scale must be valid integer
- **Category Selection**: Pre-defined options only

## ⚙️ Functionality Implementation

### Array Management

**Manual Array Operations**
```java
// Adding new employee
employees[employeeCount] = newEmployee;
employeeCount++;

// Searching by ID
for (int i = 0; i < employeeCount; i++) {
    if (employees[i].getEmpID() == searchID) {
        // Found employee
    }
}

// Handling deleted records
if (!employees[i].isDeleted()) {
    // Process active records only
}
```

**Capacity Management**
- Fixed array size of 50 elements
- `employeeCount` tracks active records
- Prevents overflow with boundary checks

### Search Algorithms

**1. Exact Match (ID Search)**
- Linear search through array
- Direct integer comparison
- Fast O(n) operation for small datasets

**2. Partial Match (Name Search)**
```java
if (employees[i].getEmployeeName().toLowerCase().contains(searchName)) {
    // Case-insensitive partial matching
}
```

**3. Range-based Search (Age)**
- Calculates age from Date of Birth
- Integer comparison for exact age matching

**4. Category-based Search**
- Exact string matching for job categories
- Groups employees by predefined categories

## 💾 File Persistence

### Serialization Implementation

**Saving Data**
```java
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
    Employee[] employeesToSave = new Employee[employeeCount];
    // Copy active records to temporary array
    oos.writeObject(employeesToSave);
    oos.flush();
}
```

**Loading Data**
```java
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
    Employee[] loadedEmployees = (Employee[]) ois.readObject();
    // Rebuild array and update counters
    Employee.setNextEmpID(highestEmpID + 1);  // Continue ID sequence
}
```

### Persistence Features

**1. Auto-recovery**
- Loads existing data on startup
- Continues EmpID sequence correctly
- Maintains data integrity across sessions

**2. Error Resilience**
- Continues operation if file not found
- Provides informative error messages
- Prevents data corruption

**3. Storage Optimization**
- Saves only active (non-deleted) records
- Maintains array structure
- Efficient binary storage

## 🎨 User Interface

### Dialog-based Interaction

**Input Dialogs**
```java
String name = JOptionPane.showInputDialog("Enter Employee Name:");
// Handles null (user cancellation) gracefully

String category = (String) JOptionPane.showInputDialog(
    null, "Select Job Category:", "Job Category",
    JOptionPane.QUESTION_MESSAGE, null, VALID_JOB_CATEGORIES, VALID_JOB_CATEGORIES[0]
);
// Dropdown selection for predefined options
```

**Message Dialogs**
```java
JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
```

**Confirmation Dialogs**
```java
int confirm = JOptionPane.showConfirmDialog(
    null, "Are you sure?", "Confirm Deletion", 
    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
);
```

### UI Flow Design

**Main Menu Navigation**
```
Main Menu → Add/Update/Delete/Search/Exit
    ↓
Search Menu → By ID/Name/Age/Category/All/Back
    ↓
Results Display → Formatted employee information
```

## 🔧 Business Logic

### Education Validation Algorithm

```java
private boolean validateEducationForJobCategory() {
    int educationIndex = getEducationIndex(this.education);
    
    switch (this.jobCategory) {
        case "Teacher":
            return educationIndex >= getEducationIndex("MS");
        case "Officer":
            return educationIndex >= getEducationIndex("BS");
        // ... other cases
    }
}
```

### Pay Scale Validation Algorithm

```java
private boolean validatePayScaleForJobCategory() {
    switch (this.jobCategory) {
        case "Teacher":
            return this.payScale >= 18;
        case "Officer":
            return this.payScale >= 17;
        case "Staff":
            return this.payScale >= 11 && this.payScale <= 16;
        case "Labour":
            return this.payScale >= 1 && this.payScale <= 10;
    }
}
```

### Age Calculation

```java
public int calculateAge() {
    Calendar dob = Calendar.getInstance();
    dob.setTime(this.dateOfBirth);
    Calendar today = Calendar.getInstance();
    
    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
    
    // Adjust for birthday not occurred this year
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--;
    }
    
    return age;
}
```

## 🛡️ Error Handling

### Comprehensive Exception Management

**Input Validation Errors**
- Empty field detection
- Invalid number format
- Incorrect date format
- Future date prevention

**File Operation Errors**
```java
try {
    // File operations
} catch (FileNotFoundException e) {
    // Handle missing file (first run)
} catch (IOException e) {
    // Handle I/O errors
} catch (ClassNotFoundException e) {
    // Handle serialization format issues
} catch (Exception e) {
    // Catch-all for unexpected errors
}
```

**Business Rule Violations**
- Education level too low for job category
- Pay scale outside allowed range
- User cancellation handling

### User Feedback System

**Success Messages**
- Clear confirmation of completed operations
- Summary of changes made
- Updated record counts

**Error Messages**
- Specific, actionable information
- Guidance for correction
- Non-technical language

**Warning Messages**
- Confirmations for destructive operations
- Notifications about constraints
- Information about system state

## 🔨 Compilation & Execution

### Compilation Commands
```bash
# Compile Employee class
javac ACP/Employee/Employee.java

# Compile Client class
javac ACP/Client/Client.java

# Run application
java ACP.Client.Client
```

### Classpath Considerations
- Both classes must be compiled successfully
- Java Swing required (included in standard JDK)
- Serialization supported in standard Java

### Runtime Environment
- **Java Version**: JDK 8 or higher
- **Dependencies**: Standard Java libraries only
- **File System**: Write permissions for current directory

## 📖 Usage Guide

### Starting the Application

1. **First Run**
   - Program detects no existing database
   - Starts with empty employee array
   - EmpID sequence begins at 9000

2. **Subsequent Runs**
   - Automatically loads `EmpDB.dat`
   - Continues EmpID sequence from highest saved ID
   - Maintains all previous records

### Adding Employees

**Step-by-Step Process:**
1. Select "Add New Employee" from main menu
2. Enter employee name (required)
3. Enter father's name (required)
4. Select job category from dropdown
5. Select education level from dropdown
6. Enter pay scale (validated against category)
7. Enter date of birth (DD-MM-YYYY format)
8. Enter NIC number (required)

**Validation During Addition:**
- Education level checked against job category
- Pay scale validated for selected category
- Date format strictly enforced
- All fields verified for non-empty values

### Updating Employees

**Allowed Updates:**
- Job Category (with re-validation)
- Education Level (with re-validation) 
- Pay Scale (with re-validation)

**Update Process:**
1. Select "Update Employee Information"
2. Enter Employee ID to update
3. View current information
4. Select new values from dialogs
5. Confirm changes with validation

### Deleting Employees

**Deletion Safety Features:**
- Confirmation dialog before deletion
- Shows employee details before deleting
- Logical deletion (record preserved but empty)
- Prevention of duplicate deletion attempts

### Searching Employees

**Search Methods Available:**
1. **By Employee ID**: Exact match search
2. **By Name**: Partial, case-insensitive search
3. **By Age**: Exact age calculation match
4. **By Job Category**: Group employees by category
5. **View All**: Complete database overview

**Search Result Display:**
- Formatted employee information
- Multiple results displayed together
- Clear indication of deleted records

### Data Persistence

**Manual Saving:**
- Use "Save Data to File" from main menu
- Immediate confirmation of save operation
- Count of saved records displayed

**Automatic Saving:**
- Prompt on program exit
- Option to save, exit without saving, or cancel
- Prevents accidental data loss

## 🎯 Design Patterns & Principles

### Implemented Patterns

**1. Model-View-Controller (MVC)**
- **Model**: Employee class (data and business logic)
- **View**: JOptionPane dialogs (user interface)
- **Controller**: Client class (application flow)

**2. Serialization Pattern**
- Object state preservation
- Platform-independent data storage
- Versioning support through Serializable interface

**3. Validation Pattern**
- Centralized validation logic
- Reusable validation methods
- Consistent error reporting

### Object-Oriented Principles

**Encapsulation**
- Private data members with public getters/setters
- Controlled access through methods
- Internal validation logic

**Abstraction**
- Complex operations hidden behind simple method calls
- User-friendly interfaces over complex logic

**Reusability**
- Generic search and validation methods
- Modular code organization
- Extensible design

## 🔮 Potential Enhancements

### Immediate Improvements
1. **Advanced Search**: Range-based age search, combined criteria
2. **Data Export**: CSV or PDF report generation
3. **Input Enhancement**: Calendar widget for date selection
4. **Data Analytics**: Statistics and reporting features

### Architectural Extensions
1. **Database Backend**: Replace serialization with SQL database
2. **Network Support**: Client-server architecture
3. **Web Interface**: Servlet-based web application
4. **Mobile App**: Android/iOS companion application

## 📝 Conclusion

This Employee Management System demonstrates comprehensive Java programming skills including:

- **Object-Oriented Design**: Clean class structure and encapsulation
- **User Interface Development**: Swing-based dialog system
- **Data Persistence**: Robust file I/O with serialization
- **Business Logic**: Complex validation rules implementation
- **Error Handling**: Comprehensive exception management
- **Array Manipulation**: Manual data structure management

The system successfully meets all assignment requirements while providing a robust, user-friendly solution for employee record management.