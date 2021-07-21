package Views;

import Controllers.CardController;
import Controllers.CommandController;
import Controllers.GameController;
import Controllers.action.Action;
import Controllers.action.AdvancedRitualArt;
import Models.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Duel {

    public static String assetsAddress = User.projectAddress + "\\src\\Assets";
    public static GameController gameController;
    public static Scanner scanner;
    public static GamePane[] panes = new GamePane[2];
    public static int screenIndex;

    public MediaPlayer[] mediaPlayers = new MediaPlayer[]{
    new MediaPlayer(new Media(getClass().getResource("/sounds/1.mp3").toExternalForm())),
    new MediaPlayer(new Media(getClass().getResource("/sounds/2.mp3").toExternalForm())),
    new MediaPlayer(new Media(getClass().getResource("/sounds/3.mp3").toExternalForm())),
    new MediaPlayer(new Media(getClass().getResource("/sounds/4.mp3").toExternalForm())),
    new MediaPlayer(new Media(getClass().getResource("/sounds/5.mp3").toExternalForm())),
    new MediaPlayer(new Media(getClass().getResource("/sounds/6.mp3").toExternalForm()))
    };

    public Duel(GameController gameController){
        Duel.gameController = gameController;
        scanner = IMenu.scan;
    }

    public void setStages(){
        screenIndex = 1;
        for(int i = 0; i < 2; i++){
            panes[i] = new GamePane(i, gameController);
            panes[i].initializeCards();
            panes[i].pane.requestFocus();
        }
        setLpLabels();

        GameStages gameStages = new GameStages();
        try {
            gameStages.start(gameController);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playSound();
    }

    public void playSound(){
        for(int i = 0; i < mediaPlayers.length; i++){
            int finalI = i;
            mediaPlayers[i].setOnEndOfMedia(() -> mediaPlayers[(finalI + 1) % mediaPlayers.length].play());
        }
        mediaPlayers[4].play();
    }

    public void playDieSound(){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/sounds/die.wav").toExternalForm()));
        mediaPlayer.play();
    }

    public void playEndGameSound(){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/sounds/endGame.mp3").toExternalForm()));
        mediaPlayer.play();
    }

    public void setLpLabels(){
        for(int i = 0; i < 2; i++){
            panes[i].setLpLabels();
        }
    }

    public void aFieldSpellActivated(Card fieldSpell){
        String fieldName = switch (fieldSpell.getName()) {
            case "Yami" -> "yami";
            case "Forest" -> "mori";
            case "Umiiruka" -> "umi";
            default -> "fusion";
        };
        for(int i = 0; i < 2; i++) {
            try {
                panes[i].field.setImage(new Image(new FileInputStream(assetsAddress + "\\Field\\fie_" + fieldName + ".png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printGameStarted(){
        System.out.println("\nDuel Started");
    }

    public static void printPhaseName(int newPhaseNumber){
        System.out.println();
        if(newPhaseNumber == 1){
            System.out.println("phase: draw phase");
        }
        if(newPhaseNumber == 2){
            System.out.println("phase: standby phase");
        }
        if(newPhaseNumber == 3){
            System.out.println("phase: main phase1");
        }
        if(newPhaseNumber == 4){
            System.out.println("phase: battle phase");
        }
        if(newPhaseNumber == 5){
            System.out.println("phase: main phase2");
        }
        if(newPhaseNumber == 6){
            System.out.println("phase: end phase");
        }
        System.out.println();
    }

    public static void printSuccessfulAddingCardInDrawPhase(Card card){
        System.out.println("new card added to the hand: " + card.getName());
    }

    public void printCurrentPlayerTurn(){
        String nickname = gameController.getUser(gameController.getCurrentPlayer()).getNickname();
        System.out.println("it is " + nickname + "'s turn");
    }

    public void getCommands(){
        String input;
        Matcher matcher;
        while((!gameController.isDuelEnded) &&
                (!gameController.isDuelEnded()) &&
                (!(input = scanner.nextLine()).equals("next phase"))){
            matcher = getCommandMatcher(input, "select ([a-z0-9 -]+)");
            if(matcher.matches()) select(matcher);
            matcher = getCommandMatcher(input, "increase LP ([\\d]+)");
            if(matcher.matches()) gameController.increaseLp(gameController.currentPlayer, Integer.parseInt(matcher.group(1)));
            matcher = getCommandMatcher(input, "duel set-winner (.+)");
            if(matcher.matches()) setWinnerByCheatCode(matcher);
            else if(input.matches("summon")) summon();
            else if(input.matches("set")) set();
            else if(input.matches("surrender")) surrender();
            else {
                matcher = getCommandMatcher(input, "set position (attack|defense)");
                if(matcher.matches()) changePosition(matcher.group(1));
                else {
                    matcher = getCommandMatcher(input, "set (attack|defense) position");
                    if(matcher.matches()) changePosition(matcher.group(1));
                    else if (input.matches("flip-summon")) flipSummon();
                    else if(input.matches("attack direct") || input.matches("direct attack")) directAttack();
                    else{
                        matcher = getCommandMatcher(input, "attack (1|2|3|4|5)");
                        if(matcher.matches()) attack(matcher.group(1));
                        else if(input.matches("activate effect")) activateSpell();
                        else if(input.matches("show graveyard")) showGraveyard();
                        else if(input.matches("card show selected") ||
                                input.matches("show selected card") ||
                                input.matches("show card selected") ||
                                input.matches("card selected show")){
                            showSelectedCard();
                        } else {
                            System.out.println("invalid command");
                        }
                    }
                }
            }
        }
    }

    private void setWinnerByCheatCode(Matcher matcher){
        String nickname = matcher.group(1);
        for(int i = 0; i < 2; i++) {
            if (nickname.equals(gameController.users[i].nickname)){
                gameController.winners[gameController.currentRound - 1] = i;
                gameController.isDuelEnded = true;
                return;
            }
        }
        System.out.println("there is no such nickname");
    }

    private void surrender() {
        gameController.winners[gameController.currentRound - 1] = 1 - gameController.currentPlayer;
        gameController.isDuelEnded = true;
    }


    public void select(Matcher inputMatcher){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.select(inputMatcher);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void summon(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.summon();
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }


    public void set(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.set();
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }


    public void changePosition(String position){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.changePosition(position);
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }


    public void flipSummon(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.flipSummon();
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }


    public void attack(String number){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.attack(number);
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }

    public void directAttack(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.directAttack();
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }


    public void activateSpell(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.activateSpell();
            System.out.println(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
        printBoards();
    }

    public void ritualSummon(Card  ritualSpell){
        Board board = gameController.getBoard(gameController.getCurrentPlayer());
        if(!AdvancedRitualArt.canEffectBeActivatedForCard(gameController, ritualSpell, null)) return;
        while(true){
            if(!(gameController.getSelectedCard().isMonster() &&
                    gameController.getSelectedCard().getCardType().equals("Ritual") &&
                    AdvancedRitualArt.canRitualSummonWithMonster(gameController.getSelectedCard(), board))){
                System.out.println("please select a new ritual monster card");
                String input = scanner.nextLine();
                Matcher matcher = getCommandMatcher(input, "select ([a-z0-9 -]+)");
                if(matcher.matches()) select(matcher);
                continue;
            }
            System.out.println("you should ritual summon right now");
            String input = scanner.nextLine();
            if(!input.matches("summon")) continue;

            Card ritualMonster = gameController.getSelectedCard();
            ArrayList<Card> monsters;
            while(true) {
                monsters = GameView.getCardsByAddressFromZone(board, 1, "some");
                if(!AdvancedRitualArt.isSumOfLevelsOfMonstersInArrayEqualToLevelOfRitualMonster(monsters, ritualMonster)){
                    System.out.println("selected monsters levels dont match with ritual monster");
                } else {
                    ritualSpell.setChosenRitualMonsterForRitualSpell(ritualMonster);
                    for(Card card : monsters) {
                        CardController.moveCardToGraveyard(board, card);
                    }
                    break;
                }
            }
            break;
        }
        Action.runActionForCard(gameController, ritualSpell, null);
        System.out.println("summoned successfully");
    }


    public void showGraveyard(int player){
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/graveyard.fxml"));
            ArrayList<Card> graveyard = gameController.getBoard(player).getGraveyard();
            setCardsInGraveyard(pane, graveyard);
            GameStages.showNewStage(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCardsInGraveyard(Pane pane, ArrayList<Card> graveyard){
        double x = 200.0;
        double[] y = new double[]{50, 135, 220, 305, 390};
        double distance = 36.0;
        double cardWidth = 45.0;
        double cardHeight = cardWidth / CardLocation.cardRatioOfWidthToHeight;
        for(Card card: graveyard){
            ImageView cardImage = new Card(card.getName()).imageView;
            CardImageView cardImageView = new CardImageView(cardImage.getImage(), card);
            cardImageView.setFitWidth(cardWidth);
            cardImageView.setFitHeight(cardHeight);
            int index = graveyard.indexOf(card);
            cardImageView.setLayoutX(x + distance * (index % 16));
            cardImageView.setLayoutY(y[index / 16]);
            setEventHandlerForCard(pane, cardImageView);
            pane.getChildren().add(cardImageView);
        }
    }

    private void setEventHandlerForCard(Pane pane, CardImageView cardImageView) {
        ImageView imageView = (ImageView) pane.getChildren().get(0);
        Label cardLabel = (Label) pane.getChildren().get(1);
        cardImageView.setOnMouseEntered(mouseEvent -> {
            imageView.setImage(cardImageView.getImage());
            Card card = cardImageView.card;
            String text = card.getName() + "\n";
            if(card.isMonster()) text += ("ATK:" + card.getAttack() + " DEF:" + card.getDefense() + "\n");
            text += "\n";
            text += card.getDescription();
            cardLabel.setText(text);
        });
    }

    public void showGraveyard(){
        ArrayList<Card> graveyard = gameController.getBoard(gameController.getCurrentPlayer()).getGraveyard();
        if(graveyard.size() == 0) {
            System.out.println("graveyard is empty");
        } else {
            for(Card card : graveyard){
                System.out.println(card.getName() + ": " + card.getDescription());
            }
        }
        while(!(scanner.nextLine()).equals("back")){
            System.out.println("invalid command");
        }
    }

    public void showSelectedCard(){
        if(gameController.getSelectedCard() == null){
            System.out.println("no card is selected yet");
            return;
        }
        Card card = gameController.getSelectedCard();
        if(CardController.boardContainsCard(gameController.getBoard(gameController.getCurrentPlayer() + 1), card) &&
        card.getMode().contains("H")){
            System.out.println("card is not visible");
            return;
        }
        System.out.println("Name: " + card.getName());
        System.out.println("Level: " + card.getLevel());
        if(card.isMonster()){
            System.out.println("Type: " + card.getMonsterType());
            System.out.println("ATK: " + card.getAttack());
            System.out.println("DEF: " + card.getDefense());
        } else {
            String[] types = card.getType().split("_");
            System.out.println(types[0]);
            System.out.println("Type: " + types[1]);
        }
        System.out.println("Description: " + card.getDescription());
    }

    public void printBoards(){
        System.out.println();
        Board myBoard = gameController.getBoard(gameController.currentPlayer);
        Board opponentBoard = gameController.getBoard(gameController.currentPlayer + 1);
        ArrayList<Card> cards;
        int handSize;
        System.out.println(gameController.users[1 - gameController.currentPlayer].getNickname() + ": " + gameController.lp[1 - gameController.currentPlayer]);
        handSize = opponentBoard.getCardsInHand().stream().filter(Objects::nonNull).collect(Collectors.toList()).size();
        String s = "  ";
        System.out.print("  " + s);
        for(int i = 0; i < handSize; i++){
            System.out.print("c " + s);
        }
        System.out.println();
        System.out.println(opponentBoard.getDeckZone().size());
        System.out.print("  " + s);
        cards = opponentBoard.getSpellsAndTraps();
        if(cards.get(3) != null) System.out.print(cards.get(3).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(1) != null) System.out.print(cards.get(1).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(0) != null) System.out.print(cards.get(0).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(2) != null) System.out.print(cards.get(2).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(4) != null) System.out.print(cards.get(4).getMode() + " " + s);
        else System.out.print("E " + s);
        System.out.println();
        System.out.print("  " + s);
        cards = opponentBoard.getMonsters();
        if(cards.get(3) != null) System.out.print(cards.get(3).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(1) != null) System.out.print(cards.get(1).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(0) != null) System.out.print(cards.get(0).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(2) != null) System.out.print(cards.get(2).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(4) != null) System.out.print(cards.get(4).getMode() + s);
        else System.out.print("E " + s);
        System.out.println();
        System.out.print(opponentBoard.getGraveyard().size());
        System.out.print("                       ");
        if(opponentBoard.getFieldZone().get(0) == null) System.out.print("E");
        else System.out.print("O");
        System.out.println();
        System.out.println();
        System.out.println("-----------------------------");
        System.out.println();
        if(myBoard.getFieldZone().get(0) == null) System.out.print("E");
        else System.out.print("O");
        System.out.print("                       ");
        System.out.print(myBoard.getGraveyard().size());
        System.out.println();
        System.out.print("  " + s);
        cards = myBoard.getMonsters();
        if(cards.get(4) != null) System.out.print(cards.get(4).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(2) != null) System.out.print(cards.get(2).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(0) != null) System.out.print(cards.get(0).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(1) != null) System.out.print(cards.get(1).getMode() + s);
        else System.out.print("E " + s);
        if(cards.get(3) != null) System.out.print(cards.get(3).getMode() + s);
        else System.out.print("E " + s);
        System.out.println();
        System.out.print("  " + s);
        cards = myBoard.getSpellsAndTraps();
        if(cards.get(4) != null) System.out.print(cards.get(4).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(2) != null) System.out.print(cards.get(2).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(0) != null) System.out.print(cards.get(0).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(1) != null) System.out.print(cards.get(1).getMode() + " " + s);
        else System.out.print("E " + s);
        if(cards.get(3) != null) System.out.print(cards.get(3).getMode() + " " + s);
        else System.out.print("E " + s);
        System.out.println();
        System.out.print("                        ");
        System.out.print(myBoard.getDeckZone().size());
        System.out.println();
        handSize = myBoard.getCardsInHand().stream().filter(Objects::nonNull).collect(Collectors.toList()).size();
        System.out.print("  " + s);
        for(int i = 0; i < handSize; i++){
            System.out.print("c " + s);
        }
        System.out.println();
        System.out.println(gameController.users[gameController.currentPlayer].getNickname() + ": " + gameController.lp[gameController.currentPlayer]);
        System.out.println();
    }


    public Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }
}

