package common;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp timestamp;
    private String type; // text, file
    private String fileName;
    private byte[] fileData;

    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.type = "text";
    }

    // For file messages
    public Message(int senderId, int receiverId, String fileName, byte[] fileData) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.fileName = fileName;
        this.fileData = fileData;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.type = "file";
    }

    // Getters and setters
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
}