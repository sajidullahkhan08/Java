class Engine {
    void start() {
        System.out.println("Engine started.");
    }
}

class Car {
    private Engine engine;  // Composition

    Car() {
        engine = new Engine(); // created inside
    }

    void drive() {
        engine.start();
        System.out.println("Car is moving...");
    }
}

public class Composition {
    public static void main(String[] args) {
        Car c = new Car();
        c.drive();
    }
}
