package store.music;

import store.Store;

public class MusicStore extends Store {
    private int numberOfInstruments;

    public MusicStore(String name, String location, int numberOfInstruments) {
        super(name, location);
        this.numberOfInstruments = numberOfInstruments;
    }

    public void sellInstruments() {
        System.out.println("Selling instruments at " + getName());
    }

    public int getNumberOfInstruments() {
        return numberOfInstruments;
    }
}
