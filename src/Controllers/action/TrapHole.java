package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class TrapHole extends Action{

    // call after opponent monster summon (normal or flip) if canEffectBeActivated was true

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        CardController.moveCardToGraveyard(opponentBoard, opponentCard);
    }


    // call when opponent monster summon (normal or flip)
    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return opponentCard != null &&
                opponentCard.getAttack() >= 1000 &&
                gameController.lastActions[1-myNumber] != null &&
                (gameController.lastActions[1-myNumber].equals("summon") || gameController.lastActions[1-myNumber].equals("flipSummon"))
                && (!myCard.getPlace().startsWith("hand"));
    }
}
