package Controllers.action;

import Controllers.GameController;
import Models.Card;

public class RingOfDefense extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        opponentCard.setDamageToLpOfOpponent(0);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastCards[1 - myNumber].getType().startsWith("Trap");
    }
}

