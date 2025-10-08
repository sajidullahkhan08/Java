class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("Cat eats fish.");
    }

    @Override
    public void sleep() {
        System.out.println("Cat sleeps on couch.");
    }
}
