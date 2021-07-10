package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class Marshmallon extends Action {

    // call after calculating damage of opponent attack to this card for opponent


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(gameController.isDamageToPlayersCalculated()){
            gameController.increaseLp(myNumber + 1, -1000);
        }
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return (!myCard.getPlace().startsWith("hand")) &&
                opponentCard != null &&
                myCard.getMode().contains("H");
    }
}

