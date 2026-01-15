import java.net.*;

public class Sender {
    public static void main(String[] args) throws Exception {

        DatagramSocket ds = new DatagramSocket();

        String msg = "Hello UDP";
        byte[] data = msg.getBytes();

        InetAddress ip = InetAddress.getByName("localhost");

        DatagramPacket dp =
            new DatagramPacket(data, data.length, ip, 6000);

        ds.send(dp);
        ds.close();
    }
}
