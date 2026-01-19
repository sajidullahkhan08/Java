public class Factorial {

    public static int f(int a) {
        if (a <= 1) {
            return 1;
        }
        else {
            return a * f(a - 1);
        }
    }

    public static void main (String[] args) {
        System.out.println("The factorial of the given number is: " + f(8));
    }
}