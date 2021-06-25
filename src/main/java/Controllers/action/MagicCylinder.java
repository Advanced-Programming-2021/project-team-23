package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class MagicCylinder extends Action{

    // call when opponent monster wanted to attack

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        opponentCard.setCanThisCardAttack(false);
        if(myCard.getDamageToLpOfOpponent() == 0) return;
        gameController.increaseLp(1 - myNumber, -opponentCard.getAttack());
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return opponentCard != null &&
                gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("attack") &&
                (!myCard.getPlace().startsWith("hand"));
    }
}
