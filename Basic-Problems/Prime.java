import javax.swing.JOptionPane;
import java.util.*;

public class Prime {
    public static void main(String[] args) {
        int a;
        a = 27;
        boolean flag = true;

        if (a <= 1) {
            flag = false;
        }

        for (int i = 2; i < a; i++) {
            if (a % i == 0) {
                flag = false;
            }
        }

        if (flag == true) {
            System.out.println("Its a prime number...");
        }
        else {
            System.out.println("Its not a prime number");
        }

       
    }
}