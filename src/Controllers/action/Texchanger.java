package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class Texchanger extends Action {

    // call whenever someone want to attack this card


    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {

    }

    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        if(myCard.isEffectUsedInTurn()) return;

        myCard.setCanAnyoneAttack(false);  // reset to true after use of "canAnyoneAttack"

        String zone = "3";

        while(true){
            //if(!gameController.users[myNumber].isAI()) zone = GameView.getZoneNumber("3 or 4 or hand");
            Card monster = getNormalMonsterOfType(myBoard, zone, "Cyberse");
            if(monster == null){
                if(!gameController.users[myNumber].isAI()) GameView.printThatThereIsNoMonsterWithType("Cyberse");
                else{
                    if(zone.equals("3")) zone = "4";
                    else if(zone.equals("4")) zone = "hand";
                    else break;
                }
            } else {
                ArrayList<Card> arrayList = new ArrayList<>();
                if(zone.equals("3")) {
                    arrayList = myBoard.getGraveyard();
                    arrayList.remove(monster);
                }
                if(zone.equals("4")) {
                    arrayList = myBoard.getDeckZone();
                    arrayList.remove(monster);
                }
                if(zone.equals("hand")) arrayList = myBoard.getCardsInHand();

                CardController.moveCardFromFirstArrayToSecondArray(monster, arrayList, myBoard.getMonsters(), "1");
                monster.setMode("OO");
                break;
            }
        }

        myCard.setIsEffectUsedInTurn(true);
    }



    private boolean boardContainsNormalMonsterOfType(Board board, String monsterType){
        return (getNormalMonsterOfType(board, "hand", monsterType) != null
        || getNormalMonsterOfType(board, "4", monsterType) != null
        || getNormalMonsterOfType(board, "3", monsterType) != null);
    }

    private Card getNormalMonsterOfType(Board board, String zone, String monsterType){
        ArrayList<Card> arrayList = new ArrayList<>();
        if(zone.equals("3")) arrayList = board.getGraveyard();
        if(zone.equals("4")) arrayList = board.getDeckZone();
        if(zone.equals("hand")) arrayList = board.getCardsInHand();
        for(Card card : arrayList){
            if(card != null &&
                    card.isMonster() &&
                    card.getMonsterType().equals(monsterType) &&
                    card.isNormalMonster()){
                return card;
            }
        }
        return null;
    }


    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);

        if(!((!CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5))
                && boardContainsNormalMonsterOfType(myBoard, "Cyberse"))) {
            return false;
        }

        return gameController.lastActions[1-myNumber] != null &&
                gameController.lastActions[1-myNumber].equals("attack");
    }
}

