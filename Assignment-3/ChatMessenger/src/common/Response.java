package common;

import java.io.Serializable;

public class Response implements Serializable {
    private String type, message;
    private boolean success;
    private User user;
    private User[] friends;
    private Message[] messages;
    private User[] friendRequests;

    //D constructor
    public Response() {
        this.friends= new User[0];
        this.messages= new Message[0];
        this.friendRequests = new User[0];
    }
    //for simple responses
    public Response(String type, boolean success, String message) {
        this();
        this.type = type;
        this.success = success;
        this.message = message;
    }
    //with user
    public Response(String type, boolean success, String message, User user) {
        this(type, success, message);
        this.user = user;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isSuccess() 
    { return success; }
    public void setSuccess(boolean success) 
    { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public User[] getFriends() { return friends; }
    public void setFriends(User[] friends) { this.friends = friends; }
    public Message[] getMessages() { 
        return messages; 
    }
    public void setMessages(Message[] messages) { this.messages = messages; }
    public User[] getFriendRequests() { return friendRequests; }
    public void setFriendRequests(User[] friendRequests) { this.friendRequests = friendRequests; }
}