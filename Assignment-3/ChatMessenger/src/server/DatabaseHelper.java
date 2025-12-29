package server;

import common.User;
import common.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseHelper {
    private static Connection connection;

    static {
        try {
            // Load .env file
            Properties props = new Properties();
            File envFile = new File(".env");
            InputStream input = new FileInputStream(envFile);
            props.load(input);
            input.close();
            String user = props.getProperty("DB_USER", "root");
            String pass = props.getProperty("DB_PASS", "");

            // Connect to MySQL
            String url = "jdbc:mysql://localhost:3306/chat_messenger";
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User authenticate(String username, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                User user = new User(id, username);
                loadProfile(user);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean register(String username, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadProfile(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT profile_pic, status_message FROM profiles WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setProfilePic(rs.getBytes("profile_pic"));
                user.setStatusMessage(rs.getString("status_message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveProfile(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO profiles (user_id, profile_pic, status_message) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE profile_pic = ?, status_message = ?");
            stmt.setInt(1, user.getId());
            stmt.setBytes(2, user.getProfilePic());
            stmt.setString(3, user.getStatusMessage());
            stmt.setBytes(4, user.getProfilePic());
            stmt.setString(5, user.getStatusMessage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getFriends(int userId) {
        List<User> friends = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT u.id, u.username FROM users u JOIN friends f ON (f.friend_id = u.id OR f.user_id = u.id) WHERE (f.user_id = ? OR f.friend_id = ?) AND f.status = 'accepted' AND u.id != ?");
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User friend = new User(rs.getInt("id"), rs.getString("username"));
                loadProfile(friend);
                friends.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static void sendFriendRequest(int userId, int friendId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT IGNORE INTO friends (user_id, friend_id) VALUES (?, ?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getFriendRequests(int userId) {
        List<User> requests = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT u.id, u.username FROM users u JOIN friends f ON f.user_id = u.id WHERE f.friend_id = ? AND f.status = 'pending'");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"));
                requests.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static void acceptFriendRequest(int userId, int friendId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_id = ?");
            stmt.setInt(1, friendId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveMessage(Message message) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages (sender_id, receiver_id, message, file_name, file_type, file_data) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, message.getSenderId());
            stmt.setInt(2, message.getReceiverId());
            stmt.setString(3, message.getContent());
            stmt.setString(4, message.getFileName());
            stmt.setString(5, message.getType());
            stmt.setBytes(6, message.getFileData());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getUserIdByUsername(String username) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Message> getMessages(int userId, int friendId) {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp");
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("file_type");
                Message msg;
                if ("file".equals(type)) {
                    msg = new Message(rs.getInt("sender_id"), rs.getInt("receiver_id"), rs.getString("file_name"), rs.getBytes("file_data"));
                } else {
                    msg = new Message(rs.getInt("sender_id"), rs.getInt("receiver_id"), rs.getString("message"));
                }
                msg.setTimestamp(rs.getTimestamp("timestamp"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}