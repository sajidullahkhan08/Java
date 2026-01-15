import java.net.*;

public class Receiver {
    public static void main(String[] args) throws Exception {

        DatagramSocket ds = new DatagramSocket(6000);

        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

        ds.receive(dp);

        String msg = new String(dp.getData(), 0, dp.getLength());
        System.out.println("Received: " + msg);

        ds.close();
    }
}
