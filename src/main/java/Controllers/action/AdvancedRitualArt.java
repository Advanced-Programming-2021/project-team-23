package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdvancedRitualArt extends Action{

    // call after you got chosenRitualMonsterForRitualSpell from user and then print successful summon

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        Card ritualMonster = myCard.getChosenRitualMonsterForRitualSpell();

        CardController.moveCardFromFirstArrayToSecondArray(ritualMonster, myBoard.getCardsInHand(), myBoard.getMonsters(), "1");
        myCard.setMode("OO");
        if(!gameController.users[myNumber].isAI() && GameView.doesPlayerWantToSetCardInDefensiveMode()){
            myCard.setMode("DO");
        }
    }

    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        if(opponentCard != null) return false;
        setBoards(gameController, myCard);
        if(CardController.arrayListOfCardsIsFull(myBoard.getMonsters(), 5)){
            return false;
        }
        ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
        ArrayList<Card> ritualMonsters = new ArrayList<>();
        for(Card card : cardsInHand){
            if(card != null && card.isMonster() && card.getType().contains("Ritual")){
                ritualMonsters.add(card);
            }
        }
        if(ritualMonsters.size()==0) return false;

        for(Card ritualMonster : ritualMonsters){
            if(canRitualSummonWithMonster(ritualMonster, myBoard)) return true;
        }
        return false;
    }

    public static boolean canRitualSummonWithMonster(Card ritualMonster, Board board){
        List<Card> monsterZone = board.getMonsters().stream().filter(Objects::nonNull).collect(Collectors.toList());
        ArrayList<Card> randomArray = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            if(i==1) randomArray.add(monsterZone.get(0));
            for (int j = 0; j < 2; j++) {
                if(j==1) randomArray.add(monsterZone.get(1));
                else randomArray.remove(monsterZone.get(1));
                for (int k = 0; k < 2; k++) {
                    if(k==1) randomArray.add(monsterZone.get(2));
                    else randomArray.remove(monsterZone.get(2));
                    for (int l = 0; l < 2; l++) {
                        if(l==1) randomArray.add(monsterZone.get(3));
                        else randomArray.remove(monsterZone.get(3));
                        for (int m = 0; m < 2; m++) {
                            if(m==1) randomArray.add(monsterZone.get(4));
                            else randomArray.remove(monsterZone.get(4));
                            if(isSumOfLevelsOfMonstersInArrayEqualToLevelOfRitualMonster(randomArray, ritualMonster)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSumOfLevelsOfMonstersInArrayEqualToLevelOfRitualMonster(ArrayList<Card> cards, Card ritualMonster){
        int sum = 0;
        for(Card card : cards){
            if(card != null) sum += card.getLevel();
        }
        return sum == ritualMonster.getLevel();
    }

}
