package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class MagicJammer extends Action{

    // call after any activation of a spell of opponent

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        int number = 1;
        //if(!gameController.users[myNumber].isAI()) number = GameView.getACardNumberInHandFromUser(myBoard);
        Card cardToBeDestroyed = myBoard.getCardsInHand().get(number - 1);
        CardController.moveCardToGraveyard(myBoard, cardToBeDestroyed);
        CardController.moveCardToGraveyard(opponentBoard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        if(CardController.cardsOfArrayListAreAllNull(cardsInHand)) return false;
        return gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("activateSpell") &&
                (!myCard.getPlace().startsWith("hand"));
    }
}
