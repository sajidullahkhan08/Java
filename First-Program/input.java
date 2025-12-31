import java.util.Scanner; // Explaining the import statement: This line imports the Scanner class from the java.util package, which is used to take input from the user.
// class input {
//     public static void main(String[] args) {
//         // taking input from user
//         Scanner sc = new Scanner(System.in); // Creating a Scanner object to read input
//         System.out.print("Enter your name: "); // Prompting the user to enter their name
//         String name = sc.nextLine(); // Reading a line of text from the user
//         System.out.println("Hello, " + name + "!");
//         sc.close(); // Closing the scanner to prevent resource leaks
//     }
// }

class Input {
    public static void main(String[] args) {
        Scanner object = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age = object.nextInt();
        System.out.println("Your age is: " + age);
        object.close();
    }
}