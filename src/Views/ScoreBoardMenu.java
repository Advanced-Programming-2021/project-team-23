package Views;

import Controllers.AI;
import Models.ICheatCode;
import Models.User;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class ScoreBoardMenu implements IMenu , ICheatCode {

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/scoreboard.fxml"));

            ListView<Label> listView = (ListView<Label>) pane.getChildren().get(0);
            listView.setItems(getUsers());

            Main.stage.setScene(new Scene(pane));

//            TableView<User> table = new TableView<>();
//            TableColumn<User, Integer> rank = new TableColumn<>("rank");
//            TableColumn<User, String> nickname = new TableColumn<>("nickname");
//            TableColumn<User, Integer> score = new TableColumn<>("score");
//
//            rank.setCellValueFactory(new PropertyValueFactory<User, Integer>("rank"));
//            nickname.setCellValueFactory(new PropertyValueFactory<User, String>("nickname"));
//            score.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));
//
//            rank.setPrefWidth(35);
//            nickname.setPrefWidth(100);
//            score.setPrefWidth(65);
//
//            //table.getColumns().add(rank);
//            table.getColumns().add(nickname);
//            table.getColumns().add(score);
//
//            table.setItems(getUsers());
////            table.setRowFactory(tableView -> {
////                TableRow<User> row = new TableRow<>();
//////                row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"),
//////                        row.getItem() == MainMenu.currentUser);
////                if(row.getItem() != null && row.getItem().username.equals(MainMenu.currentUser.username)){
////                    System.out.println(1);
////                    row.setStyle("-fx-background-color: yellow;");
////                }
////                return row;
////            });
//
//            table.setLayoutX(200);
//            table.setLayoutY(100);
//            table.setPrefHeight(200);
//            table.setPrefWidth(200);
//
//            pane.getChildren().add(table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Label> getUsers(){
        ObservableList<Label> items = FXCollections.observableArrayList();
        List<User> users = null;
        try {
            users = sortUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 1; i <= users.size(); i++){
            Label label = new Label("    " + i + "\t\t\t  " + users.get(i - 1).nickname + "\t\t\t  " + users.get(i - 1).score + "    ");
            if(users.get(i - 1).username.equals(MainMenu.currentUser.username)) label.setStyle("-fx-background-color: yellow;");
            items.add(label);
        }
        return items;
    }

    @Override
    public void processCommand(String command) throws IOException {
        if (command.matches("^menu show-current$")) {
            System.out.println("scoreboard menu");
            return;
        }
        if(command.equals("scoreboard show")){
            List<User> users = sortUsers();
            int rank = 1;
            for(int i = 0; i < users.size(); i++){
                if(i > 0 && users.get(i).getScore() < users.get(i - 1).getScore()) rank++;
                System.out.println(rank + "- " + users.get(i).getNickname() + ": " + users.get(i).getScore());
            }
        } else {
            System.out.println("invalid command");
        }
    }

    public List<User> sortUsers() throws IOException {
        List<User> users = User.getAllUsers().stream().filter(e -> !e.username.equals("AI")).collect(Collectors.toList());
        for(int i = 0; i < users.size() - 1; i++){
            for(int j = i + 1; j < users.size(); j++){
                if((users.get(i).getScore() < users.get(j).getScore()) ||
                        ((users.get(i).getScore() == users.get(j).getScore()) && (users.get(i).getNickname().compareToIgnoreCase(users.get(j).getNickname()) > 0))){
                    Collections.swap(users, i, j);
                }
            }
        }
        return users;
    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
