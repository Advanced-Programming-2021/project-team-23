package Controllers.action;

import Controllers.GameController;
import Models.Card;

public class TimeSeal extends Action{
    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        opponentBoard.setCanAnyCardBeChosenForThisBoardInDrawPhase(false);
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
