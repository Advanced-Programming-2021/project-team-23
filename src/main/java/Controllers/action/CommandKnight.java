package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;
import java.util.ArrayList;

public class CommandKnight extends Action {

    // call right after each successful summon or after it moves to graveyard
    // call whenever someone want to attack this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        int amount = 0;
        if (myCard.getMode().endsWith("O") && (!myCard.isEffectActive())) {
            amount = 400;
            myCard.setIsEffectActive(true);
        }
        if (myCard.getPlace().startsWith("3") && (myCard.isEffectActive())) {
            amount = -400;
            myCard.setIsEffectActive(false);
        }
        Board[] boards = new Board[]{myBoard, opponentBoard};
        for (int i = 0; i < 2; i++) {
            boards[i] = increaseAttackOfCards(boards[i], amount);
        }
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        runFirstAction(gameController, myCard, opponentCard);
        if (myCard.getMode().endsWith("O") &&
                myBoard.getMonsters().size() > 1) {
            myCard.setCanAnyoneAttack(false);  // reset to true after use of "canAnyoneAttack"
        }
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return true;
    }


    public Board increaseAttackOfCards(Board board, int amount) {
        ArrayList<Card> monsters = increaseAttackInArrayList(board.getMonsters(), amount);
        board.setMonsters(monsters);
        ArrayList<Card> hand = increaseAttackInArrayList(board.getCardsInHand(), amount);
        board.setCardsInHand(hand);
        ArrayList<Card> deckZone = increaseAttackInArrayList(board.getDeckZone(), amount);
        board.setDeckZone(deckZone);
        ArrayList<Card> graveyard = increaseAttackInArrayList(board.getGraveyard(), amount);
        board.setGraveyard(graveyard);
        return board;
    }

    public ArrayList<Card> increaseAttackInArrayList(ArrayList<Card> array, int amount) {
        for (Card card : array) {
            if (card != null && card.isMonster()) card.increaseAttack(amount);
        }
        return array;
    }

}
