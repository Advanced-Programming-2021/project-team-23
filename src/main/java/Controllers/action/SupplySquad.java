package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class SupplySquad extends Action{

    // call whenever one of my monsters destroys

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        Card card = myBoard.getCardByPlace("4");
        if(card != null) {
            CardController.addCardToHandFromDeck(myBoard, card);
            myCard.setIsEffectUsedInTurn(true);
        }
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return !myCard.isEffectUsedInTurn();
    }
}
