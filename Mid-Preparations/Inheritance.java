class Parent {
    void showParent() {
        System.out.println("This is the Parent class");
    }
}

class Child extends Parent {
    void showChild() {
        System.out.println("This is the Child class");
    }
}

public class Inheritance {
    public static void main(String[] args) {
        Child c = new Child();
        c.showParent(); // inherited method
        c.showChild();
    }
}
