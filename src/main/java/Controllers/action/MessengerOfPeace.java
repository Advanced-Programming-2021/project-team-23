package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

public class MessengerOfPeace extends Action {

    // call when activated
    // call when destroyed
    // call when my standby phase (phaseNumber == 2) started

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(gameController.getPhaseNumber() == 2){
            if(gameController.users[myNumber].isAI() || GameView.doesUserWantToDestroyHisCard(myCard)){
                CardController.moveCardToGraveyard(myBoard, myCard);
            } else {
                gameController.increaseLp(myNumber, -100);
            }
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(opponentCard.getAttack() >= 1500) opponentCard.setCanThisCardAttack(false);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard != null && opponentCard.isMonster();
    }
}
