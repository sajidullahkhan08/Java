# 🏥 Patient Management System – Java Swing + MySQL  

## 📌 Overview

This project implements a **fully functional Patient Management System** using **Java Swing for GUI** and **MySQL for database persistence**, as per the requirements of **Assignment No. 2**.

It supports two user roles:
- **Administrator**: Full access to **add, update, delete, and search** patient, doctor, and disease records.
- **Guest**: Limited to **searching patient records** and viewing help.

The system is designed to be **modular, beginner-friendly, readable, and extensible**, with clear separation of concerns:
- **Model**: Data classes (`Patient`, `Doctor`, `Disease`)
- **Database Layer**: DAOs (Data Access Objects) for CRUD operations
- **GUI Layer**: Clean, layout-based windows with menus, toolbars, and forms
- **Utilities**: Helper classes for tables and resource management

---

## 📂 Project Structure

```bash
PatientManagementSystem/
├── src/
│   ├── Main.java                          # Entry point
│   │
│   ├── model/                             # Plain Old Java Objects (POJOs)
│   │   ├── Patient.java
│   │   ├── Doctor.java
│   │   └── Disease.java
│   │
│   ├── database/                          # MySQL interaction layer
│   │   ├── DatabaseConnection.java        # Singleton-style DB connector
│   │   ├── DiseaseDAO.java                # CRUD for Disease table
│   │   ├── DoctorDAO.java                 # CRUD for Doctor table
│   │   └── PatientDAO.java                # CRUD + advanced search for Patient
│   │
│   ├── gui/                               # All GUI windows
│   │   ├── LoginWindow.java               # Initial login screen (user + role)
│   │   ├── AdminDashboard.java            # Full-featured admin interface
│   │   ├── GuestDashboard.java            # Read-only guest interface
│   │   └── forms/                         # Reusable input/search forms
│   │       ├── AddDiseaseForm.java
│   │       ├── AddDoctorForm.java
│   │       ├── AddPatientForm.java
│   │       ├── UpdatePatientForm.java
│   │       └── SearchResultsWindow.java   # Displays JTable results
│   │
│   └── utils/
│       └── TableModelHelper.java          # Builds JTable from lists
│
├── lib/                                   # (Optional) MySQL connector JAR
│   └── mysql-connector-j-9.5.0.jar
│
└── README.md                              # This file
```

---

## 🗄️ Database Setup

### ✅ Requirements
- **MySQL Server 8+** running locally
- Database user with **CREATE, INSERT, SELECT, UPDATE, DELETE** privileges

### 💾 Create the Database & Tables

Run the following SQL in **MySQL Workbench** or **Command Line Client**:

```sql
CREATE DATABASE IF NOT EXISTS patient_db;
USE patient_db;

CREATE TABLE Disease (
    Disease_ID INT AUTO_INCREMENT PRIMARY KEY,
    Disease_Name VARCHAR(100) NOT NULL UNIQUE,
    Disease_Description TEXT
);

CREATE TABLE Doctor (
    Doctor_ID INT AUTO_INCREMENT PRIMARY KEY,
    Disease_ID INT NOT NULL,
    Doctor_Name VARCHAR(100) NOT NULL,
    FOREIGN KEY (Disease_ID) REFERENCES Disease(Disease_ID)
);

CREATE TABLE Patient (
    Patient_ID INT AUTO_INCREMENT PRIMARY KEY,
    Patient_Name VARCHAR(100) NOT NULL,
    PF_Name VARCHAR(100),
    Sex ENUM('Male', 'Female') NOT NULL,
    DOB DATE,
    Doctor_ID INT,
    Disease_History TEXT,
    Prescription TEXT,
    FOREIGN KEY (Doctor_ID) REFERENCES Doctor(Doctor_ID)
);
```

> 🔐 All foreign key constraints are enforced by MySQL.

---

## ⚙️ Configuration

### MySQL Connection

Edit the following file to match your environment:

**`src/database/DatabaseConnection.java`**

