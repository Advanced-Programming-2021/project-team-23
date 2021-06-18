package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class MysticalSpaceTyphoon extends Action {
    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> spellsAndTrapsToBeDestroyed = CardController.getSomeCardsFromZone(opponentBoard, 2, "1");
        if(!gameController.isAI) spellsAndTrapsToBeDestroyed = GameView.
                getCardsByAddressFromZone(opponentBoard, 2, "1");
        CardController.moveCardToGraveyard(opponentBoard, spellsAndTrapsToBeDestroyed.get(0));
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> opponentSpellsAndTraps = opponentBoard.getSpellsAndTraps();
        return !(CardController.cardsOfArrayListAreAllNull(opponentSpellsAndTraps));
    }
}
