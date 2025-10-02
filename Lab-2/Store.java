package store;
public class Store {
    private String name;
    private String location;

    public Store(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void open() {
        System.out.println(name + " is now open.");
    }

    public void close() {
        System.out.println(name + " is now closed.");
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
