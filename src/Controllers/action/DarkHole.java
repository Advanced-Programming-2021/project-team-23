package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;

public class DarkHole extends Action {

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        CardController.destroyAllMonstersInBoard(myBoard);
        CardController.destroyAllMonstersInBoard(opponentBoard);
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
