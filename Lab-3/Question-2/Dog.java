class Dog implements Animal, Pet {
    @Override
    public void eat() {
        System.out.println("Dog eats bones.");
    }

    @Override
    public void sleep() {
        System.out.println("Dog sleeps in kennel.");
    }

    @Override
    public void play() {
        System.out.println("Dog plays fetch.");
    }
}
