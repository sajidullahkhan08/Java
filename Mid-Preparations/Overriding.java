class Animal {
    void sound() {
        System.out.println("Animal makes a sound");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Dog barks");
    }
}

class Cat extends Animal {
    void sound() {
        System.out.println("Cat meows");
    }
}

public class Overriding {
    public static void main(String[] args) {
        Animal a1 = new Dog(); // upcasting
        Animal a2 = new Cat();
        a1.sound();  // calls Dog’s version
        a2.sound();  // calls Cat’s version
    }
}
