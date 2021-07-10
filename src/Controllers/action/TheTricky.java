package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TheTricky extends Action {

    // call after summon request for this card from hand


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        CardController.moveCardFromFirstArrayToSecondArray(myCard, myBoard.getCardsInHand(), myBoard.getMonsters(), "1");

        myCard.setNumberOfTributesNeeded(0);

        int number = 1;
        //if(!gameController.users[myNumber].isAI()) number = GameView.getACardNumberInHandFromUser(myBoard);
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        Card cardToBeRemoved = cardsInHand.get(number - 1);
        CardController.moveCardToGraveyard(myBoard, cardToBeRemoved);
        myBoard.setCardsInHand(cardsInHand);
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return myCard.getPlace().startsWith("hand") &&
                myBoard.getCardsInHand().stream().filter(Objects::nonNull).collect(Collectors.toList()).size() > 1 &&
                gameController.lastCards[myNumber] == myCard &&
                gameController.lastActions[myNumber] != null &&
                gameController.lastActions[myNumber].equals("summon") &&
                opponentCard == null;
    }
}

