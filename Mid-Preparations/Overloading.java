class MathOperations {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}

public class Overloading {
    public static void main(String[] args) {
        MathOperations m = new MathOperations();
        System.out.println(m.add(2, 3));        // int version
        System.out.println(m.add(2.5, 3.7));    // double version
        System.out.println(m.add(2, 3, 4));     // three-argument version
    }
}
