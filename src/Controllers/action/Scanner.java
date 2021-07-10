package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class Scanner extends Action{

    // call in start of any turn or before any kind of summon request


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        if(!myCard.isEffectUsedInTurn()) {
            ArrayList<Card> graveyard = opponentBoard.getGraveyard();
            if (!CardController.arrayListContainsMonster(graveyard)) {
                myCard.setAttack(0);
                myCard.setDefense(0);
                return;
            }

            Card monster = null;
            if(!gameController.getBoard(gameController.getCurrentPlayer()).getMonsters().contains(myCard)) {
                //if(gameController.users[myNumber].isAI()) {
                    monster = CardController.getAMonsterFromGraveyard(opponentBoard);
                //}
                //else monster = GameView.getMonstersFromGraveyard(opponentBoard, 1).get(0);
            }
            else{
                for(Card aCard : graveyard){
                    if(aCard != null && aCard.isMonster()){
                        monster = aCard;
                    }
                }
            }

            myCard.setName(monster.getName());
            myCard.setMonsterType(monster.getMonsterType());
            myCard.setCardType(monster.getCardType());
            myCard.setAttack(monster.getAttack());
            myCard.setDefense(monster.getDefense());
            //set other properties

            myCard.setIsEffectUsedInTurn(true);
        }
    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }
}

