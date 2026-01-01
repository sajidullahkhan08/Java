package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 100; // Fixed size for simplicity
    public static ClientHandler[] clients = new ClientHandler[MAX_CLIENTS];
    public static int clientCount = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                // Add to array
                if (clientCount < MAX_CLIENTS) {
                    clients[clientCount] = clientHandler;
                    clientCount++;
                }
                // Start thread
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simple method to find client by user ID
    public static ClientHandler getClient(int userId) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getUserId() == userId) {
                return clients[i];
            }
        }
        return null;
    }

    // Remove client from array
    public static void removeClient(int userId) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getUserId() == userId) {
                clients[i] = null;
                // Shift array if needed (simple approach)
                break;
            }
        }
    }
}