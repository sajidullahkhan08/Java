import java.lang.Thread;

class MyThread extends Thread {
    public void run() {

        System.out.println("Thread 1 is running...");
        for (int i = 0; i < 5; i++) {
            System.out.println("Count: " + i);
            try {
                Thread.sleep(500); // Sleep for 500 milliseconds
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.println("Thread 1 has finished execution."); 
    }
}

public class MainThread {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start(); // starts a new thread and calls run()
    }
}
