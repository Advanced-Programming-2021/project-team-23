package Views;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class DeckMenu implements IMenu , ICheatCode {

    public static User user;
    public static ListView<CheckBox> table;
    public static Deck currentDeck;
    public static TextField newDeckNameTextField;

    public static CardImageView currentCardImageView;
    public static ImageView imageView;
    public static Label cardLabel;
    public static TextField cardFromMainDeck;
    public static TextField cardFromSideDeck;

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/deckMenu.fxml"));

            user = MainMenu.currentUser;
            table = (ListView<CheckBox>) pane.getChildren().get(1);
            newDeckNameTextField = (TextField) pane.getChildren().get(2);
            setTableView();

            Main.stage.setScene(new Scene(pane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTableView(){
        ObservableList<CheckBox> items = FXCollections.observableArrayList();

        ArrayList<Deck> listOfDecks = user.listOfDecks;
        for(Deck deck: listOfDecks){
            //System.out.println(deck.name);
            CheckBox checkBox = createCheckBox(deck);
            if(deck.isActive) checkBox.setStyle("-fx-background-color: yellow;");
            items.add(checkBox);
        }

        table.setItems(items);
    }

    public CheckBox createCheckBox(Deck deck){
        CheckBox checkBox = new CheckBox(deck.getStringFormat());
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(checkBox.isSelected()) {
                    currentDeck = deck;
                }
            }
        });
        return checkBox;
    }

    public void showDeck(){
        setCards();
    }

    public void setCards(){
        Pane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/deck.fxml"));

            ((Label) pane.getChildren().get(1)).setText(currentDeck.name);
            imageView = (ImageView) pane.getChildren().get(0);
            cardLabel = (Label) pane.getChildren().get(2);
            cardFromMainDeck = (TextField) pane.getChildren().get(3);
            cardFromSideDeck = (TextField) pane.getChildren().get(4);

        } catch (IOException e) {
            e.printStackTrace();
        }
        double x = 200.0;
        double[] y = new double[]{40, 125, 210, 295};
        double distance = 36.333333;
        double cardWidth = 45.0;
        double cardHeight = cardWidth / CardLocation.cardRatioOfWidthToHeight;
        ArrayList<Card> mainDeck = currentDeck.mainDeck;
        for(Card card: mainDeck){
            ImageView cardImage = new Card(card.getName()).imageView;
            CardImageView cardImageView = new CardImageView(cardImage.getImage(), card);
            cardImageView.setFitWidth(cardWidth);
            cardImageView.setFitHeight(cardHeight);
            int index = mainDeck.indexOf(card);
            cardImageView.setLayoutX(x + distance * (index % 15));
            cardImageView.setLayoutY(y[index / 15]);
            setEventHandlerForCard(cardImageView);
            pane.getChildren().add(cardImageView);
        }
        ArrayList<Card> sideDeck = currentDeck.sideDeck;
        for(Card card: sideDeck){
            ImageView cardImage = new Card(card.getName()).imageView;
            CardImageView cardImageView = new CardImageView(cardImage.getImage(), card);
            cardImageView.setFitWidth(cardWidth);
            cardImageView.setFitHeight(cardHeight);
            int index = sideDeck.indexOf(card);
            cardImageView.setLayoutX(x + distance * (index % 15));
            cardImageView.setLayoutY(400.0);
            setEventHandlerForCard(cardImageView);
            pane.getChildren().add(cardImageView);
        }

        Main.stage.setScene(new Scene(pane));
    }

    public void setEventHandlerForCard(CardImageView cardImageView){
        cardImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currentCardImageView = cardImageView;
                imageView.setImage(cardImageView.getImage());
                Card card = cardImageView.card;
                String text = card.getName() + "\n";
                if(card.isMonster()) text += ("ATK:" + card.getAttack() + " DEF:" + card.getDefense() + "\n");
                text += "\n";
                text += card.getDescription();
                cardLabel.setText(text);
            }
        });
    }

    @Override
    public void processCommand(String command) throws IOException {
        User user = MainMenu.currentUser;
        try {
            String message = processDeckCommands(command, user);
            if(message != null) System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String processDeckCommands(String command, User user) throws Exception {
        if (command.matches("^menu show-current$")) {
            return ("deck menu");
        }
        Matcher matcher;
        matcher = getCommandMatcher(command, "deck create (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = Deck.getDeckByName(deckName);
            if(deck != null){
                throw new Exception("deck with name " + deckName + " already exists");
            }
            deck = new Deck(deckName);
            user.addDeck(deck);
            return ("deck created successfully!");
        }
        matcher = getCommandMatcher(command, "deck delete (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            user.deleteDeck(deck);
            return ("deck deleted successfully!");
        }
        matcher = getCommandMatcher(command, "deck set-active (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            if(user.activeDeck != null) user.activeDeck.isActive = false;
            deck.isActive = true;
            user.activeDeck = deck;
            User.setUserInFile(user);
            return ("deck activated successfully!");
        }
        matcher = getCommandMatcher(command, "deck add-card card (.+) deck (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            Card card = user.getCardByNameFromCardsInNoDeck(cardName);
            if(card == null){
                throw new Exception("card with name " + cardName + " does not exist");
            }
            String deckName = matcher.group(2);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck add-card card (.+) deck (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(2);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            if(isSideDeck && deck.sideDeck.size() == 15){
                throw new Exception("side deck is full");
            }
            if((!isSideDeck) && deck.mainDeck.size() == 60){
                throw new Exception("main deck is full");
            }
            int number = deck.getNumberOfCardByName(cardName);
            if((card.getStatus() != null && card.getStatus().equals("Limited") && number == 1)
                    || number == 3) {
                throw new Exception("there are already " + number + " cards with name " + cardName + " in deck " + deckName);
            }
            user.addCardToDeck(card, deck, isSideDeck);
            return "card added to deck successfully";
        }
        matcher = getCommandMatcher(command, "deck rm-card card (.+) deck (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            String deckName = matcher.group(2);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck rm-card card (.+) deck (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(2);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            Card card;
            if(isSideDeck) {
                card = deck.getCardByNameInSideDeck(cardName);
                if (card == null) {
                    throw new Exception("card with name " + cardName + " does not exist in side deck");
                }
                deck.sideDeck.remove(card);
            } else {
                card = deck.getCardByNameInMainDeck(cardName);
                if (card == null) {
                    throw new Exception("card with name " + cardName + " does not exist in main deck");
                }
                deck.mainDeck.remove(card);
            }
            user.cardsInNoDeck.add(card);
            User.setUserInFile(user);
            return "card removed from deck successfully";
        }
        matcher = getCommandMatcher(command, "deck show all");
        if(matcher.matches()) {
            DeckMenu.showAllDecks(user);
            return null;
        }
        matcher = getCommandMatcher(command, "deck show deck-name (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck show deck-name (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(1);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            DeckMenu.showDeck(deck, isSideDeck);
            return null;
        }
        matcher = getCommandMatcher(command, "deck show cards");
        if(matcher.matches()) {
            DeckMenu.showAllCards(user);
            return null;
        }
        throw new Exception("invalid command");
    }


    public static void showAllDecks(User user){
        System.out.println("Decks:");
        System.out.println("Active decks:");
        Deck activeDeck = user.activeDeck;
        if(activeDeck != null){
            System.out.println(activeDeck.getStringFormat());
        }
        System.out.println("Other decks:");
        for(Deck deck: user.listOfDecks){
            if(!deck.isActive) System.out.println(deck.getStringFormat());
        }
    }

    public static void showDeck(Deck deck, boolean isSideDeck){
        ArrayList<Card> deckCards;
        if(isSideDeck) deckCards = deck.sideDeck;
        else deckCards = deck.mainDeck;
        ArrayList<String> monsters = new ArrayList<>(), spellsAndTraps = new ArrayList<>();
        for(Card card: deckCards){
            if(card.isMonster()) monsters.add(card.getName() + ": " + card.getDescription());
            else spellsAndTraps.add(card.getName() + ": " + card.getDescription());
        }
        Object[] monstersArray = monsters.toArray();
        Object[] spellsAndTrapsArray = spellsAndTraps.toArray();
        Arrays.sort(monstersArray);
        Arrays.sort(spellsAndTrapsArray);
        System.out.println("Deck: " + deck.name);
        if(isSideDeck) System.out.println("Side deck:");
        else System.out.println("Main deck:");

        System.out.println("Monsters:");
        for(Object object: monstersArray){
            System.out.println((String) object);
        }
        System.out.println("Spell and Traps:");
        for(Object object: spellsAndTrapsArray){
            System.out.println((String) object);
        }
    }

    public static void showAllCards(User user){
        ArrayList<Card> allCards = new ArrayList<>(user.cardsInNoDeck);
        for(Deck deck: user.listOfDecks){
            allCards.addAll(deck.mainDeck);
            allCards.addAll(deck.sideDeck);
        }

        ArrayList<String> cardStrings = new ArrayList<>();
        for(Card card: allCards){
            cardStrings.add(card.getName() + ": " + card.getDescription());
        }
        Object[] cardsArray = cardStrings.toArray();
        Arrays.sort(cardsArray);

        for(Object object: cardsArray){
            System.out.println((String) object);
        }
    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }

}
