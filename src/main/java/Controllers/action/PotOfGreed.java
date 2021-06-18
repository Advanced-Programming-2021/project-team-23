package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class PotOfGreed extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        Card card;
        card = myBoard.getCardByPlace("4");
        CardController.addCardToHandFromDeck(myBoard, card);
        card = myBoard.getCardByPlace("4");
        CardController.addCardToHandFromDeck(myBoard, card);
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
