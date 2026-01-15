package common;
import java.io.Serializable;

public class Request implements Serializable {
    private String type, username, password, targetUsername;
    private Message message;
    private int friendRequestId, targetUserId;
    private User user;

    public Request() {}

    public Request(String type) {
        this.type = type;
    }
    public String getType() { return type; }
    public void setType(String type) 
    { this.type = type; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }
    public String getTargetUsername() { return targetUsername; }
    public void setTargetUsername(String targetUsername) 
    { this.targetUsername = targetUsername; }
    public int getFriendRequestId() { return friendRequestId; }
    public void setFriendRequestId(int friendRequestId) { this.friendRequestId = friendRequestId; }
    public int getTargetUserId() { return targetUserId; }
    public void setTargetUserId(int targetUserId) { this.targetUserId = targetUserId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}