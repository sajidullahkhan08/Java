import java.util.Scanner;

public class MainQ1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter details for Circle:");
        System.out.print("Color: ");
        String circleColor = scanner.nextLine();
        System.out.print("Radius: ");
        double radius = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        Circle circle = new Circle(circleColor, radius);

        System.out.println("Enter details for Rectangle:");
        System.out.print("Color: ");
        String rectColor = scanner.nextLine();
        System.out.print("Length: ");
        double length = scanner.nextDouble();
        System.out.print("Width: ");
        double width = scanner.nextDouble();

        Rectangle rectangle = new Rectangle(rectColor, length, width);

        System.out.println("\nShape Details:");
        System.out.println("Circle - Color: " + circle.getColor() + ", Area: " + circle.area());
        System.out.println("Rectangle - Color: " + rectangle.getColor() + ", Area: " + rectangle.area());

        scanner.close();
    }
}
