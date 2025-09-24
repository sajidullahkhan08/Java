package ACP.Lab;

public class Student {
    private String name;
    private String father_name;
    private int id;
    private int age;
    private static int counter = 0;

    public Student(String name, String father_name, int age) {
        this.name = name;
        this.father_name = father_name;
        this.age = age;
        this.id = counter++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return father_name;
    }

    public void setFatherName(String father_name) {
        this.father_name = father_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Student findEldestStudent(Student[] students) {
        if (students == null || students.length == 0) {
            return null;
        }
        Student eldest = students[0];
        for (Student student : students) {
            if (student.getAge() > eldest.getAge()) {
                eldest = student;
            }
        }
        return eldest;
    }
}
