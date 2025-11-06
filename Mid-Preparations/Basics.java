// Practicing "continue" and "break statements" in Java

public class Basics {
    public static void main(String[] args) {
        // Using "continue" statement
        System.out.println("Using continue statement:");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // Skip even numbers
            }
            System.out.println(i); // Print odd numbers
        }

        // Using "break" statement
        System.out.println("\nUsing break statement:");
        for (int i = 1; i <= 10; i++) {
            if (i > 5) {
                break; // Exit loop when i is greater than 5
            }
            System.out.println(i); // Print numbers from 1 to 5
        }
    }
}