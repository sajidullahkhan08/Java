import store.grocery.GroceryStore;
import store.music.MusicStore;

public class Driver {
    public static void main(String[] args) {
        GroceryStore gs = new GroceryStore("SuperMart", "Downtown", 10);
        gs.open();
        gs.sellGroceries();
        gs.close();

        MusicStore ms = new MusicStore("TuneTown", "Uptown", 50);
        ms.open();
        ms.sellInstruments();
        ms.close();
    }
}
ah farag insaan hai
wzwswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswswsz3ws