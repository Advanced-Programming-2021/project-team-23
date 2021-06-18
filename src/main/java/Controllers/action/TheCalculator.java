package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class TheCalculator extends Action{

    // call right after each successful summon
    // also call whenever this card want to attack


    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        myCard.setAttack(300 * getSumOfLevelOfFaceUpCardsInMonsterZone(myBoard));
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }

    public int getSumOfLevelOfFaceUpCardsInMonsterZone(Board board){
        int sum = 0;
        ArrayList<Card> monsters = board.getMonsters();
        for(Card monster : monsters){
            if(monster.getMode().endsWith("O")) {
                sum += monster.getLevel();
            }
        }
        return sum;
    }
}