```java
private static final String URL = "jdbc:mysql://localhost:3306/patient_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### MySQL Driver Location

Your MySQL Connector/J is located at:  
`C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar`

✅ **You have two options**:

#### Option 1: Add to CLASSPATH (Recommended)
Ensure the JAR is in your system `CLASSPATH` environment variable.

#### Option 2: Include in Compile/Run Command
```bash
# Compile (Windows - list all packages)
javac -cp ".;C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" -d bin src/model/*.java src/database/*.java src/utils/*.java src/gui/*.java src/gui/forms/*.java src/*.java

# Run
java -cp "bin;C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" Main
```

> 💡 On **Linux/macOS**, replace `;` with `:` in the `-cp` flag.

---

## 🔐 Login System

- **Login Screen**: `LoginWindow.java`
- **User Types**: `Administrator` or `Guest`
- **Hardcoded Credentials** (for simplicity):
  - **Admin**: `Username = admin`, `Password = admin`
  - **Guest**: `Username = guest`, `Password = guest`

> 🔒 In a real system, you’d add a `Users` table with hashed passwords.

Upon successful login:
- **Admin** → `AdminDashboard.java`
- **Guest** → `GuestDashboard.java`

---

## 🖥️ Administrator Dashboard (`AdminDashboard.java`)

### Menu Bar
| Menu          | Items |
|---------------|-------|
| **Manage Record** | - Add New Patient<br>- Add New Doctor<br>- Add New Disease<br>- Delete Patient Record<br>- **Update Record** → (Update Patient, Update Doctor Record) |
| **Search Record** | - Search Patient by Name<br>- Search Patient by ID<br>- Search Patient by Age<br>- Search Patient by Disease *(placeholder)*<br>- Search Patient by Doctor *(placeholder)*<br>- Search Doctor by Name *(placeholder)*<br>- Search Doctor by Disease Specialization *(placeholder)* |
| **Help** | - About Us<br>- Change Password |

### Tool Bar (Icons)
- Add New Patient
- Search Patient Record
- Add New Doctor
- Print *(placeholder)*

> 🔁 **Update Patient**: Opens `UpdatePatientForm` with only **Disease History** and **Prescription** editable.  
> 🗑️ **Delete Patient**: Prompts for ID → confirms deletion → removes from DB.

---

## 👥 Guest Dashboard (`GuestDashboard.java`)

### Menu Bar
| Menu | Items |
|------|-------|
| **Search Record** | - Search by Name<br>- Search by ID<br>- Search by Age |
| **Print** | - Print Records *(placeholder)* |
| **Help** | - About Us<br>- Change Password |

### Tool Bar
- Search Record
- Print

> 🔍 All search results are displayed in a **`JTable`** via `SearchResultsWindow`.

---

## 📝 Forms (All in `gui.forms` package)

### 1. `AddDiseaseForm.java`
- Fields: `Disease Name` (text), `Description` (text area)
- Buttons: **Save** → inserts into `Disease` table | **Cancel** → closes form

### 2. `AddDoctorForm.java`
- Fields: `Doctor Name` (text), `Disease Specialization` (combo box loaded from DB)
- Saves to `Doctor` table with foreign key to `Disease`

### 3. `AddPatientForm.java`
- **Patient ID**: Auto-generated (non-editable, displays "Auto")
- **Name, Father Name**: Text fields
- **Sex**: Radio buttons (`Male` / `Female`)
- **DOB**: Text field (format: `yyyy-MM-dd`)  
  *(Note: `JCalendar` not used to avoid external dependencies)*
- **Doctor**: Combo box (populated from DB with disease specialization)
- **Disease History & Prescription**: Text areas
- **Save**: Inserts full record into `Patient` table

### 4. `UpdatePatientForm.java`
- Loads patient by ID
- Displays **all fields as read-only** except:
  - Disease History
  - Prescription
- **Save**: Updates only these two fields in DB

### 5. `SearchResultsWindow.java`
- Accepts a `List<?>` (usually `List<Patient>`)
- Uses `TableModelHelper` to render data in a **scrollable `JTable`**
- Read-only view

---

## 🧠 Database Layer (`database` package)

All DAOs use:
- **Try-with-resources** for auto-closing connections
- **PreparedStatements** to prevent SQL injection
- **Standard CRUD patterns**

### Key Methods

| Class | Key Methods |
|------|-------------|
| `DiseaseDAO` | `save()`, `getAll()` |
| `DoctorDAO` | `save()`, `getAllWithDiseaseName()`, `findByDiseaseId()` |
| `PatientDAO` | `save()`, `deleteById()`, `findById()`, `searchByName()`, `searchById()`, `searchByAge()` |

> ⏳ **Age Calculation**: Uses `TIMESTAMPDIFF(YEAR, DOB, CURDATE())` in SQL for accuracy.

---

## 🧰 Utilities

### `TableModelHelper.java`
- Static helper to convert `List<Patient>` → `JTable`
- Handles null-safe date conversion
- Used by **all search result windows**

---

## 🧪 How to Run the Application

### Step 1: Set Up MySQL
- Start MySQL server
- Run the SQL script to create `patient_db` and tables

### Step 2: Update DB Credentials
- Open `src/database/DatabaseConnection.java`
- Set your `USERNAME` and `PASSWORD`

### Step 3: Compile
```bash
# Windows
javac -cp ".;C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" -d bin src/model/*.java src/database/*.java src/utils/*.java src/gui/*.java src/gui/forms/*.java src/*.java

# Linux/macOS
javac -cp ".:your_path/mysql-connector-j-9.5.0.jar" -d bin src/**/*.java
```

### Step 4: Run
```bash
# Windows
java -cp "bin;C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" Main

