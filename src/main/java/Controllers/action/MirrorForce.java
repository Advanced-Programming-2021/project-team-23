package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;

import java.util.ArrayList;

public class MirrorForce extends Action{

    // call when opponent monster wanted to attack

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> opponentMonsters = opponentBoard.getMonsters();
        for(Card monster : opponentMonsters){
            if(monster != null && monster.getMode().startsWith("O")){
                CardController.moveCardToGraveyard(opponentBoard, monster);
            }
        }
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("attack") &&
                (!myCard.getPlace().startsWith("hand"));
    }
}
