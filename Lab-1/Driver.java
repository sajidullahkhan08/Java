import ACP.Lab.Student;

public class Driver {
    public static void main(String[] args) {
        Student[] students = new Student[3]; 
        students[0] = new Student("Alice", "Bob", 20);
        students[1] = new Student("Charlie", "David", 22);
        students[2] = new Student("Eve", "Frank", 21);

        Student eldest = Student.findEldestStudent(students);
        if (eldest != null) {
            System.out.println("The eldest student is: " + eldest.getName() + ", Age: " + eldest.getAge());
        } else {
            System.out.println("No students found.");
        }
    }
}