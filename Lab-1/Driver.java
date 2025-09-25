import ACP.Lab.Student;

public class Driver {
    public static void main(String[] args) {
        Student s1 = new Student("Alice", "Bob", 20);
        Student s2 = new Student("Charlie", "David", 22);
        Student s3 = new Student("Eve", "Frank", 21);
        Student[] students = {s1, s2, s3};
        

        Student eldest = Student.findEldestStudent(students);
        if (eldest != null) {
            System.out.println("The eldest student is: " + eldest.getName() + ", Age: " + eldest.getAge());
        } else {
            System.out.println("No students found.");
        }

        for (int i = 0; i < students.length; i++) {
            System.out.println("Student: " + students[i].getName() + ", ID: " + students[i].getId());
        }
    }
}