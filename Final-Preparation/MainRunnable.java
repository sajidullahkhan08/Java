import java.lang.Runnable;
import java.lang.Thread;

class MyRunnable implements Runnable {
    public void run() {

        System.out.println("Thread 2 is running...");
        for (int i = 0; i < 5; i++) {
            System.out.println("Count: " + i);
            try {
                Thread.sleep(500); // Sleep for 500 milliseconds
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("Thread 2 has finished execution."); 
    }
}

public class MainRunnable {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread t2 = new Thread(myRunnable);
        t2.start(); // starts a new thread and calls run()
    }
}