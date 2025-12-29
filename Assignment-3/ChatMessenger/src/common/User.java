package common;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String status; // online, offline
    private byte[] profilePic;
    private String statusMessage;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.status = "offline";
        this.statusMessage = "Available";
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public byte[] getProfilePic() { return profilePic; }
    public void setProfilePic(byte[] profilePic) { this.profilePic = profilePic; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    @Override
    public String toString() {
        return username + " (" + status + ")";
    }
}