package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;

import java.util.ArrayList;

public class SwordsOfRevealingLight extends Action{

    // call when activated
    // also call when turn of opponent finished but call increaseNumberOfTurnsOfOpponentBeingActive before calling this

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(myCard.getNumberOfTurnsOfOpponentBeingActive() == 0){
            myCard.setIsEffectActive(true);
            faceUpAllMonstersInBoard(opponentBoard);
            opponentBoard.setCanMonstersAttack(false);
        }
        if(myCard.getNumberOfTurnsOfOpponentBeingActive() == 3){
            opponentBoard.setCanMonstersAttack(true);
            myCard.setIsEffectActive(false);
            CardController.moveCardToGraveyard(myBoard, myCard);
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }


    public void faceUpAllMonstersInBoard(Board board){
        ArrayList<Card> monsters = board.getMonsters();
        for(Card monster : monsters){
            if(monster != null){
                String mode = monster.getMode();
                monster.setMode(mode.charAt(0) + "O");
            }
        }
    }
}