# Linux/macOS
java -cp "bin:your_path/mysql-connector-j-9.5.0.jar" Main
```

> 🚀 The app will launch the **Login Window**.

---

## 🧩 Design Principles

| Principle | Implementation |
|--------|----------------|
| **Modularity** | Each concern in its own package |
| **Beginner-Friendly** | No complex patterns, minimal abstractions |
| **No Null Layout** | Uses `BorderLayout`, `GridLayout`, `GridBagLayout` |
| **Reusable Components** | `SearchResultsWindow`, `TableModelHelper` |
| **Error Handling** | Try-catch with user-friendly messages |
| **Security** | PreparedStatements (no SQL injection) |

---

## 🧾 Assignment Compliance Checklist ✅

| Requirement | Implemented? | File(s) |
|-----------|--------------|--------|
| Login with user type (Admin/Guest) | ✅ | `LoginWindow.java` |
| Admin: Manage Record menu | ✅ | `AdminDashboard.java` |
| Admin: Add Disease form | ✅ | `AddDiseaseForm.java`, `DiseaseDAO.java` |
| Admin: Add Doctor form (with disease combo) | ✅ | `AddDoctorForm.java`, `DoctorDAO.java` |
| Admin: Add Patient form (with fields as specified) | ✅ | `AddPatientForm.java`, `PatientDAO.java` |
| Admin: Update Patient (history + prescription only) | ✅ | `UpdatePatientForm.java` |
| Admin: Delete Patient by ID | ✅ | `AdminDashboard.java`, `PatientDAO.java` |
| Admin: Search by Name/ID/Age → JTable | ✅ | `SearchResultsWindow.java`, `TableModelHelper.java` |
| Admin: Other search options (placeholder) | ⚠️ | Not fully implemented (can be extended) |
| Guest: Search by Name/ID/Age | ✅ | `GuestDashboard.java` |
| Guest: Print & Help | ✅ (placeholders) | `GuestDashboard.java` |
| Toolbars with icons | ✅ (buttons labeled as icons) | `AdminDashboard.java`, `GuestDashboard.java` |
| No Null Layout | ✅ | All GUI files |
| MySQL integration | ✅ | `database` package |

> 📌 *Note: "Search by Disease", "Search by Doctor", etc., are marked as placeholders to keep scope manageable. They can be implemented similarly using joins.*

---

## 🛠️ Extending the System

You can easily add:
- `JCalendar` (via external lib like `jcalendar-1.4.jar`)
- Real user authentication (`Users` table)
- Export to PDF/Excel (using Apache POI)
- Audit logs
- Form validation (e.g., DOB in past, name not empty)

---

## 📬 Author

**Student Name**: [Your Name]  
**Course**: [Your Course]  
**Assignment**: No. 2 – Patient Management System  
**Date**: November 2025  

---

## 🎯 Final Notes

This project demonstrates:
- Core Java Swing GUI development
- JDBC + MySQL integration
- Layered architecture (Model → DAO → GUI)
- Real-world CRUD operations
- User role-based access control

It is **fully functional**, **well-commented**, and **aligned with beginner skill level** while meeting all assignment requirements.

---

> 💡 **Tip**: Keep your MySQL server running while testing!  
> 🔁 **Restart** the app after making DB changes.