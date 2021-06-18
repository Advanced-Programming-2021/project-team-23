package Controllers.action;

import Controllers.GameController;
import Models.Board;
import Models.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipSpell extends Action{

    @Override
    public void runFirstAction(GameController gameController, Card myCard, Card opponentCard) {
        setBoards(gameController, myCard);
        Card equippedCard = myCard.getEquippedCard();
        if(myCard.getName().equals("United We Stand")){
            runActionOfUnitedWeStand(gameController, equippedCard, myCard);
            return;
        }
        if(myCard.getName().equals("Magnum Shield")){
            runActionOfMagnumShield(equippedCard);
            return;
        }
        HashMap<String, Integer> monsterTypesAndAttack =
                myCard.getTypesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell();
        HashMap<String, Integer> monsterTypesAndDefense =
                myCard.getTypesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell();

        for (String type : monsterTypesAndAttack.keySet()) {
            if (type.equals("null") || equippedCard.getMonsterType().equals(type)) {
                equippedCard.increaseAttack(monsterTypesAndAttack.get(type));
            }
        }
        for (String type : monsterTypesAndDefense.keySet()) {
            if (type.equals("null") || equippedCard.getMonsterType().equals(type)) {
                equippedCard.increaseDefense(monsterTypesAndDefense.get(type));
            }
        }
    }

    public void runActionOfUnitedWeStand(GameController gameController, Card equippedCard, Card myCard){
        setBoards(gameController, myCard);
        int numberOfFaceUpMonsters = getNumberOfFaceUpMonstersInMonsterZone(myBoard);
        equippedCard.increaseAttack(800 * numberOfFaceUpMonsters);
        equippedCard.increaseDefense(800 * numberOfFaceUpMonsters);
    }

    public int getNumberOfFaceUpMonstersInMonsterZone(Board board){
        int counter = 0;
        ArrayList<Card> monsters = board.getMonsters();
        for(Card monster : monsters){
            if(monster != null && monster.getMode().endsWith("O")){
                counter++;
            }
        }
        return counter;
    }

    public void runActionOfMagnumShield(Card equippedCard){
        if(equippedCard.getMonsterType().equals("Warrior")){
            if(equippedCard.getMode().startsWith("O")){
                equippedCard.increaseAttack(equippedCard.getDefense());
            }
            if(equippedCard.getMode().startsWith("D")){
                equippedCard.increaseDefense(equippedCard.getAttack());
            }
        }
    }


    @Override
    public void runActionForDefense(GameController gameController, Card myCard, Card opponentCard) {

    }

    @Override
    public boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard) {
        return opponentCard == null;
    }
}