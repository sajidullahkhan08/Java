-- Database setup for Chat Messenger

CREATE DATABASE IF NOT EXISTS chat_messenger;

USE chat_messenger;

-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Profiles table
CREATE TABLE profiles (
    user_id INT PRIMARY KEY,
    profile_pic LONGBLOB,
    status_message VARCHAR(255) DEFAULT 'Available',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Friends table
CREATE TABLE friends (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    friend_id INT,
    status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_friendship (user_id, friend_id)
);

-- Messages table
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    message TEXT,
    file_path VARCHAR(255),
    file_name VARCHAR(255),
    file_type VARCHAR(50),
    file_data LONGBLOB,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample users
INSERT INTO users (username, password) VALUES ('user1', 'pass1'), ('user2', 'pass2');