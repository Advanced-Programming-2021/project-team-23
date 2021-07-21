package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class MirageDragon extends Action{

    // call after any summon or any change in mode or place or when destroyed
    // call whenever anything happens about this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(myCard.getMode() != null) opponentBoard.setCanAnyTrapBeActivated(!myCard.getMode().endsWith("O"));
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }
}

