package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class HeraldOfCreation extends Action{

    // call anytime


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        if(!gameController.isAI && !GameView.doesUserWantToUseEffectOfCard(myCard)) return;

        Card card = getACardFromGraveyardWithLeastLevel(myBoard, 7);

        int number = 1;
        if(!gameController.isAI) number = GameView.getACardNumberInHandFromUser(myBoard);
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        Card cardToBeRemoved = cardsInHand.get(number - 1);
        CardController.moveCardToGraveyard(myBoard, cardToBeRemoved);

        cardsInHand.set(number - 1, card);
        myBoard.setCardsInHand(cardsInHand);

        ArrayList<Card> graveyard = myBoard.getGraveyard();
        graveyard.remove(card);
        myBoard.setGraveyard(graveyard);

        myCard.setIsEffectUsedInTurn(true);
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if((!myCard.getPlace().startsWith("1")) ||
                myCard.isEffectUsedInTurn() ||
                myBoard.getCardsInHand().size() == 0 ||
                getACardFromGraveyardWithLeastLevel(myBoard, 7) == null) return false;
        if(!(gameController.lastCards[myNumber] == myCard && (!myCard.getPlace().startsWith("hand")) && gameController.lastActions[myNumber] != null &&
                (gameController.lastActions[myNumber].equals("summon")||gameController.lastActions[myNumber].equals("flipSummon")))) return false;
        for(Card card : myBoard.getGraveyard()){
            if(card != null && card.isMonster() && card.getLevel() > 6 && opponentCard == null) return true;
        }
        return false;
    }


    public Card getACardFromGraveyardWithLeastLevel(Board board, int level){
        ArrayList<Card> graveyard = board.getGraveyard();
        for(Card card : graveyard){
            if(card.getLevel() >= level) return card;
        }
        return null;
    }
}

