package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class BeastKingBarbaros extends Action {

    // call before any kind of summon request
    // also call right after each successful summon
    // effect of this card is destroying all opponent cards


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if((!myCard.getMode().startsWith("1"))) {
            if (gameController.isAI || GameView.doesUserWantToUseOptionalNumberForTributes(0)) {
                myCard.setNumberOfTributesNeeded(0);
                myCard.setAttack(1900);
            } else if (CardController.areThereEnoughCardsInMonsterZoneToTribute(myBoard, 3) &&
                    GameView.doesUserWantToUseOptionalNumberForTributes(3)) {
                myCard.setNumberOfTributesNeeded(3);
                CardController.destroyAllCardsInBoard(opponentBoard);
            }
        }
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastCards[myNumber] == myCard &&
                gameController.lastActions[myNumber] != null &&
                gameController.lastActions[myNumber].equals("summon") &&
                opponentCard == null;
    }
}

