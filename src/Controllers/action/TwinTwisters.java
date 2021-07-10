package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class TwinTwisters extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        int number = 1;
        //if(!gameController.users[myNumber].isAI()) number = GameView.getACardNumberInHandFromUser(myBoard);
        CardController.moveCardToGraveyard(myBoard, myBoard.getCardByPlace("hand_" + number));
        int numberOfSpellsOrTrapsToBeDestroyed = 1;
//        if(!gameController.users[myNumber].isAI()) numberOfSpellsOrTrapsToBeDestroyed = GameView.
//                howManyCardsDoesPlayerWantToDestroy(2);
        ArrayList<Card> spellsAndTrapsToBeDestroyed;
        spellsAndTrapsToBeDestroyed = CardController.
                getSomeCardsFromZone(opponentBoard, 2, String.valueOf(numberOfSpellsOrTrapsToBeDestroyed));
        //if(!gameController.users[myNumber].isAI()) spellsAndTrapsToBeDestroyed = GameView.getCardsByAddressFromZone(opponentBoard, 2, String.valueOf(numberOfSpellsOrTrapsToBeDestroyed));
        for(Card card : spellsAndTrapsToBeDestroyed){
            CardController.moveCardToGraveyard(opponentBoard, card);
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> opponentSpellsAndTraps = opponentBoard.getSpellsAndTraps();
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        return !(CardController.cardsOfArrayListAreAllNull(opponentSpellsAndTraps) ||
                CardController.cardsOfArrayListAreAllNull(cardsInHand));
    }
}
