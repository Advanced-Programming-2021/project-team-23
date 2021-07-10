package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class ExploderDragon extends Action {

    // call whenever someone destroyed this card before calculating damage of attack


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        CardController.moveCardToGraveyard(opponentBoard, opponentCard);
        if(myCard.getMode().startsWith("O")) gameController.increaseLp(myNumber, opponentCard.getAttack() - myCard.getAttack());
        if(myCard.getMode().startsWith("D")) gameController.increaseLp(myNumber, opponentCard.getAttack() - myCard.getDefense());
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return (myCard.getPlace().startsWith("3")) && opponentCard != null;
    }
}

