package Views;

import Models.Card;
import Models.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;

public class CardCreatorMenu implements IMenu{

    public static int nonEffectPrice;
    public static int effectsPrice;
    public static Label priceLabel;
    public static TextField name;
    public static TextField type;
    public static TextField level;
    public static TextField attack;
    public static TextField defense;
    public static ListView<CheckBox> table;

    public static ArrayList<String> effects;

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/cardCreator.fxml"));

            effects = new ArrayList<>();
            name = (TextField) pane.getChildren().get(1);
            type = (TextField) pane.getChildren().get(2);
            level = (TextField) pane.getChildren().get(3);
            level.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    calculatePrice();
                }
            });
            attack = (TextField) pane.getChildren().get(4);
            attack.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    calculatePrice();
                }
            });
            defense = (TextField) pane.getChildren().get(5);
            defense.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    calculatePrice();
                }
            });
            table = (ListView<CheckBox>) pane.getChildren().get(6);
            priceLabel = (Label) pane.getChildren().get(7);
            pane.getChildren().get(8).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    createCard();
                }
            });

            setTableView();

            Main.stage.setScene(new Scene(pane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTableView(){
        ObservableList<CheckBox> items = FXCollections.observableArrayList();
        FileReader fileReader;
        String[] row;
        try {
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(!(row[0].equals("Name"))) {
                    CheckBox checkBox = createCheckBox(row[7]);
                    items.add(checkBox);
                }
            }
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\SpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(!(row[0].equals("Name"))) {
                    CheckBox checkBox = createCheckBox(row[3]);
                    items.add(checkBox);
                }
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }

        table.setItems(items);
    }

    public CheckBox createCheckBox(String string){
        CheckBox checkBox = new CheckBox(string);
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(checkBox.isSelected()) {
                    effects.add(checkBox.getText());
                    effectsPrice += 1000;
                }
                else {
                    effects.remove(checkBox.getText());
                    effectsPrice -= 1000;
                }
                showPrice();
            }
        });
        return checkBox;
    }

    public void calculatePrice(){
        try {
            int levelValue = 0, attackValue = 0, defenseValue = 0;
            if(level.getText().matches("[\\d]+")) levelValue = Integer.parseInt(level.getText());
            if(attack.getText().matches("[\\d]+")) attackValue = Integer.parseInt(attack.getText());
            if(defense.getText().matches("[\\d]+")) defenseValue = Integer.parseInt(defense.getText());
            nonEffectPrice = (levelValue * 200) + (attackValue + defenseValue);
        } catch (Exception e){
            e.printStackTrace();
        }
        showPrice();
    }

    public void showPrice(){
        priceLabel.setText("Price: " + (effectsPrice + nonEffectPrice));
    }

    public void createCard(){
        boolean isMonster = type.getText().equalsIgnoreCase("Monster");
        File file = new File(User.projectAddress + "\\src\\main\\resources\\newSpellTrap.csv");
        if(isMonster) file = new File(User.projectAddress + "\\src\\main\\resources\\newMonster.csv");
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter);
            String[] strings = {name.getText(), type.getText(), "Normal", effects.toString(), "Unlimited", String.valueOf((effectsPrice + nonEffectPrice))};
            if (isMonster) strings = new String[]{name.getText(), level.getText(), "EARTH", "Warrior", "Normal", attack.getText(), defense.getText(), effects.toString(), String.valueOf((effectsPrice + nonEffectPrice)), "0"};
            writer.writeNext(strings);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainMenu.currentUser.setWallet(MainMenu.currentUser.getWallet() - ((effectsPrice + nonEffectPrice) / 10));
    }

    @Override
    public void processCommand(String command) throws Exception {

    }
}
