package server;

import common.*;

import java.io.*;
import java.net.Socket;

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
        Response response = null;

        switch (type) {
            case "login":
                user = DatabaseHelper.authenticate(request.getUsername(), request.getPassword());
                if (user != null) {
                    user.setStatus("online");
                    // Note: Server.addClient is removed, using array in Server
                    broadcastStatusUpdate(user.getId(), "online");
                    response = new Response("login", true, "Login successful");
                    response.setUser(user);
                } else {
                    response = new Response("login", false, "Invalid credentials");
                }
                break;
            case "signup":
                boolean success = DatabaseHelper.register(request.getUsername(), request.getPassword());
                if (success) {
                    response = new Response("signup", true, "Signup successful");
                } else {
                    response = new Response("signup", false, "Username already exists");
                }
                break;
            case "getFriends":
                User[] friends = DatabaseHelper.getFriends(user.getId());
                // Set online status
                for (int i = 0; i < friends.length; i++) {
                    User friend = friends[i];
                    friend.setStatus(Server.getClient(friend.getId()) != null ? "online" : "offline");
                }
                response = new Response("getFriends", true, "");
                response.setFriends(friends);
                break;
            case "sendFriendRequest":
                int friendId = DatabaseHelper.getUserIdByUsername(request.getTargetUsername());
                if (friendId == -1) {
                    response = new Response("sendFriendRequest", false, "User not found");
                    break;
                }
                DatabaseHelper.sendFriendRequest(user.getId(), friendId);
                // Notify receiver if online
                ClientHandler friendHandler = Server.getClient(friendId);
                if (friendHandler != null) {
                    friendHandler.sendResponse(new Response("newFriendRequest", true, ""));
                }
                response = new Response("sendFriendRequest", true, "Request sent");
                break;
            case "getFriendRequests":
                User[] requests = DatabaseHelper.getFriendRequests(user.getId());
                response = new Response("getFriendRequests", true, "");
                response.setFriendRequests(requests);
                break;
            case "acceptFriendRequest":
                DatabaseHelper.acceptFriendRequest(user.getId(), request.getFriendRequestId());
                // Notify both users to refresh friends
                ClientHandler accepterHandler = this;
                ClientHandler requesterHandler = Server.getClient(request.getFriendRequestId());
                if (accepterHandler != null) {
                    accepterHandler.sendResponse(new Response("refreshFriends", true, ""));
                }
                if (requesterHandler != null) {
                    requesterHandler.sendResponse(new Response("refreshFriends", true, ""));
                }
                response = new Response("acceptFriendRequest", true, "Friend added");
                break;
            case "sendMessage":
                DatabaseHelper.saveMessage(request.getMessage());
                ClientHandler receiverHandler = Server.getClient(request.getMessage().getReceiverId());
                if (receiverHandler != null) {
                    Response newMsgResponse = new Response("newMessage", true, "");
                    newMsgResponse.setMessages(new Message[]{request.getMessage()});
                    receiverHandler.sendResponse(newMsgResponse);
                }
                response = new Response("sendMessage", true, "Message sent");
                break;
            case "getMessages":
                int friendId2 = DatabaseHelper.getUserIdByUsername(request.getTargetUsername());
                Message[] messages = DatabaseHelper.getMessages(user.getId(), friendId2);
                response = new Response("getMessages", true, "");
                response.setMessages(messages);
                break;
            case "updateProfile":
                DatabaseHelper.saveProfile(request.getUser());
                response = new Response("updateProfile", true, "Profile updated");
                break;
            default:
                response = new Response("error", false, "Unknown request");
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
        // Simplified broadcast using array
        for (int i = 0; i < Server.clientCount; i++) {
            ClientHandler handler = Server.clients[i];
            if (handler != null && handler != this) {
                Response statusResponse = new Response("statusUpdate", true, "");
                // Note: Simplified, assuming status is passed somehow
                handler.sendResponse(statusResponse);
            }
        }
    }

    // Add method for Server to get user ID
    public int getUserId() {
        return user != null ? user.getId() : -1;
    }
}