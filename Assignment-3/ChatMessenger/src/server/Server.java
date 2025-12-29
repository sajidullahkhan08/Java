package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<Integer, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addClient(int userId, ClientHandler handler) {
        clients.put(userId, handler);
    }

    public static void removeClient(int userId) {
        clients.remove(userId);
    }

    public static ClientHandler getClient(int userId) {
        return clients.get(userId);
    }

    public static ConcurrentHashMap<Integer, ClientHandler> getClients() {
        return clients;
    }
}