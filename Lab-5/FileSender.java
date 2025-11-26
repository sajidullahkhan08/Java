import javax.swing.*; // for GUI components
import java.awt.*; // for layout managers
import java.io.*; // for file and stream handling
import java.net.*; // for networking

public class FileSender extends JFrame { // extends JFrame because it's a GUI application 

    private JTextField ipField; // Field to enter receiver IP
    private JButton chooseBtn, sendBtn; // Buttons for choosing file and sending file
    private JTextArea status; // Area to display status messages
    private File selectedFile; // The file selected to send

    // Constructor to set up the GUI, initialize components and layout, add action listeners, and make the frame visible
    public FileSender() {
        setTitle("File Sender"); // Set window title
        setSize(400, 300); // Set window size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit application on close
        setLayout(new BorderLayout()); // Use BorderLayout for layout

        JPanel topPanel = new JPanel(); // Panel for IP input
        topPanel.add(new JLabel("Receiver IP:")); // Label for IP field
        ipField = new JTextField(15); // Text field for entering receiver IP
        topPanel.add(ipField); // Add IP field to top panel
        add(topPanel, BorderLayout.NORTH); // Add top panel to the north region of the frame

        status = new JTextArea(); // Text area for status messages
        status.setEditable(false); // Make status area non-editable
        add(new JScrollPane(status), BorderLayout.CENTER); // Add scrollable status area to center region

        JPanel bottomPanel = new JPanel(); // Panel for buttons
        chooseBtn = new JButton("Choose File"); // Button to choose file
        chooseBtn.addActionListener(e -> chooseFile()); // Add action listener to choose file button 
        bottomPanel.add(chooseBtn); // Add choose button to bottom panel

        sendBtn = new JButton("Send File"); // Button to send file
        sendBtn.addActionListener(e -> sendFile()); // Add action listener to send file button
        bottomPanel.add(sendBtn); // Add send button to bottom panel

        add(bottomPanel, BorderLayout.SOUTH); // Add bottom panel to the south region of the frame

        setVisible(true); // Make the frame visible
    }

    // Method to choose a file using JFileChooser
    private void chooseFile() {
        JFileChooser fc = new JFileChooser(); // Create file chooser dialog
        // Show open dialog and check if a file was selected
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile(); // Get the selected file
            status.append("Selected: " + selectedFile.getName() + "\n"); // Update status area with selected file name
        }
    }

    // Method to send the selected file to the specified receiver IP
    private void sendFile() {
        if (selectedFile == null) {
            status.append("Please choose a file first.\n");
            return;
        }

        String ip = ipField.getText(); // Get the receiver IP from the text field
        if (ip.isEmpty()) {
            status.append("Enter receiver IP.\n");
            return;
        }

        // Start a new thread to handle file sending to avoid blocking the UI
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, 5000); // Connect to receiver on port 5000
                status.append("Connected to receiver...\n");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // Output stream to send data

                // Send metadata
                dos.writeUTF(selectedFile.getName()); // Send file name
                dos.writeLong(selectedFile.length()); // Send file size

                // Send file content
                FileInputStream fis = new FileInputStream(selectedFile); // Input stream to read file
                byte[] buffer = new byte[4096]; // Buffer for file data
                int bytes; // Number of bytes read

                // Read from file and write to output stream
                while ((bytes = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytes); // Write bytes to output stream
                }

                fis.close();
                dos.close();
                socket.close();

                status.append("File sent successfully!\n");

            } catch (Exception ex) {
                status.append("Error: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        new FileSender(); // Create and show the FileSender GUI
    }
}
