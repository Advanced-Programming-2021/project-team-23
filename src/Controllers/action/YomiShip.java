package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class YomiShip extends Action {

    // call whenever someone destroyed this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        CardController.moveCardToGraveyard(opponentBoard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return myCard.getPlace().startsWith("3");
    }
}

