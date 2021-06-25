package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class MindCrush extends Action {

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> opponentHand = opponentBoard.getCardsInHand();
        String cardName;
        if(!gameController.users[myNumber].isAI()) cardName = GameView.getACardNameFromUser();
        else cardName = opponentHand.get(0).getName();
        if (CardController.arrayListContainsCardWithName(opponentHand, cardName)) {
            destroyAllCardInArrayWithName(opponentHand, cardName, opponentBoard);
            destroyAllCardInArrayWithName(opponentBoard.getMonsters(), cardName, opponentBoard);
            destroyAllCardInArrayWithName(opponentBoard.getSpellsAndTraps(), cardName, opponentBoard);
        } else {
            Card randomCard = myBoard.getCardsInHand().get(0);
            CardController.moveCardToGraveyard(myBoard, randomCard);
        }
    }

    public void destroyAllCardInArrayWithName(ArrayList<Card> arrayList, String name, Board board) {
        for (Card card : arrayList) {
            if (card != null && card.getName().equals(name)) {
                CardController.moveCardToGraveyard(board, card);
            }
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return true;
    }
}
