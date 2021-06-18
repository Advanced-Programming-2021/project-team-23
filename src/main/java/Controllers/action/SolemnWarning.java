package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class SolemnWarning extends Action{

    // call after any successful request for summon or special summon

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        gameController.increaseLp(gameController.getCurrentPlayer(), -2000);
        CardController.moveCardToGraveyard(opponentBoard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastActions[1-myNumber].equals("summon");
    }
}
