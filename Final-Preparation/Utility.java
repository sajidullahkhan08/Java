import java.util.*;
import Package.PackagingExample;

public class Utility {
    public static void main (String[] args) {

        PackagingExample example = new PackagingExample();
        example.main(args);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a number: ");
        int number = scanner.nextInt();

        if (number % 2 == 0) {
            System.out.println(number + " is even.");
        } else {
            System.out.println(number + " is odd.");
        }
    }    
}
