package Models;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Card {

    private String type;  // examples: Monster_Warrior, Spell_Field, Trap
    private String monsterType;
    private String cardType;
    private String name;
    private int level;
    private String attribute;
    private String status; // Limited, Unlimited
    private String description;
    private int attack;
    private int defense;
    private int price;
    private int speed;
    private int numberOfTributesNeeded; //("Gate Guardian" is 3, for example)

    private boolean isNormalMonster; // set in constructor from database


    private HashMap<String, Integer> typesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell; // set in constructor from database
    private HashMap<String, Integer> typesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell; // set in constructor from database
    private HashMap<String, Integer> typesOfMonstersWithAttackToBeIncreasedDueToEquipSpell; // set in constructor from database (String can be null)
    private HashMap<String, Integer> typesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell; // set in constructor from database (String can be null)

    private Card chosenRitualMonsterForRitualSpell; // used for advanced ritual art

    private String mode;  // examples : OO, DH, DO
    private String place;   // examples: 1_5, 2_2, 3, 4, 5, hand_6

    private boolean canAnyoneAttack; //set to true again after any use of the value
    private boolean canBeDestroyed;
    private boolean canThisCardAttack; // check this after attack request and reset to true after you checked the value

    private boolean isPositionChangedInTurn; // reset to false after any turn
    private boolean attackedInTurn; // reset to false after any turn
    private boolean isEffectUsedInTurn;  // reset to false after any turn
    private int numberOfTurnsOfOpponentBeingActive; // increase this after any turn of opponent if isEffectActive is true // reset to zero when it comes to game


    private int numberOfTimesUsed;
    private boolean isEffectActive; // check this right before any use of spells or traps or monsters  // reset to false after any use

    private Card targetCard; // used for changeOfHeart, ...
    private int damageToLpOfOpponent; //set this after calculating damage //also used for MagicCylinder, ...

    public Card(String name){
        this.name = name;
        canAnyoneAttack = true;
        canBeDestroyed = !name.equals("Marshmallon");
        try {
            FileReader fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            String[] row;
            while((row = csvReader.readNext()) != null){
                if(row[0].equals(name)){
                    level = Integer.parseInt(row[1]);
                    attribute = row[2];
                    monsterType = row[3];
                    cardType = row[4];
                    type = "Monster_" + monsterType + "_" + cardType;
                    attack = Integer.parseInt(row[5]);
                    defense = Integer.parseInt(row[6]);
                    description = row[7];
                    price = Integer.parseInt(row[8]);
                    numberOfTributesNeeded = Integer.parseInt(row[9]);
                    speed = 1;
                    if(name.equals("Suijin") || name.equals("Texchanger")) speed = 2;
                    setIsNormalMonsterByName(name);
                    canThisCardAttack = true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        try {
            FileReader fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\SpellTrap.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            String[] row;
            while((row = csvReader.readNext()) != null){
                if(row[0].equals(name)){
                    type = row[1] + "_" + row[2];
                    description = row[3];
                    status = row[4];
                    price = Integer.parseInt(row[5]);
                    if(type.contains("Field")){
                        setTypesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell(row[6]);
                        setTypesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell(row[7]);
                    }
                    if(type.contains("Equip")){
                        setTypesOfMonstersWithAttackToBeIncreasedDueToEquipSpell(row[6]);
                        setTypesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell(row[7]);
                    }
                    speed = 1;
                    if(type.startsWith("Spell_Quick-play")) speed = 2;
                    if(type.startsWith("Trap")) speed = 2;
                    if(type.startsWith("Trap_Counter")) speed = 3;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static Card getCardByName(String name){
        FileReader fileReader;
        String[] row;
        try {
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(name.equals(row[0])) return new Card(row[0]);
            }
            fileReader = new FileReader(User.projectAddress + "\\src\\main\\resources\\SpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(name.equals(row[0])) return new Card(row[0]);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isMonster(){
        return this.getType().startsWith("Monster");
    }

    public boolean isSpell(){
        return this.getType().startsWith("Spell");
    }

    public boolean isTrap(){
        return this.getType().startsWith("Trap");
    }

    public void setIsNormalMonsterByName(String name){
        isNormalMonster = name.equals("Battle OX") || name.equals("Axe Raider") || name.equals("Horn Imp") ||
                name.equals("Silver Fang") || name.equals("Fireyarou") || name.equals("Curtain of the dark ones") ||
                name.equals("Feral Imp") || name.equals("Dark magician") || name.equals("Wattkid") ||
                name.equals("Baby dragon") || name.equals("Hero of the east") || name.equals("Battle warrior") ||
                name.equals("Crawling dragon") || name.equals("Flame manipulator") || name.equals("Blue-Eyes white dragon") ||
                name.equals("Slot Machine") || name.equals("Haniwa") || name.equals("Bitron") ||
                name.equals("Leotron") || name.equals("Alexandrite Dragon") || name.equals("Warrior Dai Grepher") ||
                name.equals("Dark Blade") || name.equals("Wattaildragon") || name.equals("Spiral Serpent");
    }

    public boolean isEffectActive() {
        return isEffectActive;
    }

    public void setIsEffectActive(boolean effectActive) {
        isEffectActive = effectActive;
    }

    public String getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setNormalMonster(boolean normalMonster) {
        isNormalMonster = normalMonster;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setCanAnyoneAttack(boolean canAnyoneAttack) {
        this.canAnyoneAttack = canAnyoneAttack;
    }

    public void setNumberOfTributesNeeded(int numberOfTributesNeeded) {
        this.numberOfTributesNeeded = numberOfTributesNeeded;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIsEffectUsedInTurn(boolean effectUsedInTurn) {
        isEffectUsedInTurn = effectUsedInTurn;
    }

    public void setNumberOfTimesUsed(int numberOfTimesUsed) {
        this.numberOfTimesUsed = numberOfTimesUsed;
    }

    public void setCanBeDestroyed(boolean canBeDestroyed) {
        this.canBeDestroyed = canBeDestroyed;
    }

    public void increaseNumberOfTimesUsed(){
        numberOfTimesUsed++;
    }

    public boolean canBeDestroyed() {
        return canBeDestroyed;
    }

    public String getType() {
        return type;
    }

    public int getNumberOfTributesNeeded() {
        return numberOfTributesNeeded;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getPrice() {
        return price;
    }

    public String getMode() {
        return mode;
    }

    public String getPlace() {
        return place;
    }

    public boolean canAnyoneAttack() {
        return canAnyoneAttack;
    }

    public boolean isEffectUsedInTurn() {
        return isEffectUsedInTurn;
    }

    public int getNumberOfTimesUsed() {
        return numberOfTimesUsed;
    }

    public Card getTargetCard() {
        return targetCard;
    }

    public void setTargetCard(Card targetCard) {
        this.targetCard = targetCard;
    }

    public int getDamageToLpOfOpponent() {
        return damageToLpOfOpponent;
    }

    public void setDamageToLpOfOpponent(int damageToLpOfOpponent) {
        this.damageToLpOfOpponent = damageToLpOfOpponent;
    }

    public HashMap<String, Integer> getTypesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell() {
        return typesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell;
    }

    public void setTypesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell(String string) {
        this.typesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell = new HashMap<>();
        String[] elements = string.split(",");
        for(String element : elements){
            String[] elementStrings = element.split(":");
            if(elementStrings.length == 1) return;
            String type = elementStrings[0];
            int attack = Integer.parseInt(elementStrings[1]);
            typesOfMonstersWithAttackToBeIncreasedDueToAFieldSpell.put(type, attack);
        }
    }

    public HashMap<String, Integer> getTypesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell() {
        return typesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell;
    }

    public void setTypesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell(String string) {
        this.typesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell = new HashMap<>();
        String[] elements = string.split(",");
        for(String element : elements){
            String[] elementStrings = element.split(":");
            if(elementStrings.length == 1) return;
            String type = elementStrings[0];
            int defense = Integer.parseInt(elementStrings[1]);
            typesOfMonstersWithDefenseToBeIncreasedDueToAFieldSpell.put(type, defense);
        }
    }

    public HashMap<String, Integer> getTypesOfMonstersWithAttackToBeIncreasedDueToEquipSpell() {
        return typesOfMonstersWithAttackToBeIncreasedDueToEquipSpell;
    }

    public void setTypesOfMonstersWithAttackToBeIncreasedDueToEquipSpell(String string) {
        this.typesOfMonstersWithAttackToBeIncreasedDueToEquipSpell = new HashMap<>();
        String[] elements = string.split(",");
        for(String element : elements){
            String[] elementStrings = element.split(":");
            if(elementStrings.length == 1) return;
            String type = elementStrings[0];
            int attack = Integer.parseInt(elementStrings[1]);
            typesOfMonstersWithAttackToBeIncreasedDueToEquipSpell.put(type, attack);
        }
    }

    public HashMap<String, Integer> getTypesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell() {
        return typesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell;
    }

    public void setTypesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell(String string) {
        this.typesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell = new HashMap<>();
        String[] elements = string.split(",");
        for(String element : elements){
            String[] elementStrings = element.split(":");
            if(elementStrings.length == 1) return;
            String type = elementStrings[0];
            int defense = Integer.parseInt(elementStrings[1]);
            typesOfMonstersWithDefenseToBeIncreasedDueToEquipSpell.put(type, defense);
        }
    }

    public boolean isNormalMonster() {
        return isNormalMonster;
    }

    public Card getChosenRitualMonsterForRitualSpell() {
        return chosenRitualMonsterForRitualSpell;
    }

    public void setChosenRitualMonsterForRitualSpell(Card chosenRitualMonsterForRitualSpell) {
        this.chosenRitualMonsterForRitualSpell = chosenRitualMonsterForRitualSpell;
    }

    public boolean canThisCardAttack() {
        return canThisCardAttack;
    }

    public void setCanThisCardAttack(boolean canThisCardAttack) {
        this.canThisCardAttack = canThisCardAttack;
    }

    public void increaseAttack(int amount){
        attack += amount;
    }

    public void increaseDefense(int amount){
        defense += amount;
    }

    public boolean isPositionChangedInTurn() {
        return isPositionChangedInTurn;
    }

    public void setPositionChangedInTurn(boolean positionChangedInTurn) {
        isPositionChangedInTurn = positionChangedInTurn;
    }

    public boolean hasAttackedInTurn() {
        return attackedInTurn;
    }

    public void setAttackedInTurn(boolean attackedInTurn) {
        this.attackedInTurn = attackedInTurn;
    }

    public int getNumberOfTurnsOfOpponentBeingActive() {
        return numberOfTurnsOfOpponentBeingActive;
    }

    public String getDescription() {
        return description;
    }

    public void increaseNumberOfTurnsOfOpponentBeingActive(){
        numberOfTurnsOfOpponentBeingActive++;
    }

    public void setNumberOfTurnsOfOpponentBeingActive(int numberOfTurnsOfOpponentBeingActive) {
        this.numberOfTurnsOfOpponentBeingActive = numberOfTurnsOfOpponentBeingActive;
    }
}
