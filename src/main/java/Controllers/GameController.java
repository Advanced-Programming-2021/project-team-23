package Controllers;

import Controllers.action.Action;
import Models.Board;
import Models.Card;
import Models.User;
import Views.DuelMenu;
import Views.GameView;

import java.util.ArrayList;

public class GameController {

    public int numberOfRounds; // 1 or 3
    public int currentRound;  // 1 or 2 or 3
    public int[] lpOfWinners;   // add number after each round
    public int[] winners;  // contains 0 or 1 as playerNumber // add number after each round
    private int winner; // 0 or 1 // winner of whole match

    public int phaseNumber;  // 1 to 6

    public User[] users = new User[2];
    public Board[] boards = new Board[2];
    public int[] lp = new int[2];

    public int currentPlayer;  // 0 or 1
    public Card selectedCard;
    private boolean isCardSummonedOrSetInTurn;

    private boolean isDamageToPlayersCalculated; // check this before calculating // reset to false after each use

    public DuelMenu duelMenu;

    public ArrayList<Card[]> chain; // each Card[] is a pair of "card" and "opponentCard"
    public Card[] lastCards; // size == 2  (for 2 players)
    public String[] lastActions; // size == 2  (for 2 players)
    // summon, activateSpell, changePosition, flipSummon, attack

    // set isEffectActive true after "any kind of activation" of a spell or trap
    // call spells and traps in board after any attack or after any destroy
    // whenever one of my monsters destroys, call some cards in board like supply squad actions
    // call spellAbsorptions in boards whenever an action of any spell is called

    public boolean isAI;  // set before calling runGameController
    public AI ai;

    public void runGameController(User user1, User user2, int numberOfRounds){
        if(isAI) ai = new AI();
        this.numberOfRounds = numberOfRounds;
        users[0] = user1;
        users[1] = user2;
        lpOfWinners = new int[]{-1, -1, -1};
        winners = new int[]{-1, -1, -1};
        duelMenu = new DuelMenu();
        for(int i = 1; i <= numberOfRounds; i++) {
            boards[0] = new Board(user1);
            boards[1] = new Board(user2);
            lp[0] = 8000;
            lp[1] = 8000;
            currentRound = i;
            currentPlayer = 0;
            isDamageToPlayersCalculated = false;
            phaseNumber = 1;
            if(!isAI) {
                phaseController(1);
                if (isGameEnded()) {
                    if (numberOfRounds == 3) printWinnerOfMatch();
                    setAwards();
                    return;
                }
            } else {
                while(true){
                    for(int j = 0; j < 2; j++){
                        if(currentPlayer == 0) phaseController(1);
                        else ai.playAITurn(1);
                        if (isGameEnded()) {
                            if (numberOfRounds == 3) printWinnerOfMatch();
                            setAwards();
                            return;
                        }
                    }
                }
            }
        }
    }


