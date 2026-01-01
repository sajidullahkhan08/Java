package common;

import java.io.Serializable;

public class Request implements Serializable {
    private String type; // "login", "signup", "sendMessage", etc.
    private String username; // For login/signup
    private String password; // For login/signup
    private Message message; // For sending messages/files
    private String targetUsername; // For friend requests
    private int friendRequestId; // For accepting requests
    private int targetUserId; // For getting messages with specific user
    private User user; // For updating profile

    // Default constructor
    public Request() {}

    // Constructor for simple requests
    public Request(String type) {
        this.type = type;
    }

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }

    public String getTargetUsername() { return targetUsername; }
    public void setTargetUsername(String targetUsername) { this.targetUsername = targetUsername; }

    public int getFriendRequestId() { return friendRequestId; }
    public void setFriendRequestId(int friendRequestId) { this.friendRequestId = friendRequestId; }

    public int getTargetUserId() { return targetUserId; }
    public void setTargetUserId(int targetUserId) { this.targetUserId = targetUserId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}