package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class MonsterReborn extends Action{


    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(canEffectBeActivated(gameController, myCard, opponentCard)) {
            Card card;
            if(gameController.isAI) {
                card = CardController.getAMonsterFromGraveyard(opponentBoard);
                if(card == null) card = CardController.getAMonsterFromGraveyard(myBoard);
            }
            else card = GameView.getAMonsterInGraveyardsFromUser(myBoard, opponentBoard);
            ArrayList<Card> graveyard;
            if(myBoard.getGraveyard().contains(card)) graveyard = myBoard.getGraveyard();
            else graveyard = opponentBoard.getGraveyard();
            CardController.moveCardFromFirstArrayToSecondArray(card, graveyard, myBoard.getMonsters(), "1");
            card.setMode("OO");
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {
        runFirstAction(gameController, myCard, opponentCard);
    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        if(CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5)) return false;
        ArrayList<Card> myGraveyard = myBoard.getGraveyard();
        ArrayList<Card> opponentGraveyard = opponentBoard.getGraveyard();
        for(Card card : myGraveyard){
            if(card.getType().startsWith("Monster")) return true;
        }
        for(Card card : opponentGraveyard){
            if(card.getType().startsWith("Monster")) return true;
        }
        return false;
    }

}
