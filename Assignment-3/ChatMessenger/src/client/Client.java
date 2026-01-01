package client;

import common.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User currentUser;
    private LoginWindow loginWindow;
    private MainWindow mainWindow;

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            loginWindow = new LoginWindow(this);
            loginWindow.setVisible(true);

            // Start listening for responses
            new Thread(this::listenForResponses).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server");
        }
    }

    public void sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForResponses() {
        try {
            while (true) {
                Response response = (Response) in.readObject();
                handleResponse(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected from server");
        }
    }

    private void handleResponse(Response response) {
        SwingUtilities.invokeLater(() -> {
            switch (response.getType()) {
                case "login":
                    if (response.isSuccess()) {
                        currentUser = response.getUser();
                        loginWindow.dispose();
                        mainWindow = new MainWindow(this, currentUser);
                        mainWindow.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(loginWindow, response.getMessage());
                    }
                    break;
                case "signup":
                    JOptionPane.showMessageDialog(loginWindow, response.getMessage());
                    break;
                case "getFriends":
                    if (mainWindow != null) {
                        mainWindow.updateFriends(response.getFriends());
                    }
                    break;
                case "getFriendRequests":
                    if (mainWindow != null) {
                        mainWindow.updateFriendRequests(response.getFriendRequests());
                    }
                    break;
                case "newMessage":
                    if (mainWindow != null && response.getMessages() != null && response.getMessages().length > 0) {
                        mainWindow.receiveMessage(response.getMessages()[0]);
                    }
                    break;
                case "getMessages":
                    if (mainWindow != null) {
                        mainWindow.displayMessages(response.getMessages());
                    }
                    break;
                case "statusUpdate":
                    // Simplified status update
                    if (mainWindow != null) {
                        mainWindow.loadFriends(); // Refresh friends list
                    }
                    break;
                case "sendFriendRequest":
                    if (mainWindow != null) {
                        JOptionPane.showMessageDialog(mainWindow, response.getMessage());
                    }
                    break;
                case "updateProfile":
                    if (mainWindow != null) {
                        JOptionPane.showMessageDialog(mainWindow, response.getMessage());
                    }
                    break;
                case "newFriendRequest":
                    if (mainWindow != null) {
                        mainWindow.loadFriendRequests();
                    }
                    break;
                case "refreshFriends":
                    if (mainWindow != null) {
                        mainWindow.loadFriends();
                    }
                    break;
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }
}