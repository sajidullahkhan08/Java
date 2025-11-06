// Using teacher - student example to demonstrate association in Java

class Teacher {
    String name;

    Teacher(String name) {
        this.name = name;
    }

    void teach() {
        System.out.println(name + " is teaching.");
    }
}
class Student {
    String name;

    Student(String name) {
        this.name = name;
    }

    void attendClass(Teacher teacher) {  // Association
        System.out.println(name + " is attending class taught by " + teacher.name);
        teacher.teach();
    }
}
public class Association {
    public static void main(String[] args) {
        Teacher t1 = new Teacher("Sajid");
        Student s1 = new Student("Aisha");

        s1.attendClass(t1);  // Student attends class taught by Teacher
    }
}