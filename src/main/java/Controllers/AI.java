package Controllers;

import Models.Card;
import Models.Deck;
import Models.User;
import Views.DuelMenu;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AI extends GameController{

    public void playAITurn(int newPhaseNumber){
        DuelMenu.printPhaseName(newPhaseNumber);
        if(newPhaseNumber == 1){
            if(boards[currentPlayer].getDeckZone().size() == 0){
                lpOfWinners[currentRound - 1] = getLp(1 - currentPlayer);
                winners[currentRound - 1] = 1 - currentPlayer;
                printWinnerOfDuel();
                return;
            } else {
                if(boards[currentPlayer].canAnyCardBeChosenForThisBoardInDrawPhase()) {
                    Card card = boards[currentPlayer].getCardByPlace("4");
                    CardController.addCardToHandFromDeck(boards[currentPlayer], card);
                    DuelMenu.printSuccessfulAddingCardInDrawPhase(card);
                }
                phaseNumber++;
            }
        }
        if(newPhaseNumber == 2){
            callSomeCardsInStandbyPhase();
            phaseNumber++;
        }
        if(newPhaseNumber == 3){
            //print board (also after each action)
            duelMenu.printBoards();
            setOrSummon();
            phaseNumber++;
        }
        if(newPhaseNumber == 4){
            //print board (also after each action)
            duelMenu.printBoards();
            attackCards();
            phaseNumber++;
        }
        if(newPhaseNumber == 5){
            //print board (also after each action)
            duelMenu.printBoards();
            setOrSummon();
            phaseNumber++;
        }
        if(newPhaseNumber == 6){
            currentPlayer = 1 - currentPlayer;
            duelMenu.printCurrentPlayerTurn();
            resetSomeVariablesInEndOfTheTurn();
            phaseNumber = 1;
        }
        if (isDuelEnded()) {
            printWinnerOfDuel();
            return;
        }
        if(phaseNumber == 1) return;
        playAITurn(phaseNumber);
    }


    private void attackCards(){
        CommandController commandController = new CommandController();
        ArrayList<Card> monsters = boards[currentPlayer].getMonsters();
        sortMonsters(monsters);
        List<Card> opponentMonsters;
        for(int i = 0; i < 2; i++) {
            if(i == 0) opponentMonsters = boards[1 - currentPlayer].getMonsters().stream().filter(Objects::nonNull).filter(e -> !(e.getMode().contains("H"))).collect(Collectors.toList());
            else opponentMonsters = boards[1 - currentPlayer].getMonsters().stream().filter(Objects::nonNull).filter(e -> (e.getMode().contains("H"))).collect(Collectors.toList());
            for (Card monster : monsters) {
                for (Card opponentMonster : opponentMonsters) {
                    if ((opponentMonster.getMode().equals("OO") && monster.getAttack() > opponentMonster.getAttack()) ||
                            (opponentMonster.getMode().startsWith("D") && monster.getAttack() > opponentMonster.getDefense())) {
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

    private void sortMonsters(ArrayList<Card> monsters){
        for(int i = 0; i < monsters.size() - 1; i++){
            for(int j = i + 1; j < monsters.size(); j++){
                if(monsters.get(j).getAttack() > monsters.get(i).getAttack()){
                    Card tempI = monsters.get(i);
                    monsters.set(i, monsters.get(j));
                    monsters.set(j, tempI);
                }
            }
        }
    }


    private void setOrSummon() {
        CommandController commandController = new CommandController();
        selectMonsterFromHand();
        if(selectedCard.getAttack() > selectedCard.getDefense() &&
        selectedCard.getAttack() >= 800){
            try { commandController.summon(); } catch (Exception e) { }
        } else {
            try { commandController.set(); } catch (Exception e) { }
        }
        selectSpellOrTrapFromHand();
        try { commandController.set(); } catch (Exception e) { }
    }

    private Card selectMonsterFromHand() {
        Card attackMonster = getHighestAttackMonster(boards[currentPlayer].getCardsInHand());
        Card defenseMonster = getHighestDefenseMonster(boards[currentPlayer].getCardsInHand());
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
        selectedCard = selected;
        return selected;
    }

    private Card selectSpellOrTrapFromHand() {
        Card selected = null;
        for(Card card: boards[currentPlayer].getCardsInHand()){
            if(!card.getType().startsWith("Monster")) selected = card;
        }
        selectedCard = selected;
        return selected;
    }


    public static Card getHighestAttackMonster(ArrayList<Card> arrayList){
        int maxAttack = 0;
        Card bestCard = null;
        for(Card card: arrayList){
            if(card.getAttack() > maxAttack){
                bestCard = card;
                maxAttack = card.getAttack();
            }
        }
        return bestCard;
    }

    public static Card getHighestDefenseMonster(ArrayList<Card> arrayList){
        int maxDefense = 0;
        Card bestCard = null;
        for(Card card: arrayList){
            if(card.getAttack() > maxDefense){
                bestCard = card;
                maxDefense = card.getAttack();
            }
        }
        return bestCard;
    }

    public static Card getLowestAttackMonster(ArrayList<Card> arrayList){
        int minAttack = arrayList.get(0).getAttack();
        Card bestCard = arrayList.get(0);
        for(Card card: arrayList){
            if(card.getAttack() < minAttack){
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
