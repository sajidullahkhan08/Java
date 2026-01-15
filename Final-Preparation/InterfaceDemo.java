// Interface demonstration and implementation

interface Interface {

    void displayMessage(String message);
    void calculateSum(int a, int b);
}

public class InterfaceDemo implements Interface {

    @Override
    public void displayMessage(String message) {
        System.out.println("Message: " + message);
    }

    @Override
    public void calculateSum(int a, int b) {
        int sum = a + b;
        System.out.println("Sum: " + sum);
    }

    public static void main(String[] args) {
        InterfaceDemo demo = new InterfaceDemo();
        demo.displayMessage("Hello, World!");
        demo.calculateSum(5, 10);
    }
}