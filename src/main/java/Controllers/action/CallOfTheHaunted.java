package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class CallOfTheHaunted extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        ArrayList<Card> graveyard = myBoard.getGraveyard();
        ArrayList<Card> monsters = myBoard.getMonsters();
        Card monsterFromGraveyard;
        if(gameController.users[myNumber].isAI()) monsterFromGraveyard = CardController.getAMonsterFromGraveyard(myBoard);
        else monsterFromGraveyard = GameView.getMonstersFromGraveyard(myBoard, 1).get(0);
        CardController.moveCardFromFirstArrayToSecondArray(monsterFromGraveyard, graveyard, monsters, "1");
        monsterFromGraveyard.setMode("OO");
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        return !((!CardController.arrayListContainsMonster(myBoard.getGraveyard())) ||
                CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5));
    }
}
