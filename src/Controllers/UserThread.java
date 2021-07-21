package Controllers;

import Models.Message;
import Models.User;
import Views.RegisterMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserThread extends Thread{

    public Server server;
    public Socket socket;
    public User user;

    public UserThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String input = dataInputStream.readUTF();
                String result = processInput(input);
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            Server.userThreads.remove(this);
        }
    }

    public String processInput(String input){
        if(input.startsWith("user")){
            RegisterMenu registerMenu = new RegisterMenu();
            try {
                user = registerMenu.processRegisterCommand(input);
                if(!Server.userThreads.contains(this)) Server.userThreads.add(this);
            } catch (Exception e) {
                return e.getMessage();
            }
            return "";
        }
        Matcher matcher = getCommandMatcher(input, "duel ([\\d])");
        if(matcher.matches()){
            ArrayList<User> users = Server.oneRoundUsers;
            if(matcher.group(1).equals("3")) users = Server.threeRoundUsers;
            users.add(user);
            String result = "1_";
            if(users.indexOf(this.user) == 0) result = "0_";
            String secondUsername = this.user.username;
            label:
            while (true) {
                for(int i = 0; i < 2; i++) {
                    if(users.size() > i) secondUsername = users.get(i).username;
                    if(!secondUsername.equals(this.user.username)) break label;
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result += secondUsername;
            users.remove(this.user);
            return result;
        }
        if(input.equals("logout")){
            Server.removeUserThreadByUsername(user.username);
            return "";
        }
        if(input.equals("chatBox")){
            Gson gson = new Gson();
            return gson.toJson(server);
        }
        if(input.equals("online users")){
            return String.valueOf(Server.userThreads.size());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        Message message = gson.fromJson(input, Message.class);
        processMessage(message);

        return "";
    }

    public synchronized void processMessage(Message message){
        if(message.type.equals("new message") || message.type.equals("reply")){
            server.chatBox.add(message);
        }
        if(message.type.equals("edit")){
            for(Message aMessage: server.chatBox){
                if(aMessage.message.equals(message.messageBeforeEdit)) {
                    server.chatBox.set(server.chatBox.indexOf(aMessage), message);
                }
            }
        }
        if(message.type.equals("delete")){
            server.chatBox.removeIf(message1 -> message1.message.equals(message.message));
        }
        if(message.type.equals("pin")){
            server.pinnedMessage = message;
        }
    }

    public Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

}
