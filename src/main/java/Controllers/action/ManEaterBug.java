package Controllers.action;

import Controllers.AI;
import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public class ManEaterBug extends Action{

    // call after each successful Flip Summon


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(CardController.cardsOfArrayListAreAllNull(opponentBoard.getMonsters())) return;
        if(!gameController.isAI) CardController.getAndDestroyMonstersInBoard(opponentBoard, 1);
        else {
            Card opponentMonster = AI.getHighestAttackMonster(opponentBoard.getMonsters());
            CardController.moveCardToGraveyard(opponentBoard, opponentMonster);
        }
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastCards[myNumber] == myCard &&
                gameController.lastActions[myNumber].equals("flipSummon") &&
                (!myCard.getPlace().startsWith("hand")) &&
                opponentCard == null;
    }
}

