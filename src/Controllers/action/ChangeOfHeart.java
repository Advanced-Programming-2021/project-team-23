package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

public class ChangeOfHeart extends Action{

    // call when activated
    // also call when end phase started

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(!(gameController.getPhaseNumber() == 6)){
            Card monster;
            //if(gameController.users[myNumber].isAI()){
                monster = CardController.getSomeCardsFromZone(opponentBoard, 1, "1").get(0);
            //}
            //else monster = GameView.getCardsByAddressFromZone(opponentBoard, 1, "1").get(0);
            CardController.moveCardFromFirstArrayToSecondArray(monster,
                    opponentBoard.getMonsters(), myBoard.getMonsters(), "1");
            myCard.setTargetCard(monster);
        } else {
            CardController.moveCardFromFirstArrayToSecondArray(myCard.getTargetCard(),
                    myBoard.getMonsters(), opponentBoard.getMonsters(), "1");
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return !(CardController.cardsOfArrayListAreAllNull(opponentBoard.getMonsters())
                || CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5)) &&
                opponentCard == null;
    }
}
