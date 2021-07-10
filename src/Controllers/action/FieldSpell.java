package Controllers.action;

import Controllers.GameController;
import Models.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldSpell extends Action{

    // call when a field spell activated

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        if(myCard.getName().equals("Closed Forest")){
            runActionOfClosedForest(gameController, myCard);
            return;
        }
        setBoards(gameController, myCard);
        ArrayList<Card>[] monsterZones = new ArrayList[]{myBoard.getMonsters(), opponentBoard.getMonsters()};

        HashMap<String, Integer> monsterTypesAndAttack =
                myCard.getTypesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell();
        HashMap<String, Integer> monsterTypesAndDefense =
                myCard.getTypesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell();

        for(ArrayList<Card> monsterZone : monsterZones){
            for(Card monster : monsterZone) {
                if(monster != null) {
                    if(monsterTypesAndAttack != null) {
                        for (String type : monsterTypesAndAttack.keySet()) {
                            if (monster.getMonsterType().equals(type)) {
                                monster.increaseAttack(monsterTypesAndAttack.get(type));
                            }
                        }
                    }
                    if(monsterTypesAndDefense != null) {
                        for (String type : monsterTypesAndDefense.keySet()) {
                            if (monster.getMonsterType().equals(type)) {
                                monster.increaseDefense(monsterTypesAndDefense.get(type));
                            }
                        }
                    }
                }
            }
        }
    }

    public void runActionOfClosedForest(GameController gameController, Card myCard){
        setBoards(gameController, myCard);
        int numberOfMonstersInGraveyard = getNumberOfMonstersInArrayList(myBoard.getGraveyard());
        ArrayList<Card> monsterZone = myBoard.getMonsters();
        for(Card monster : monsterZone) {
            if ((monster != null) && monster.getMonsterType().contains("Beast")) {
                monster.increaseAttack(100 * numberOfMonstersInGraveyard);
            }
        }
    }

    public int getNumberOfMonstersInArrayList(ArrayList<Card> arrayList){
        int counter = 0;
        for(Card card : arrayList){
            if(card != null && card.isMonster()){
                counter++;
            }
        }
        return counter;
    }


    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }
}

