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
        ArrayList<Card> deckZone = myBoard.getDeckZone();
        Card cardToAdd = null;
        for(Card card : deckZone){
            if(card != null && card.getType().startsWith("Spell_Field")) {
                cardToAdd = card;
                break;
            }
        }
        if(cardToAdd != null) CardController.addCardToHandFromDeck(myBoard, cardToAdd);
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        if(CardController.arrayListOfCardsIsFull(cardsInHand, 6)) return false;
        ArrayList<Card> deckZone = myBoard.getDeckZone();
        for(Card card : deckZone){
            if(card != null && card.getType().startsWith("Spell_Field")) return true;
        }
        return opponentCard == null;
    }
}
