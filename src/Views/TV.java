package Views;

import Models.User;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TV implements IMenu{

    public Pane pane;
    public ImageView imageView;
    public ChoiceBox<String> choiceBox;
    public ToggleButton onlineGamesButton;
    public ToggleButton recordedGamesButton;
    public Button startButton;
    public Button backButton;
    public int i;

    @Override
    public void show() {
        try {
            pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/TV.fxml"));

            imageView = (ImageView) pane.getChildren().get(1);
            choiceBox = (ChoiceBox<String>) pane.getChildren().get(2);
            onlineGamesButton = (ToggleButton) pane.getChildren().get(3);
            recordedGamesButton = (ToggleButton) pane.getChildren().get(4);
            startButton = (Button) pane.getChildren().get(5);
            backButton = (Button) pane.getChildren().get(6);

            setChoiceBox();
            startButton.setOnMouseClicked(mouseEvent -> start());
            //backButton.requestFocus();

            Main.stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        i = 1;
        String username = choiceBox.getValue();
        File folder = new File(User.projectAddress + "\\src\\main\\resources\\TV");
        if(onlineGamesButton.isSelected()) {
            //showOnlineGameOfUsername(username, folder);

            pane.setOnScroll(scrollEvent -> {
                Image image = null;
                while(true){
                    try {
                        image = new Image(new FileInputStream(folder.getAbsolutePath() + "\\" + username + "_" + (i++) + ".png"));
                    } catch (Exception e){
                        imageView.setImage(image);
                        i = 1;
                        return;
                    }
                }
            });
        }
        else if(recordedGamesButton.isSelected()) {
            //showRecordedGameOfUsername(username, folder);

            pane.setOnScroll(scrollEvent -> {
                try {
                    imageView.setImage(new Image(new FileInputStream(folder.getAbsolutePath() + "\\" + username + "_" + (i++) + ".png")));
                } catch (FileNotFoundException ignored) {

                }
            });
        }
    }

    public void showOnlineGameOfUsername(String username, File folder){
        Image image = null;
        int i = 1;
        while(true){
            try {
                image = new Image(new FileInputStream(folder.getAbsolutePath() + "\\" + username + "_" + (i++) + ".png"));
            } catch (Exception e){
                imageView.setImage(image);
                i = 1;
            }
        }
    }

    public void showRecordedGameOfUsername(String username, File folder){
        try {
            int i = 1;
            while(true){
                imageView.setImage(new Image(new FileInputStream(folder.getAbsolutePath() + "\\" + username + "_" + (i++) + ".png")));
            }
        } catch (Exception ignored){

        }
    }

    public void setChoiceBox(){
        try {
            ArrayList<User> allUsers = User.getAllUsers();
            for(User user: allUsers){
                choiceBox.getItems().add(user.username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processCommand(String command) throws Exception {

    }
}
