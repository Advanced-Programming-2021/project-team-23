package Views;

import Models.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu implements IMenu , ICheatCode {

    public static Pane pane;
    public static ImageView imageView;
    public static Label cardLabel;
    public static Label walletLabel;
    public static Label priceLabel;
    public static Label countLabel;
    public static Label dropLabel;

    @Override
    public void show() {
        try {
            pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/shop.fxml"));
            imageView = (ImageView) pane.getChildren().get(0);
            cardLabel = (Label) pane.getChildren().get(1);
            walletLabel = (Label) pane.getChildren().get(2);
            walletLabel.setText("Wallet: " + MainMenu.currentUser.wallet);
            priceLabel = (Label) pane.getChildren().get(3);
            countLabel = (Label) pane.getChildren().get(4);
            dropLabel = (Label) pane.getChildren().get(5);
            setCards();
            Main.stage.setScene(new Scene(pane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCards(){
        double x = 200.0;
        double[] y = new double[]{50, 135, 220, 305, 390};
        double distance = 36.0;
        double cardWidth = 45.0;
        double cardHeight = cardWidth / CardLocation.cardRatioOfWidthToHeight;
        ArrayList<Card> allCards = getAllCards();
        for(Card card: allCards){
            ImageView cardImage = new Card(card.getName()).imageView;
            CardImageView cardImageView = new CardImageView(cardImage.getImage(), card);
            cardImageView.setFitWidth(cardWidth);
            cardImageView.setFitHeight(cardHeight);
            int index = allCards.indexOf(card);
            cardImageView.setLayoutX(x + distance * (index % 16));
            cardImageView.setLayoutY(y[index / 16]);
            setEventHandlerForCard(cardImageView);
            pane.getChildren().add(cardImageView);
        }
    }

    private void setEventHandlerForCard(CardImageView cardImageView) {
        cardImageView.setOnMouseEntered(mouseEvent -> {
            imageView.setImage(cardImageView.getImage());
            Card card = cardImageView.card;
            String text = card.getName() + "\n";
            if(card.isMonster()) text += ("ATK:" + card.getAttack() + " DEF:" + card.getDefense() + "\n");
            text += "\n";
            text += card.getDescription();
            cardLabel.setText(text);
            priceLabel.setText("Price: " + card.getPrice());
            countLabel.setText("Card Count: " + (MainMenu.currentUser.getNumberOfCardsInNoDeckByName(card.getName()) - 1));
        });

        if(cardImageView.card.getPrice() <= MainMenu.currentUser.wallet) {
            cardImageView.setOnDragDetected(mouseEvent -> {
                Dragboard dragboard = cardImageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(cardImageView.card.getName());
                dragboard.setContent(content);
            });
            cardImageView.setOnMouseDragged(mouseEvent -> {
                mouseEvent.setDragDetect(true);
            });
        }

        dropLabel.setOnDragOver(dragEvent -> {
            if(dragEvent.getGestureSource() != dropLabel && dragEvent.getDragboard().hasString()){
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });
        dropLabel.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            if(dragboard.hasString()){
                String cardName = dragboard.getString();
                processCommand("shop buy " + cardName);
                dragEvent.setDropCompleted(true);
                walletLabel.setText("Wallet: " + MainMenu.currentUser.wallet);
                countLabel.setText("Card Count: " + (Integer.parseInt(countLabel.getText().substring(12)) + 1));
            } else {
                dragEvent.setDropCompleted(false);
            }
            dragEvent.consume();
        });
    }

    @Override
    public void processCommand(String command) {
        if (command.matches("^menu show-current$")) {
            System.out.println("shop menu");
            return;
        }
        Matcher matcher;
        matcher = getCommandMatcher(command, "shop buy (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            try {
                String message = buyCard(cardName, MainMenu.currentUser);
                IMenu.showSuccess(message);
            } catch (Exception e) {
                IMenu.showErrorAlert(e.getMessage());
            }
            return;
        }
        if(command.equals("shop show all") || command.equals("shop all show")){
            showAllCards();
            return;
        }
        System.out.println("invalid command");
    }

    public String buyCard(String cardName, User user) throws Exception{
        Card card = Card.getCardByName(cardName);
        if(card == null){
            throw new Exception("there is no card with this name");
        }
        if(card.getPrice() > user.wallet){
            throw new Exception("not enough money");
        }
        user.wallet -= card.getPrice();
        user.cardsInNoDeck.add(card);
        return "Card added successfully";
    }

    public void showAllCards(){
        ArrayList<Card> allCards = getAllCards();
        String[] cardStrings = new String[allCards.size()];
        Card card;
        for(int i = 0; i < allCards.size(); i++){
            card = allCards.get(i);
            cardStrings[i] = card.getName() + ": " + card.getPrice();
        }
        Arrays.sort(cardStrings);
        for(String string: cardStrings){
            System.out.println(string);
        }
    }

    public ArrayList<Card> getAllCards(){
        FileReader fileReader;
        String[] row;
        ArrayList<Card> allCards = new ArrayList<>();
        try {
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(row[0].equals("Name")) continue;
                allCards.add(new Card(row[0]));
            }
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\SpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(row[0].equals("Name")) continue;
                allCards.add(new Card(row[0]));
            }
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\newSpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(row[0].equals("Name")) continue;
                allCards.add(new Card(row[0]));
            }
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\newMonster.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(row[0].equals("Name")) continue;
                allCards.add(new Card(row[0]));
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return allCards;
    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
