import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server waiting...");

        Socket s = ss.accept();  // Blocking call

        BufferedReader br = new BufferedReader(
            new InputStreamReader(s.getInputStream())
        );

        PrintWriter pw = new PrintWriter(
            s.getOutputStream(), true
        );

        String msg = br.readLine();
        System.out.println("Client says: " + msg);

        pw.println("Hello Client");

        s.close();
        ss.close();
    }
}
