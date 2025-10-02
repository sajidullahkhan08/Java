package store.grocery;

import store.Store;

public class GroceryStore extends Store {
    private int numberOfAisles;

    public GroceryStore(String name, String location, int numberOfAisles) {
        super(name, location);
        this.numberOfAisles = numberOfAisles;
    }

    public void sellGroceries() {
        System.out.println("Selling groceries at " + getName());
    }

    public int getNumberOfAisles() {
        return numberOfAisles;
    }
}
