public class MainQ3 {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("Add int: " + calc.add(1, 2));
        System.out.println("Add int int int: " + calc.add(1, 2, 3));
        System.out.println("Add double: " + calc.add(1.5, 2.5));

        Employee emp1 = new Manager();
        Employee emp2 = new Developer();

        emp1.showRole(); // Manages team
        emp2.showRole(); // Writes code
    }
}
