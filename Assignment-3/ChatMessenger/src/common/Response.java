package common;

import java.io.Serializable;

public class Response implements Serializable {
    private String type;
    private boolean success;
    private String message;
    private User user;
    private User[] friends;  // Array instead of List<User>
    private Message[] messages;  // Array instead of List<Message>
    private User[] friendRequests;  // Array for friend requests

    // Default constructor
    public Response() {
        this.friends = new User[0];
        this.messages = new Message[0];
        this.friendRequests = new User[0];
    }

    // Constructor for simple responses
    public Response(String type, boolean success, String message) {
        this();
        this.type = type;
        this.success = success;
        this.message = message;
    }

    // Constructor with user
    public Response(String type, boolean success, String message, User user) {
        this(type, success, message);
        this.user = user;
    }

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User[] getFriends() { return friends; }
    public void setFriends(User[] friends) { this.friends = friends; }

    public Message[] getMessages() { return messages; }
    public void setMessages(Message[] messages) { this.messages = messages; }

    public User[] getFriendRequests() { return friendRequests; }
    public void setFriendRequests(User[] friendRequests) { this.friendRequests = friendRequests; }
}