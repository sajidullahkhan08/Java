import javax.swing.*; // for GUI components
import java.awt.*; // for layout managers
import java.awt.event.*; // for event handling
import java.io.*; // for file and stream handling
import java.net.*; // for networking

// FileReceiver class to receive files over the network
public class FileReceiver extends JFrame {

    private JTextArea status; // Area to display status messages
    private JButton startBtn; // Button to start the receiver


    // Constructor to set up the GUI and initialize components
    public FileReceiver() {
        setTitle("File Receiver");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Use BorderLayout for layout

        status = new JTextArea(); // Text area for status messages
        status.setEditable(false); // Make status area non-editable
        add(new JScrollPane(status), BorderLayout.CENTER); // Add scrollable status area to center region

        startBtn = new JButton("Start Receiver (Port 5000)"); // Button to start receiver
        startBtn.addActionListener(e -> startReceiver()); // Add action listener to start button and start receiver on click
        add(startBtn, BorderLayout.SOUTH); // Add start button to the south region of the frame

        setVisible(true); // Make the frame visible
    }

    // Method to start the file receiver
    private void startReceiver() {
        startBtn.setEnabled(false); // Disable start button after starting

        // Start a new thread to handle file receiving to avoid blocking the UI
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(5000); // Listen on port 5000
                status.append("Receiver started. Waiting for sender...\n"); // Update status area

                Socket socket = server.accept(); // Accept incoming connection
                status.append("Connected to sender.\n"); // Update status area

                DataInputStream dis = new DataInputStream(socket.getInputStream()); // Input stream to receive data

                String fileName = dis.readUTF(); // Read file name from input stream
                long fileSize = dis.readLong(); // Read file size from input stream

                FileOutputStream fos = new FileOutputStream("received_" + fileName); // Output stream to save received file
                byte[] buffer = new byte[4096]; // Buffer for file data

                long read = 0; // Bytes read so far
                int bytes; // Number of bytes read in each iteration

                // Read from input stream and write to file
                while (read < fileSize && (bytes = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytes); // Write bytes to file
                    read += bytes; // Update total bytes read
                }

                fos.close();
                dis.close();
                socket.close();
                server.close();

                status.append("File received: received_" + fileName + "\n");

            } catch (Exception ex) {
                status.append("Error: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        new FileReceiver(); // Create and show the FileReceiver GUI
    }
}
