package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class Suijin extends Action{

    // call whenever someone want to attack this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(myCard.getMode().endsWith("O") && myCard.getNumberOfTimesUsed() == 0){
            opponentCard.setAttack(0);
            myCard.increaseNumberOfTimesUsed();
        }
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("attack") &&
                opponentCard != null;
    }
}
