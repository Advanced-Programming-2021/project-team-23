package Controllers.action;

import Controllers.GameController;
import Models.Card;

public class NegateAttack extends Action{

    // call when opponent monster wanted to attack

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        opponentCard.setCanThisCardAttack(false);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("attack") &&
                (!myCard.getPlace().startsWith("hand"));
    }
}
