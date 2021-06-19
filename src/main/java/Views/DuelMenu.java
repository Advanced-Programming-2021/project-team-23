package Views;

import Controllers.CardController;
import Controllers.CommandController;
import Controllers.GameController;
import Controllers.action.Action;
import Controllers.action.AdvancedRitualArt;
import Models.Board;
import Models.Card;
import Models.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class DuelMenu{

    public GameController gameController;

    public DuelMenu(GameController gameController){
        this.gameController = gameController;
    }

    public static Scanner scanner = new Scanner(System.in);

    public static void printPhaseName(int newPhaseNumber){
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
        while((!(input = scanner.nextLine()).equals("next phase")) && (!gameController.isDuelEnded())){
            matcher = getCommandMatcher(input, "select ([a-z0-9 -]+)");
            if(matcher.matches()) select(matcher);
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

    private void surrender() {
        gameController.winners[gameController.currentRound - 1] = 1 - gameController.currentPlayer;
        gameController.printWinnerOfDuel();
        if(gameController.isGameEnded()){
            if(gameController.numberOfRounds == 3) gameController.printWinnerOfMatch();
            gameController.setAwards();
        }
    }


    public void select(Matcher inputMatcher){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.select(inputMatcher);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void summon(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.summon();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void set(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.set();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }

    public void setMonster(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.setMonster();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }

    public void setSpellOrTrap(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.setSpellOrTrap();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void changePosition(String position){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.changePosition(position);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void flipSummon(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.flipSummon();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void attack(String number){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.attack(number);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }

    public void directAttack(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.directAttack();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }


    public void activateSpell(){
        CommandController commandController = new CommandController(gameController);
        try {
            String message = commandController.activateSpell();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printBoards();
    }

    public void ritualSummon(Card  ritualSpell){
        Board board = gameController.getBoard(gameController.getCurrentPlayer());
        if(!AdvancedRitualArt.canEffectBeActivatedForCard(gameController, ritualSpell, null)) return;
        while(true){
            if(!(gameController.getSelectedCard().getType().startsWith("Monster") &&
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
        if(card.getType().startsWith("Monster")){
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
        Board myBoard = gameController.getBoard(gameController.currentPlayer);
        Board opponentBoard = gameController.getBoard(gameController.currentPlayer + 1);
        ArrayList<Card> cards;
        int handSize;
        System.out.println(gameController.users[1 - gameController.currentPlayer].getNickname() + ": " + gameController.lp[1 - gameController.currentPlayer]);
        handSize = opponentBoard.getCardsInHand().size();
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
        System.out.println("--------------------------------------");
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
        handSize = myBoard.getCardsInHand().size();
        System.out.print("  " + s);
        for(int i = 0; i < handSize; i++){
            System.out.print("c " + s);
        }
        System.out.println();
        System.out.println(gameController.users[gameController.currentPlayer].getNickname() + ": " + gameController.lp[gameController.currentPlayer]);
    }



    public Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }
}

