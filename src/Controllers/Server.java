package Controllers;

import Models.Message;
import Models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public transient static int port = 12345;

    public transient static ArrayList<UserThread> userThreads;

    public transient static ArrayList<User> oneRoundUsers;
    public transient static ArrayList<User> threeRoundUsers;

    public ArrayList<Message> chatBox = new ArrayList<>();
    public Message pinnedMessage;

    static {
        userThreads = new ArrayList<>();
        oneRoundUsers = new ArrayList<>();
        threeRoundUsers = new ArrayList<>();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket);
                userThread.server = server;
                userThreads.add(userThread);
                userThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeUserThreadByUsername(String username){
        userThreads.removeIf(userThread -> userThread.user.username.equals(username));
    }
}
