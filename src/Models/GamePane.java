package Models;

import Controllers.CardController;
import Controllers.GameController;
import Views.CardAnimation;
import Views.Duel;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GamePane extends Pane {

    public int playerNumber;
    public GameController gameController;
    public Pane pane;
    public ImageView cardImage;
    public Label cardLabel, phaseLabel;
    public Button nextPhaseButton, pauseButton;
    public Label[] lpLabels;
    public ImageView field;

    public GamePane(int playerNumber, GameController gameController){
        this.playerNumber = playerNumber;
        this.gameController = gameController;
        lpLabels = new Label[2];
        try {
            pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/game.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        field = (ImageView) pane.getChildren().get(0);
        cardImage = (ImageView) pane.getChildren().get(3);
        cardLabel = (Label) pane.getChildren().get(4);
        nextPhaseButton = (Button) pane.getChildren().get(9);
        pauseButton = (Button) pane.getChildren().get(10);
        ((Button) pane.getChildren().get(11)).setText(gameController.users[0].username + "'s graveyard");
        ((Button) pane.getChildren().get(12)).setText(gameController.users[1].username + "'s graveyard");
        phaseLabel = (Label) pane.getChildren().get(13);
        for(int i = 0; i < 2; i++){
            lpLabels[i] = (Label) pane.getChildren().get(2 - ((i + playerNumber) % 2));
        }
        for(int i = 0; i < 2; i++){
            ((Label) pane.getChildren().get(7 - 2 * i)).setText("nickname: " + gameController.users[(i + playerNumber) % 2].nickname);
            ((Label) pane.getChildren().get(8 - 2 * i)).setText("username: " + gameController.users[(i + playerNumber) % 2].username);
        }
    }

    public void setPhaseLabel(){
        int newPhaseNumber = gameController.phaseNumber;
        String phaseName = "";
        if(newPhaseNumber == 1){
            phaseName = ("draw phase");
        }
        if(newPhaseNumber == 2){
            phaseName = ("standby phase");
        }
        if(newPhaseNumber == 3){
            phaseName = ("main phase1");
        }
        if(newPhaseNumber == 4){
            phaseName = ("battle phase");
        }
        if(newPhaseNumber == 5){
            phaseName = ("main phase2");
        }
        if(newPhaseNumber == 6){
            phaseName = ("end phase");
        }
        phaseLabel.setText(phaseName);
    }

    public void setLpLabels(){
        for(int i = 0; i < 2; i++){
            double lp = gameController.lp[i];
            lpLabels[i].setText("LP " + (int)lp);
            Color color = Color.hsb(360.000000 - lp * (120.000000 / 8000), 1.0, 0.8);
            lpLabels[i].setTextFill(color);
        }
    }

    public void initializeCards(){
        for(int player = 0; player < 2; player++) {
            initializeDeck(player);
            initializeHand(player);
        }
    }

    public void initializeDeck(int player){
        CardLocation cardLocation;
        CardCoordinates cardCoordinates;
        ArrayList<Card> deck = gameController.boards[player].getDeckZone();
        cardLocation = CardLocation.getCardLocationByPlace("4");
        cardCoordinates = getCardCoordinates(player, cardLocation);
        for (int i = 0; i < deck.size(); i++) {
            CardImageView backOfCard = null;
            try {
                backOfCard = new CardImageView(new Image(new FileInputStream(Duel.assetsAddress + "\\Cards\\Monsters\\Unknown.jpg")), deck.get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            backOfCard.setLayoutX(cardCoordinates.x - CardLocation.smallDistance * i);
            backOfCard.setLayoutY(cardCoordinates.y - CardLocation.smallDistance * i);
            backOfCard.setPreserveRatio(true);
            backOfCard.setFitWidth(CardLocation.cardWidth);
            backOfCard.setFitHeight(CardLocation.cardWidth / CardLocation.cardRatioOfWidthToHeight);
            backOfCard.setEvents(this);
            deck.get(i).currentImageView[playerNumber] = backOfCard;
            pane.getChildren().add(backOfCard);
        }
    }

    public void initializeHand(int player){
        CardLocation cardLocation;
        CardCoordinates cardCoordinates;
        ArrayList<Card> hand = gameController.boards[player].getCardsInHand();
        for(int i = 0; i < 5; i++){
            CardImageView imageView = null;
            try {
                imageView = new CardImageView(new Image(new FileInputStream(Duel.assetsAddress + "\\Cards\\Monsters\\Unknown.jpg")), hand.get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(player == this.playerNumber) {
                imageView.setImage(hand.get(i).imageView.getImage());
            }
            cardLocation = CardLocation.getCardLocationByPlace("hand_" + (i + 1));
            cardCoordinates = getCardCoordinates(player, cardLocation);
            imageView.setLayoutX(cardCoordinates.x);
            imageView.setLayoutY(cardCoordinates.y);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(CardLocation.cardWidth);
            imageView.setFitHeight(CardLocation.cardWidth / CardLocation.cardRatioOfWidthToHeight);
            imageView.setEvents(this);
            hand.get(i).currentImageView[playerNumber] = imageView;
            pane.getChildren().add(imageView);
        }
    }

    public CardCoordinates getCardCoordinates(int playerNumber, CardLocation cardLocation){
        if(playerNumber == this.playerNumber){
            return new CardCoordinates(cardLocation.downX, cardLocation.downY);
        } else {
            return new CardCoordinates(cardLocation.upX, cardLocation.upY);
        }
    }


    // never use cards like change of heart in this project because of this method
    public void moveCard(Card card, String place1, String place2){
        CardLocation cardLocation1 = CardLocation.getCardLocationByPlace(place1);
        CardLocation cardLocation2 = CardLocation.getCardLocationByPlace(place2);
        CardCoordinates cardCoordinates1, cardCoordinates2;
        Board board;
        if(CardController.boardContainsCard(gameController.boards[playerNumber], card)) {
            board = gameController.boards[playerNumber];
            cardCoordinates1 = new CardCoordinates(cardLocation1.downX, cardLocation1.downY);
            cardCoordinates2 = new CardCoordinates(cardLocation2.downX, cardLocation2.downY);
        } else {
            board = gameController.boards[1 - playerNumber];
            cardCoordinates1 = new CardCoordinates(cardLocation1.upX, cardLocation1.upY);
            cardCoordinates2 = new CardCoordinates(cardLocation2.upX, cardLocation2.upY);
        }
        if(place2.startsWith("3")) {
            cardCoordinates2.x -= CardLocation.smallDistance * board.getGraveyard().size();
            cardCoordinates2.y -= CardLocation.smallDistance * board.getGraveyard().size();
        }
        //CardAnimation.translate(card.currentImageView[playerNumber], cardCoordinates1, cardCoordinates2);
        CardAnimation cardAnimation = new CardAnimation(card.currentImageView[playerNumber], cardCoordinates1, cardCoordinates2);
        cardAnimation.play();
    }

}
