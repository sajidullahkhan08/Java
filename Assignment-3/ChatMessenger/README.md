# Chat Messenger Application

A comprehensive client-server chat application built with Java Swing, Socket Programming, and MySQL database. Features real-time messaging, file transfer, friend management, and user profiles with a modern, intuitive GUI.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [API Reference](#api-reference)
- [Troubleshooting](#troubleshooting)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### 🔐 User Authentication
- Secure user registration and login
- Unique username validation
- Password-based authentication

### 👤 User Profiles
- Custom profile pictures (image upload)
- Status messages (custom text or predefined)
- Profile viewing for friends and self
- Real-time profile updates

### 👥 Friend Management
- Send and receive friend requests
- Accept/reject friend requests
- Real-time friend request notifications
- Friends list with online/offline status

### 💬 Real-Time Messaging
- Instant text messaging between friends
- Message timestamps ([HH:mm] format)
- Chat history loading
- Message delivery confirmation

### 📁 File Transfer
- Send images, documents, and videos
- Automatic file saving with "received_" prefix
- File type validation
- Progress indication for large files

### 🔴 Online Presence
- Real-time online/offline status updates
- Automatic status broadcasting
- Friend list status indicators

### 🎨 Modern UI
- Clean, intuitive Swing-based interface
- Responsive design with split panes
- Color-coded buttons and status indicators
- Right-click context menus
- Professional styling with proper spacing

## 🏗️ Architecture

### Client-Server Model
```
┌─────────────────┐    TCP/IP    ┌─────────────────┐
│   Client App    │◄──────────► │   Server App    │
│                 │             │                 │
│ - Login Window  │             │ - Client Handler│
│ - Main Chat UI  │             │ - User Sessions │
│ - Profile Mgmt  │             │ - Message Router│
└─────────────────┘             └─────────────────┘
                                      │
                                      ▼
                            ┌─────────────────┐
                            │   MySQL DB      │
                            │                 │
                            │ - Users         │
                            │ - Profiles      │
                            │ - Friends       │
                            │ - Messages      │
                            └─────────────────┘
```

### Components

**Server Components:**
- `Server.java`: Main server class handling connections
- `ClientHandler.java`: Threaded handler for each client
- `DatabaseHelper.java`: Database operations and queries

**Client Components:**
- `Client.java`: Main client class managing connection
- `LoginWindow.java`: Authentication interface
- `MainWindow.java`: Primary chat interface
- `ProfileWindow.java`: Profile management (view/edit modes)

**Common Components:**
- `User.java`: User data model
- `Message.java`: Message data model
- `Request.java`: Client-to-server communication
- `Response.java`: Server-to-client communication

## 📋 Prerequisites

### System Requirements
- **Operating System**: Windows 10/11, Linux, or macOS
- **Java**: JDK 11 or higher
- **MySQL**: Version 5.7 or higher
- **Memory**: Minimum 512MB RAM
- **Storage**: 100MB free space

### Software Dependencies
- Java Development Kit (JDK)
- MySQL Server and Client
- MySQL Connector/J (included in `lib/`)

## 🚀 Installation

### 1. Clone or Download
```bash
# Navigate to your Java projects directory
cd "C:\Users\Administrator\Desktop\Languages\Java\Assignment-3\ChatMessenger"
```

### 2. Install MySQL
- Download and install MySQL Server from [mysql.com](https://dev.mysql.com/downloads/mysql/)
- Start MySQL service
- Note down root password

### 3. Setup Database
```bash
# Create database and tables
mysql --user=**** --password="****" -e "CREATE DATABASE IF NOT EXISTS chat_messenger;"

# Import schema
Get-Content db_setup.sql | mysql --user=****** --password="*****" chat_messenger
```

### 4. Verify MySQL Connector
Ensure `lib/mysql-connector-j.jar` exists. If not:
```bash
# Copy from your MySQL installation
Copy-Item "C:\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" "lib\mysql-connector-j.jar"
```

### 5. Compile Application
```bash
# Compile all components
javac -cp "lib\mysql-connector-j.jar;src" src\common\*.java src\server\*.java src\client\*.java
```

## ⚙️ Configuration

### Database Configuration
Located in `.env` file:
```env
DB_USER=****
DB_PASS=*****
```

### Network Configuration
Default settings in `Client.java`:
```java
private static final String SERVER_IP = "localhost";
private static final int SERVER_PORT = 12345;
```

### Customizing Port
To change the server port, modify:
- `Server.java`: `private static final int PORT = 12345;`
- `Client.java`: `private static final int SERVER_PORT = 12345;`

## 🎯 Usage

### Starting the Application

**Terminal 1 - Start Server:**
```bash
java -cp "lib\mysql-connector-j.jar;src" server.Server
```
Expected output: `Server started on port 12345`

**Terminal 2 - Start Client:**
```bash
java -cp "lib\mysql-connector-j.jar;src" client.Client
```

### Using the Application

1. **Login/Register**
   - Enter username and password
   - Click "Login" or "Signup"

2. **Add Friends**
   - Click "Add Friend" button
   - Enter friend's username
   - Wait for acceptance

3. **Chat**
   - Select friend from list
   - Type message and press Send
   - Send files using "Send File" button

4. **Profile Management**
   - Click "Profile" to edit your profile
   - Right-click friends to view their profiles
   - Upload profile pictures and set status

### Sample Users
The database includes sample users:
- Username: `user1`, Password: `pass1`
- Username: `user2`, Password: `pass2`

## 🗄️ Database Schema

### Tables Overview

```sql
-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User profiles
CREATE TABLE profiles (
    user_id INT PRIMARY KEY,
    profile_pic LONGBLOB,
    status_message VARCHAR(255) DEFAULT 'Available',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Friend relationships
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

-- Messages storage
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    message TEXT,
    file_name VARCHAR(255),
    file_type VARCHAR(50),
    file_data LONGBLOB,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### Relationships
- **Users** ↔ **Profiles**: One-to-one (user_id)
- **Users** ↔ **Friends**: Many-to-many (user_id, friend_id)
- **Users** ↔ **Messages**: One-to-many (sender_id, receiver_id)

## 📡 API Reference

### Network Protocol

#### Request Types
- `login`: User authentication
- `signup`: User registration
- `getFriends`: Retrieve friends list
- `sendFriendRequest`: Send friend request
- `acceptFriendRequest`: Accept friend request
- `sendMessage`: Send text/file message
- `getMessages`: Load chat history
- `updateProfile`: Update user profile

#### Response Types
- `login`: Authentication result
- `signup`: Registration result
- `getFriends`: Friends list data
- `newFriendRequest`: Friend request notification
- `newMessage`: Incoming message
- `statusUpdate`: User status change
- `refreshFriends`: Update friends list

### Message Format
```java
// Text Message
Message msg = new Message(senderId, receiverId, "Hello World");

// File Message
Message fileMsg = new Message(senderId, receiverId, fileName, fileData);
```

## 🔧 Troubleshooting

### Common Issues

**1. MySQL Connection Failed**
```
java.sql.SQLException: Access denied for user 'root'@'localhost'
```
**Solution:**
- Ensure MySQL service is running
- Verify credentials in `.env` file
- Check if database `chat_messenger` exists

**2. Port Already in Use**
```
java.net.BindException: Address already in use: bind
```
**Solution:**
- Kill existing Java processes: `Stop-Process -Name java`
- Change port in both `Server.java` and `Client.java`

**3. Compilation Errors**
```
error: cannot find symbol
```
**Solution:**
- Ensure all imports are present
- Check classpath: `-cp "lib\mysql-connector-j.jar;src"`
- Recompile in correct order: common → server/client

**4. Client Cannot Connect**
```
Connection refused
```
**Solution:**
- Verify server is running
- Check firewall settings
- Ensure correct IP/port configuration

**5. File Transfer Issues**
```
Permission denied
```
**Solution:**
- Run application with proper permissions
- Check write access to client directory
- Verify file size limits

### Debug Mode
Run with verbose output:
```bash
java -verbose:class -cp "lib\mysql-connector-j.jar;src" client.Client
```

### Logs
Server logs appear in terminal. For persistent logging, redirect output:
```bash
java -cp "lib\mysql-connector-j.jar;src" server.Server > server.log 2>&1
```

## 💻 Development

### Project Structure
```
ChatMessenger/
├── src/
│   ├── common/          # Shared classes
│   │   ├── User.java
│   │   ├── Message.java
│   │   ├── Request.java
│   │   └── Response.java
│   ├── server/          # Server-side code
│   │   ├── Server.java
│   │   ├── ClientHandler.java
│   │   └── DatabaseHelper.java
│   └── client/          # Client-side code
│       ├── Client.java
│       ├── LoginWindow.java
│       ├── MainWindow.java
│       └── ProfileWindow.java
├── lib/                 # Dependencies
│   └── mysql-connector-j.jar
├── db_setup.sql         # Database schema
├── .env                 # Configuration
└── README.md           # This file
```

### Building from Source
```bash
# Clean compile
javac -cp "lib\mysql-connector-j.jar;src" src\common\*.java
javac -cp "lib\mysql-connector-j.jar;src" src\server\*.java
javac -cp "lib\mysql-connector-j.jar;src" src\client\*.java

# Run tests (if implemented)
# java -cp "lib\mysql-connector-j.jar;src;test" TestSuite
```

### Adding New Features
1. Define protocol messages in `Request.java`/`Response.java`
2. Implement server-side logic in `ClientHandler.java`
3. Add client-side UI in appropriate window class
4. Update database schema if needed
5. Test with multiple clients

## 🤝 Contributing

### Guidelines
1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -am 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit a pull request

### Code Standards
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Handle exceptions properly
- Test with multiple concurrent users
- Maintain backward compatibility

### Testing
- Test with 2+ concurrent clients
- Verify database integrity
- Test file transfers of various sizes
- Check network disconnection handling

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎯 Future Enhancements

- [ ] Message encryption for security
- [ ] Group chat functionality
- [ ] Message read receipts
- [ ] Voice/video calling
- [ ] Message search and filtering
- [ ] Dark mode theme
- [ ] Mobile client (Android)
- [ ] Web client interface
- [ ] Push notifications
- [ ] Message history export
- [ ] Admin panel for user management

---

**Built with ❤️ using Java Swing, Socket Programming, and MySQL**

For questions or support, please check the troubleshooting section or create an issue.