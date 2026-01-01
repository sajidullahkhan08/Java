package client;

import common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private Client client;
    private User currentUser;
    private JList<User> friendsList;
    private DefaultListModel<User> friendsModel;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton sendFileButton;
    private JButton addFriendButton;
    private JButton profileButton;
    private Map<Integer, JTextArea> chatAreas;
    private User selectedFriend;

    public MainWindow(Client client, User currentUser) {
        this.client = client;
        this.currentUser = currentUser;
        this.chatAreas = new HashMap<>();
        initializeUI();
        loadFriends();
        loadFriendRequests();
    }

    private void initializeUI() {
        setTitle("Chat Messenger - " + currentUser.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Friends list
        friendsModel = new DefaultListModel<>();
        friendsList = new JList<>(friendsModel);
        friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendsList.setBackground(new Color(240, 240, 240));
        friendsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedFriend = friendsList.getSelectedValue();
                if (selectedFriend != null) {
                    showChat(selectedFriend);
                }
            }
        });

        // Add right-click context menu for friends
        JPopupMenu friendMenu = new JPopupMenu();
        JMenuItem viewProfileItem = new JMenuItem("View Profile");
        viewProfileItem.addActionListener(e -> {
            User selectedUser = friendsList.getSelectedValue();
            if (selectedUser != null) {
                showFriendProfile(selectedUser);
            }
        });
        friendMenu.add(viewProfileItem);

        friendsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = friendsList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        friendsList.setSelectedIndex(index);
                        friendMenu.show(friendsList, e.getX(), e.getY());
                    }
                }
            }
        });
        JScrollPane friendsScroll = new JScrollPane(friendsList);
        friendsScroll.setBorder(BorderFactory.createTitledBorder("Friends"));
        friendsScroll.setPreferredSize(new Dimension(200, 0));

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(255, 255, 255));
        chatArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("Chat"));

        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(e -> sendMessage());

        sendFileButton = new JButton("Send File");
        sendFileButton.setBackground(new Color(34, 139, 34));
        sendFileButton.setForeground(Color.WHITE);
        sendFileButton.addActionListener(e -> sendFile());

        JPanel messagePanel = new JPanel(new BorderLayout(5, 0));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messagePanel.add(messageField, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(sendFileButton);
        buttonPanel.add(sendButton);
        messagePanel.add(buttonPanel, BorderLayout.EAST);

        // Buttons
        addFriendButton = new JButton("Add Friend");
        addFriendButton.setBackground(new Color(255, 165, 0));
        addFriendButton.setForeground(Color.WHITE);
        addFriendButton.addActionListener(e -> addFriend());

        profileButton = new JButton("Profile");
        profileButton.setBackground(new Color(138, 43, 226));
        profileButton.setForeground(Color.WHITE);
        profileButton.addActionListener(e -> showProfile());

        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanelTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanelTop.add(addFriendButton);
        buttonPanelTop.add(profileButton);

        // Main panel with split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, friendsScroll, chatScroll);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);

        // Layout
        setLayout(new BorderLayout());
        add(buttonPanelTop, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void loadFriends() {
        Request request = new Request();
        request.setType("getFriends");
        client.sendRequest(request);
    }

    public void loadFriendRequests() {
        Request request = new Request();
        request.setType("getFriendRequests");
        client.sendRequest(request);
    }

    public void updateFriends(User[] friends) {
        friendsModel.clear();
        for (int i = 0; i < friends.length; i++) {
            friendsModel.addElement(friends[i]);
        }
    }

    public void updateFriendRequests(User[] requests) {
        // Show in a dialog
        if (requests.length > 0) {
            for (int i = 0; i < requests.length; i++) {
                User requester = requests[i];
                int result = JOptionPane.showConfirmDialog(this, requester.getUsername() + " wants to be friends. Accept?", "Friend Request", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Request request = new Request();
                    request.setType("acceptFriendRequest");
                    request.setFriendRequestId(requester.getId());
                    client.sendRequest(request);
                }
            }
        }
    }

    private void showChat(User friend) {
        chatArea.setText("");
        Request request = new Request();
        request.setType("getMessages");
        request.setTargetUserId(friend.getId());
        client.sendRequest(request);
    }

    public void receiveMessage(Message message) {
        // Assuming message is for current chat
        if (selectedFriend != null && (message.getSenderId() == selectedFriend.getId() || message.getReceiverId() == selectedFriend.getId())) {
            if ("file".equals(message.getType())) {
                try {
                    File file = new File("received_" + message.getFileName());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(message.getFileData());
                    fos.close();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String time = sdf.format(message.getTimestamp());
                    chatArea.append("[" + time + "] " + selectedFriend.getUsername() + " sent file: " + file.getName() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                appendMessage(message);
            }
        }
    }

    private void appendMessage(Message message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(message.getTimestamp());
        String sender = message.getSenderId() == currentUser.getId() ? "You" : selectedFriend.getUsername();
        chatArea.append("[" + time + "] " + sender + ": " + message.getContent() + "\n");
    }

    private void sendMessage() {
        if (selectedFriend != null && !messageField.getText().isEmpty()) {
            Message message = new Message(currentUser.getId(), selectedFriend.getId(), messageField.getText());
            Request request = new Request();
            request.setType("sendMessage");
            request.setMessage(message);
            client.sendRequest(request);
            appendMessage(message);
            messageField.setText("");
        }
    }

    private void sendFile() {
        if (selectedFriend != null) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    byte[] fileData = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    fis.read(fileData);
                    fis.close();
                    Message message = new Message(currentUser.getId(), selectedFriend.getId(), file.getName(), fileData);
                    Request request = new Request();
                    request.setType("sendMessage");
                    request.setMessage(message);
                    client.sendRequest(request);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String time = sdf.format(message.getTimestamp());
                    chatArea.append("[" + time + "] You sent file: " + file.getName() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addFriend() {
        String friendUsername = JOptionPane.showInputDialog(this, "Enter friend's username:");
        if (friendUsername != null && !friendUsername.isEmpty()) {
            Request request = new Request();
            request.setType("sendFriendRequest");
            request.setTargetUsername(friendUsername);
            client.sendRequest(request);
        }
    }

    private void showProfile() {
        ProfileWindow profileWindow = new ProfileWindow(client, currentUser, currentUser, true);
        profileWindow.setVisible(true);
    }

    private void showFriendProfile(User friend) {
        ProfileWindow profileWindow = new ProfileWindow(client, friend, currentUser, false);
        profileWindow.setVisible(true);
    }

    public void updateFriendStatus(int userId, String status) {
        for (int i = 0; i < friendsModel.getSize(); i++) {
            User friend = friendsModel.get(i);
            if (friend.getId() == userId) {
                friend.setStatus(status);
                friendsModel.set(i, friend);
                break;
            }
        }
    }

    public void displayMessages(Message[] messages) {
        chatArea.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < messages.length; i++) {
            Message msg = messages[i];
            String time = sdf.format(msg.getTimestamp());
            if ("file".equals(msg.getType())) {
                String sender = msg.getSenderId() == currentUser.getId() ? "You" : selectedFriend.getUsername();
                chatArea.append("[" + time + "] " + sender + " sent file: " + msg.getFileName() + "\n");
            } else {
                String sender = msg.getSenderId() == currentUser.getId() ? "You" : selectedFriend.getUsername();
                chatArea.append("[" + time + "] " + sender + ": " + msg.getContent() + "\n");
            }
        }
    }
}