package server;

import common.*;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User user;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = (Request) in.readObject();
                handleRequest(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected");
            if (user != null) {
                Server.removeClient(user.getId());
                broadcastStatusUpdate(user.getId(), "offline");
            }
        }
    }

    private void handleRequest(Request request) {
        String type = request.getType();
        Object data = request.getData();
        Response response = null;

        switch (type) {
            case "login":
                String[] loginData = (String[]) data;
                user = DatabaseHelper.authenticate(loginData[0], loginData[1]);
                if (user != null) {
                    user.setStatus("online");
                    Server.addClient(user.getId(), this);
                    broadcastStatusUpdate(user.getId(), "online");
                    response = new Response("login", user, true, "Login successful");
                } else {
                    response = new Response("login", null, false, "Invalid credentials");
                }
                break;
            case "signup":
                String[] signupData = (String[]) data;
                boolean success = DatabaseHelper.register(signupData[0], signupData[1]);
                if (success) {
                    response = new Response("signup", null, true, "Signup successful");
                } else {
                    response = new Response("signup", null, false, "Username already exists");
                }
                break;
            case "getFriends":
                List<User> friends = DatabaseHelper.getFriends(user.getId());
                for (User friend : friends) {
                    friend.setStatus(Server.getClient(friend.getId()) != null ? "online" : "offline");
                }
                response = new Response("getFriends", friends, true, "");
                break;
            case "sendFriendRequest":
                int friendId;
                if (data instanceof String) {
                    friendId = DatabaseHelper.getUserIdByUsername((String) data);
                    if (friendId == -1) {
                        response = new Response("sendFriendRequest", null, false, "User not found");
                        break;
                    }
                } else {
                    friendId = (Integer) data;
                }
                DatabaseHelper.sendFriendRequest(user.getId(), friendId);
                // Notify receiver if online
                ClientHandler friendHandler = Server.getClient(friendId);
                if (friendHandler != null) {
                    friendHandler.sendResponse(new Response("newFriendRequest", null, true, ""));
                }
                response = new Response("sendFriendRequest", null, true, "Request sent");
                break;
            case "getFriendRequests":
                List<User> requests = DatabaseHelper.getFriendRequests(user.getId());
                response = new Response("getFriendRequests", requests, true, "");
                break;
            case "acceptFriendRequest":
                int requesterId = (Integer) data;
                DatabaseHelper.acceptFriendRequest(user.getId(), requesterId);
                // Notify both users to refresh friends
                ClientHandler accepterHandler = this;
                ClientHandler requesterHandler = Server.getClient(requesterId);
                if (accepterHandler != null) {
                    accepterHandler.sendResponse(new Response("refreshFriends", null, true, ""));
                }
                if (requesterHandler != null) {
                    requesterHandler.sendResponse(new Response("refreshFriends", null, true, ""));
                }
                response = new Response("acceptFriendRequest", null, true, "Friend added");
                break;
            case "sendMessage":
                Message message = (Message) data;
                DatabaseHelper.saveMessage(message);
                ClientHandler receiverHandler = Server.getClient(message.getReceiverId());
                if (receiverHandler != null) {
                    receiverHandler.sendResponse(new Response("newMessage", message, true, ""));
                }
                response = new Response("sendMessage", null, true, "Message sent");
                break;
            case "getMessages":
                int friendId2 = (Integer) data;
                List<Message> messages = DatabaseHelper.getMessages(user.getId(), friendId2);
                response = new Response("getMessages", messages, true, "");
                break;
            case "updateProfile":
                User updatedUser = (User) data;
                DatabaseHelper.saveProfile(updatedUser);
                response = new Response("updateProfile", null, true, "Profile updated");
                break;
            default:
                response = new Response("error", null, false, "Unknown request");
        }

        sendResponse(response);
    }

    private void sendResponse(Response response) {
        try {
            out.writeObject(response);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastStatusUpdate(int userId, String status) {
        for (ClientHandler handler : Server.getClients().values()) {
            if (handler != this) {
                handler.sendResponse(new Response("statusUpdate", new Object[]{userId, status}, true, ""));
            }
        }
    }
}