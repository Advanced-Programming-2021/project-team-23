package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class TorrentialTribute extends Action{

    // call when opponent monster summon

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        runActionForDefense(gameController, myCard, opponentCard);
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        CardController.destroyAllMonstersInBoard(myBoard);
        CardController.destroyAllMonstersInBoard(opponentBoard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return ((gameController.lastActions[1-myNumber].equals("summon") ||
                gameController.lastActions[myNumber].equals("summon")))
                && (!myCard.getPlace().startsWith("hand"));
    }
}

