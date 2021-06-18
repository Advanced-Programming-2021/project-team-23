package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;
import java.util.ArrayList;

public class  Terratiger extends Action{

    // call after normal summon of this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        for(Card card : cardsInHand){
            if(card.getType().startsWith("Monster") && card.getLevel() <= 4){
                CardController.moveCardFromFirstArrayToSecondArray(card, cardsInHand, myBoard.getMonsters(), "1");
                card.setMode("DO");
            }
        }
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        if(CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5) ||
                (!suitableMonsterExistsInHand(myBoard))) {
            return false;
        }
        return gameController.lastCards[myNumber] == myCard &&
                gameController.lastActions[myNumber].equals("summon") &&
                (!myCard.getPlace().startsWith("hand")) &&
                opponentCard == null;
    }

    public boolean suitableMonsterExistsInHand(Board board){
        ArrayList<Card> cardsInHand = board.getCardsInHand();
        for(Card card : cardsInHand){
            if(card.getType().startsWith("Monster") && card.getLevel() <= 4){
                return true;
            }
        }
        return false;
    }
}
