package client;

import common.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

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
                        currentUser = (User) response.getData();
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
                    mainWindow.updateFriends((List<User>) response.getData());
                    break;
                case "getFriendRequests":
                    mainWindow.updateFriendRequests((List<User>) response.getData());
                    break;
                case "newMessage":
                    mainWindow.receiveMessage((Message) response.getData());
                    break;
                case "getMessages":
                    mainWindow.displayMessages((List<Message>) response.getData());
                    break;
                case "statusUpdate":
                    Object[] data = (Object[]) response.getData();
                    mainWindow.updateFriendStatus((Integer) data[0], (String) data[1]);
                    break;
                case "sendFriendRequest":
                    JOptionPane.showMessageDialog(mainWindow, response.getMessage());
                    break;
                case "updateProfile":
                    JOptionPane.showMessageDialog(mainWindow, response.getMessage());
                    break;
                case "newFriendRequest":
                    mainWindow.loadFriendRequests();
                    break;
                case "refreshFriends":
                    mainWindow.loadFriends();
                    break;
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }
}