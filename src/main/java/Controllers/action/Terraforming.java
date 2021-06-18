package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class Terraforming extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(canEffectBeActivated(gameController, myCard, opponentCard)) {
            ArrayList<Card> deckZone = myBoard.getDeckZone();
            for(Card card : deckZone){
                if(card.getType().startsWith("Spell_Field")) {
                    CardController.addCardToHandFromDeck(myBoard, card);
                }
            }
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        if(CardController.arrayListOfCardsIsFull(cardsInHand, 6)) return false;
        ArrayList<Card> deckZone = myBoard.getDeckZone();
        for(Card card : deckZone){
            if(card.getType().startsWith("Spell_Field")) return true;
        }
        return false;
    }
}
