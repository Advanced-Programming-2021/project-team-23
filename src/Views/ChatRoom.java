package Views;


import Controllers.Server;
import Models.Message;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;

public class ChatRoom implements IMenu {

    public static User user;
    public static ListView<Label> listView;
    public static ArrayList<Label> messageLabels;
    public static TextField textField;
    public static Label onlineUsersLabel;
    public static Button sendButton;
    public static Button refreshButton;
    public static String requestType; // new message, reply, edit, delete, pin
    public static String selectedMessage;
    public static Label pinLabel;

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/chatRoom.fxml"));

            listView = (ListView<Label>) pane.getChildren().get(0);
            textField = (TextField) pane.getChildren().get(1);
            sendButton = (Button) pane.getChildren().get(2);
            onlineUsersLabel = (Label) pane.getChildren().get(3);
            refreshButton = (Button) pane.getChildren().get(4);
            pinLabel = (Label) pane.getChildren().get(5);
            messageLabels = new ArrayList<>();
            requestType = "new message";

            sendButton.setOnMouseClicked(mouseEvent -> {
                sendRequest();
            });
            refreshButton.setOnMouseClicked(mouseEvent -> {
                getDataFromServerAndUpdateThings();
            });

            getDataFromServerAndUpdateThings();

            Main.stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromServerAndUpdateThings(){
        try {
            String numberOfOnlineUsers = Main.getResult("online users");
            onlineUsersLabel.setText("online users: " + numberOfOnlineUsers);

            String json = Main.getResult("chatBox");
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            Server server = gson.fromJson(json, Server.class);
            ArrayList<Message> messages = server.chatBox;
            setListView(messages);
            Message pinnedMessage = server.pinnedMessage;
            if(pinnedMessage != null) setPinLabel(pinnedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPinLabel(Message message){
        String string = "pinned message:";
        string += ("\n" + message.message);
        pinLabel.setText(string);
    }

    public void setListView(ArrayList<Message> messages){
        ObservableList<Label> items = FXCollections.observableArrayList();
        for(Message message: messages){
            Label label = createLabel(message);
            items.add(label);
        }
        listView.setItems(items);
    }

    public void sendRequest(){
        Message message = new Message(user.username, requestType, textField.getText());
        if(requestType.equals("reply")) message.repliedToMessage = selectedMessage;
        if(requestType.equals("edit")) message.messageBeforeEdit = selectedMessage;
        if(requestType.equals("delete") || requestType.equals("pin")) message.message = selectedMessage;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        try {
            Main.getResult(gson.toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestType = "new message";
        selectedMessage = null;

        getDataFromServerAndUpdateThings();
    }

    public Label createLabel(Message message){
        String string = message.username + ": ";
        if(message.messageBeforeEdit != null){
            string += ("(edited)");
        }
        if(message.repliedToMessage != null){
            string += ("(replied to \"" + message.repliedToMessage + "\")");
        }
        string += ("\n" + message.message + "\n");
        Label label = new Label(string);

        MenuItem replyItem = new MenuItem("reply");
        replyItem.setOnAction(actionEvent -> {
            requestType = "reply";
            selectedMessage = message.message;
        });
        MenuItem editItem = new MenuItem("edit");
        editItem.setOnAction(actionEvent -> {
            requestType = "edit";
            selectedMessage = message.message;
        });
        MenuItem deleteItem = new MenuItem("delete");
        deleteItem.setOnAction(actionEvent -> {
            requestType = "delete";
            selectedMessage = message.message;
            sendRequest();
        });
        MenuItem pinItem = new MenuItem("pin");
        pinItem.setOnAction(actionEvent -> {
            requestType = "pin";
            selectedMessage = message.message;
            sendRequest();
        });
        label.setOnContextMenuRequested(event -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().add(replyItem);
            if(message.username.equals(user.username)) contextMenu.getItems().add(editItem);
            contextMenu.getItems().add(deleteItem);
            contextMenu.getItems().add(pinItem);
            contextMenu.show(label, event.getScreenX(), event.getScreenY());
        });
        return label;
    }

    @Override
    public void processCommand(String command) throws Exception {

    }
}
