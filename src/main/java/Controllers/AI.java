package Controllers;

import Models.Card;
import Models.Deck;
import Models.User;
import Views.DuelMenu;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class AI{

    public GameController gameController;

    public AI(GameController gameController){
        this.gameController = gameController;
    }

    public void playAITurn(int newPhaseNumber){
        DuelMenu.printPhaseName(newPhaseNumber);
        if(newPhaseNumber == 1){
            if(gameController.boards[gameController.currentPlayer].getDeckZone().size() == 0){
                gameController.lpOfWinners[gameController.currentRound - 1] = gameController.getLp(1 - gameController.currentPlayer);
                gameController.winners[gameController.currentRound - 1] = 1 - gameController.currentPlayer;
                gameController.printWinnerOfDuel();
                return;
            } else {
                if(gameController.boards[gameController.currentPlayer].canAnyCardBeChosenForThisBoardInDrawPhase()) {
                    Card card = gameController.boards[gameController.currentPlayer].getCardByPlace("4");
                    CardController.addCardToHandFromDeck(gameController.boards[gameController.currentPlayer], card);
                    DuelMenu.printSuccessfulAddingCardInDrawPhase(card);
                }
                gameController.duelMenu.printBoards();
                gameController.phaseNumber++;
            }
        }
        if(newPhaseNumber == 2){
            gameController.callSomeCardsInStandbyPhase();
            gameController.duelMenu.printBoards();
            gameController.phaseNumber++;
        }
        if(newPhaseNumber == 3){
            //print board (also after each action)
            setOrSummon();
            gameController.duelMenu.printBoards();
            gameController.phaseNumber++;
        }
        if(newPhaseNumber == 4){
            //print board (also after each action)
            attackCards();
            gameController.duelMenu.printBoards();
            gameController.phaseNumber++;
        }
        if(newPhaseNumber == 5){
            //print board (also after each action)
            setOrSummon();
            gameController.duelMenu.printBoards();
            gameController.phaseNumber++;
        }
        if(newPhaseNumber == 6){
            gameController.currentPlayer = 1 - gameController.currentPlayer;
            gameController.duelMenu.printCurrentPlayerTurn();
            gameController.resetSomeVariablesInEndOfTheTurn();
            gameController.phaseNumber = 1;
        }
        if (gameController.isDuelEnded()) {
            gameController.printWinnerOfDuel();
            return;
        }
        if(gameController.phaseNumber == 1) return;
        playAITurn(gameController.phaseNumber);
    }


    private void attackCards(){
        CommandController commandController = new CommandController(gameController);
        List<Card> monsters = gameController.boards[gameController.currentPlayer].getMonsters().stream().filter(Objects::nonNull).collect(Collectors.toList());
        if(monsters.size() == 0) return;
        sortMonsters(monsters);
        List<Card> opponentMonsters;
        for(int i = 0; i < 2; i++) {
            if(i == 0) opponentMonsters = gameController.boards[1 - gameController.currentPlayer].getMonsters().stream().filter(Objects::nonNull).filter(e -> !(e.getMode().contains("H"))).collect(Collectors.toList());
            else opponentMonsters = gameController.boards[1 - gameController.currentPlayer].getMonsters().stream().filter(Objects::nonNull).filter(e -> (e.getMode().contains("H"))).collect(Collectors.toList());
            for (Card monster : monsters) {
                gameController.selectedCard = monster;
                try { commandController.directAttack(); } catch (Exception e) { }
                for (Card opponentMonster : opponentMonsters) {
                    if (opponentMonster != null && ((opponentMonster.getMode().equals("OO") && monster.getAttack() > opponentMonster.getAttack()) ||
                            (opponentMonster.getMode().startsWith("D") && monster.getAttack() > opponentMonster.getDefense()))) {
                        String position = opponentMonster.getPlace().substring(2);
                        try {
                            commandController.attack(position);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private void sortMonsters(List<Card> monsters){
        for(int i = 0; i < monsters.size() - 1; i++){
            for(int j = i + 1; j < monsters.size(); j++){
                if(monsters.get(j).getAttack() > monsters.get(i).getAttack()){
                    Collections.swap(monsters, i, j);
                }
            }
        }
    }


    private void setOrSummon() {
        CommandController commandController = new CommandController(gameController);
        Card card = selectMonsterFromHand();
        if(card == null) return;
        if(gameController.selectedCard.getAttack() > gameController.selectedCard.getDefense() &&
                gameController.selectedCard.getAttack() >= 800){
            try { commandController.summon(); } catch (Exception e) { }
        } else {
            try { commandController.set(); } catch (Exception e) { }
        }
        selectSpellOrTrapFromHand();
        try { commandController.set(); } catch (Exception e) { }
    }

    private Card selectMonsterFromHand() {
        Card attackMonster = getHighestAttackMonster(gameController.boards[gameController.currentPlayer].getCardsInHand().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        Card defenseMonster = getHighestDefenseMonster(gameController.boards[gameController.currentPlayer].getCardsInHand().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        Card selected;
        if (attackMonster == null && defenseMonster == null) {
            return null;
        } else if (attackMonster == null) {
            selected = defenseMonster;
        } else if (defenseMonster == null) {
            selected = attackMonster;
        } else {
            if (attackMonster.getAttack() > defenseMonster.getDefense()) {
                selected = attackMonster;
            } else {
                selected = defenseMonster;
            }
        }
        gameController.selectedCard = selected;
        return selected;
    }

    private Card selectSpellOrTrapFromHand() {
        Card selected = null;
        for(Card card: gameController.boards[gameController.currentPlayer].getCardsInHand()){
            if(card != null && !card.isMonster()) selected = card;
        }
        gameController.selectedCard = selected;
        return selected;
    }


    public static Card getHighestAttackMonster(List<Card> arrayList){
        int maxAttack = 0;
        Card bestCard = null;
        for(Card card: arrayList){
            if(card != null && card.isMonster() && card.getAttack() > maxAttack){
                bestCard = card;
                maxAttack = card.getAttack();
            }
        }
        return bestCard;
    }

    public static Card getHighestDefenseMonster(List<Card> arrayList){
        int maxDefense = 0;
        Card bestCard = null;
        for(Card card: arrayList){
            if(card != null && card.isMonster() && card.getAttack() > maxDefense){
                bestCard = card;
                maxDefense = card.getAttack();
            }
        }
        return bestCard;
    }

    public static Card getLowestAttackMonster(ArrayList<Card> arrayList){
        if(!CardController.arrayListContainsMonster(arrayList)) return null;
        int minAttack = -1;
        Card bestCard = null;
        for(Card card: arrayList){
            if(card != null && card.isMonster() && (minAttack == -1 || card.getAttack() < minAttack)){
                bestCard = card;
                minAttack = card.getAttack();
            }
        }
        return bestCard;
    }


    public static User getAIUser() throws FileNotFoundException {
        User user = new User("AI", "AI", "AI");
        Deck deck = new Deck("AI deck");
        deck.isActive = true;
        for(int i = 0; i < 35; i++){
            deck.mainDeck.add(user.cardsInNoDeck.get(i));
        }
        for(int i = 50; i < 75; i++){
            deck.mainDeck.add(user.cardsInNoDeck.get(i));
        }
        for(int i = 35; i < 50; i++){
            deck.sideDeck.add(user.cardsInNoDeck.get(i));
        }
        user.listOfDecks.add(deck);
        user.activeDeck = deck;
        User.setUserInFile(user);
        return user;
    }

}
