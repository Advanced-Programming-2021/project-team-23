package Controllers.action;

import Controllers.GameController;
import Models.Card;

import java.util.ArrayList;

public class SpellAbsorption extends Action{

    // call whenever an action of any spell is called

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        gameController.increaseLp(myNumber, 500);
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return (gameController.lastActions[myNumber].equals("activateSpell") ||
                gameController.lastActions[1-myNumber].equals("activateSpell"))
                && (!myCard.getPlace().startsWith("hand"));
    }
}