    public void phaseController(int newPhaseNumber){
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
                duelMenu.getCommands();
                phaseNumber++;
            }
        }
        if(newPhaseNumber == 2){
            callSomeCardsInStandbyPhase();
            duelMenu.getCommands();
            phaseNumber++;
        }
        if(newPhaseNumber == 3){
            //print board (also after each action)
            duelMenu.printBoards();
            duelMenu.getCommands();
            phaseNumber++;
        }
        if(newPhaseNumber == 4){
            //print board (also after each action)
            duelMenu.printBoards();
            duelMenu.getCommands();
            phaseNumber++;
        }
        if(newPhaseNumber == 5){
            //print board (also after each action)
            duelMenu.printBoards();
            duelMenu.getCommands();
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
        if(isAI && phaseNumber == 1) return;
        phaseController(phaseNumber);
    }

    public void callSomeCardsInStandbyPhase(){
        // some cards in boards should be called like scanners of both players and MessengerOfPeace
        for(int i = 0; i < 2; i++){
            for(Card card: getBoard(i).getMonsters()){
                if(card.getName().equals("Scanner")){
                    Action.runActionForCard(this, card, null);
                }
            }
        }

        for(Card card: getBoard(currentPlayer).getSpellsAndTraps()){
            if(card.getName().equals("Messenger of peace")) {
                Action.runActionForCard(this, card, null);
            }
        }
    }

    public void resetSomeVariablesInEndOfTheTurn(){
        selectedCard = null;
        isCardSummonedOrSetInTurn = false;
        for(int i = 0; i < 2; i++){
            ArrayList<Card> arrayList = null;
            for(int j = 0; j < 4; j++) {
                if(j == 0) arrayList = getBoard(i).getMonsters();
                if(j == 1) arrayList = getBoard(i).getSpellsAndTraps();
                if(j == 2) arrayList = getBoard(i).getGraveyard();
                if(j == 3) arrayList = getBoard(i).getFieldZone();
                for (Card card : arrayList) {
                    card.setIsEffectUsedInTurn(false);
                    card.setAttackedInTurn(false);
                    card.setPositionChangedInTurn(false);
                }
            }
        }
        for(Card card: getBoard(currentPlayer).getSpellsAndTraps()){
            if(card.isEffectActive()) {
                card.increaseNumberOfTurnsOfOpponentBeingActive();
            }
        }
        for(Card card: getBoard(currentPlayer).getGraveyard()){
            card.setNumberOfTurnsOfOpponentBeingActive(0);
        }

        for(Card card: getBoard(currentPlayer).getSpellsAndTraps()){
            if(card.getName().equals("Swords of Revealing Light")) {
                Action.runActionForCard(this, card, null);
            }
        }

        for(Card card: getBoard(currentPlayer).getSpellsAndTraps()){
            if(card.getName().equals("Change of Heart")) {
                Action.runActionForCard(this, card, null);
            }
        }
        for(Card card: getBoard(1 - currentPlayer).getSpellsAndTraps()){
            if(card.getName().equals("Change of Heart")) {
                Action.runActionForCard(this, card, null);
            }
        }
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCardSummonedOrSetInTurn() {
        return isCardSummonedOrSetInTurn;
    }

    public void setCardSummonedOrSetInTurn(boolean cardSummonedOrSetInTurn) {
        isCardSummonedOrSetInTurn = cardSummonedOrSetInTurn;
    }

    public Board getBoard(int playerNumber){
        // playerNumber will be 0 or 1
        playerNumber %=2;
        return boards[playerNumber];
    }

    public User getUser(int playerNumber){
        // playerNumber will be 0 or 1
        playerNumber %=2;
        return users[playerNumber];
    }

    public int getLp(int playerNumber){
        // playerNumber will be 0 or 1
        playerNumber %=2;
        return lp[playerNumber];
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public boolean isDamageToPlayersCalculated() {
        return isDamageToPlayersCalculated;
    }

    public void setIsDamageToPlayersCalculated(boolean damageToPlayersCalculated) {
        isDamageToPlayersCalculated = damageToPlayersCalculated;
    }

    public void increaseLp(int playerNumber, int amount){
        // playerNumber will be 0 or 1
        playerNumber %=2;
        if(playerNumber == 0) lp[0] += amount;
        if(playerNumber == 1) lp[1] += amount;
    }


    public boolean isDuelEnded(){
        for(int i = 0; i < 2; i++) {
            if (getLp(i) <= 0) {
                lpOfWinners[currentRound - 1] = getLp(1 - i);
                winners[currentRound - 1] = 1 - i;
                return true;
            }
        }
        return false;
    }

    public void printWinnerOfDuel(){
        int winner = winners[currentRound - 1];
        System.out.println(getUser(winner).getUsername() + " won the game and the score is: " +
                getUser(0).getScore() + "-" + getUser(1).getScore());
    }

    public boolean isGameEnded(){
        if(currentRound == 1) {
            if(numberOfRounds == 1){
                winner = winners[0];
                return true;
            } else {
                return false;
            }
        }
        else if(currentRound == 2) {
            if(winners[0] != winners[1]) return false;
            else winner = winners[0];
            return true;
        } else {
            if(winners[0] == winners[1] || winners[0] == winners[2]){
                winner = winners[0];
            } else {
                winner = 1 - winners[0];
            }
            return true;
        }
    }

    public void printWinnerOfMatch(){
        System.out.println(getUser(winner).getUsername() + " won the whole match with score: " +
                getUser(0).getScore() + "-" + getUser(1).getScore());
    }

    public void setAwards(){
        if(numberOfRounds == 1){
            User won = getUser(winner);
            won.setScore(won.getScore() + 1000);
            won.setWallet(won.getWallet() + 1000 + getLp(winner));
        } else {
            int maxLpOfWinner = 0;
            for(int i = 0; i < 3; i++){
                if(winners[i] == winner && lpOfWinners[i] > maxLpOfWinner){
                    maxLpOfWinner = lpOfWinners[i];
                }
            }
            User won = getUser(winner);
            won.setScore(won.getScore() + 3000);
            won.setWallet(won.getWallet() + 3000 + 3 * maxLpOfWinner);
        }
    }


    public void createAndRunChain(){
        boolean[] isPlayerDone = new boolean[]{false, false};
        label:
        for(int i = 0; i >= 0; i++){
            if(isPlayerDone[0] && isPlayerDone[1]){
                runChain();
                return;
            }
            currentPlayer = (currentPlayer + 1) % 2;
            duelMenu.printCurrentPlayerTurn();
            duelMenu.printBoards();
            for(int j = 0; j < 2; j++) {
                ArrayList<Card> arrayList;
                if(j == 0) arrayList = getBoard(currentPlayer + i).getMonsters();
                else arrayList = getBoard(currentPlayer + i).getSpellsAndTraps();
                for (Card card : arrayList) {
                    if ((card.getSpeed() > 1 && (lastCards[(currentPlayer + i + 1) % 2] == null || card.getSpeed() >= lastCards[(currentPlayer + i + 1) % 2].getSpeed())) &&
                            Action.canEffectBeActivatedForCard(this, card, lastCards[(currentPlayer + i + 1) % 2])) {
                        if (isAI || GameView.doesUserWantToUseEffectOfCard(card)) {
                            isPlayerDone[(currentPlayer + i + 1) % 2] = false;
                            chain.add(new Card[]{card, lastCards[(currentPlayer + i + 1) % 2]});
                            lastCards[(currentPlayer + i) % 2] = card;
                            currentPlayer = (currentPlayer + 1) % 2;
                            continue label;
                        }
                    }
                }
            }
            currentPlayer = (currentPlayer + 1) % 2;
            isPlayerDone[(currentPlayer + i + 1) % 2] = true;
        }
    }

    public void runChain(){
        int size = chain.size();
        for(int i = size - 1; i >= 0; i--){
            Action.runActionForCard(this, chain.get(i)[0], chain.get(i)[1]);
        }
    }


}
