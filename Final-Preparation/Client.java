import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 5000);

        BufferedReader br = new BufferedReader(
            new InputStreamReader(s.getInputStream())
        );

        PrintWriter pw = new PrintWriter(
            s.getOutputStream(), true
        );

        pw.println("Hello Server");

        System.out.println("Server says: " + br.readLine());

        s.close();
    }
}
