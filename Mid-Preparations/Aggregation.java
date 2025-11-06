class Department {
    String name;

    Department(String name) {
        this.name = name;
    }
}

class Teacher {
    String name;
    Department department;  // Aggregation

    Teacher(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    void display() {
        System.out.println(name + " works in " + department.name + " department");
    }
}

public class Aggregation {
    public static void main(String[] args) {
        Department d1 = new Department("Computer Science");
        Teacher t1 = new Teacher("Sajid", d1);
        t1.display();
    }
}
