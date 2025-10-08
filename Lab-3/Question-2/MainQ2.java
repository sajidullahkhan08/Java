public class MainQ2 {
    public static void main(String[] args) {
        Animal[] animals = new Animal[2];
        animals[0] = new Dog();
        animals[1] = new Cat();

        for (Animal animal : animals) {
            animal.eat();
            animal.sleep();
        }

        // Extension: Demonstrate Pet
        if (animals[0] instanceof Pet) {
            ((Pet) animals[0]).play();
        }
    }
}
