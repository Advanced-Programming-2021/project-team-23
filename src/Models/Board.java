package Models;

import java.util.ArrayList;
import java.util.Collections;

public class Board {

    public User user;

    private ArrayList<Card> monsters = new ArrayList<>();
    private ArrayList<Card> spellsAndTraps = new ArrayList<>();
    private ArrayList<Card> deckZone = new ArrayList<>();
    private ArrayList<Card> graveyard = new ArrayList<>();
    private ArrayList<Card> fieldZone = new ArrayList<>();
    private ArrayList<Card> cardsInHand = new ArrayList<>();

    private boolean canAnyTrapBeActivated; // check this before any activation (because of MirageDragon, for example)
    private boolean canMonstersAttack; // check this before any attack

    private boolean canAnyCardBeChosenForThisBoardInDrawPhase; // check this before draw // reset to true after any use of the value

    public Board(User user){
        this.user = user;
        for(int i = 0; i < 5; i++){
            monsters.add(null);
            spellsAndTraps.add(null);
            cardsInHand.add(null);
        }
        cardsInHand.add(null);
        fieldZone.add(null);
        // add cards to deckZone
        ArrayList<Card> mainDeck = user.activeDeck.mainDeck;
        for(Card card: mainDeck){
            Card deckCard = new Card(card.getName());
            deckCard.setPlace("4");
            deckZone.add(deckCard);
        }
        Collections.shuffle(deckZone);
        // add 5 cards from deckZone to cardsInHand
        for(int i = 0; i < 5; i++){
            Card card = deckZone.get(i);
            deckZone.remove(card);
            card.setPlace("hand_" + (i + 1));
            cardsInHand.set(i, card);
        }
        canAnyTrapBeActivated = true;
        canMonstersAttack = true;
        canAnyCardBeChosenForThisBoardInDrawPhase = true;
    }

    public ArrayList<Card> getMonsters() {
        return monsters;
    }

    public ArrayList<Card> getSpellsAndTraps() {
        return spellsAndTraps;
    }

    public ArrayList<Card> getDeckZone() {
        return deckZone;
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public ArrayList<Card> getFieldZone() {
        return fieldZone;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public boolean canAnyTrapBeActivated() {
        return canAnyTrapBeActivated;
    }

    public boolean canAnyCardBeChosenForThisBoardInDrawPhase() {
        return canAnyCardBeChosenForThisBoardInDrawPhase;
    }

    public void setCanAnyCardBeChosenForThisBoardInDrawPhase(boolean canAnyCardBeChosenForThisBoardInDrawPhase) {
        this.canAnyCardBeChosenForThisBoardInDrawPhase = canAnyCardBeChosenForThisBoardInDrawPhase;
    }

    public void setCanAnyTrapBeActivated(boolean canAnyTrapBeActivated) {
        this.canAnyTrapBeActivated = canAnyTrapBeActivated;
    }

    public void setMonsters(ArrayList<Card> monsters) {
        this.monsters = monsters;
        // update other arraylists
    }

    public void setSpellsAndTraps(ArrayList<Card> spellsAndTraps) {
        this.spellsAndTraps = spellsAndTraps;
        // update other arraylists
    }

    public void setDeckZone(ArrayList<Card> deckZone) {
        this.deckZone = deckZone;
        // update other arraylists
    }

    public void setGraveyard(ArrayList<Card> graveyard) {
        this.graveyard = graveyard;
        // update other arraylists
    }

    public void setFieldZone(ArrayList<Card> fieldZone) {
        this.fieldZone = fieldZone;
        // update other arraylists
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
        // update other arraylists
    }

    public boolean canMonstersAttack() {
        return canMonstersAttack;
    }

    public void setCanMonstersAttack(boolean canMonstersAttack) {
        this.canMonstersAttack = canMonstersAttack;
    }


    public Card getCardByPlace(String place){
        // check place validation before calling this method
        // valid places examples : 3_1, 1_4, 4, hand_6
        // invalid places examples : 3, 3_0, 2_0
        if(place.startsWith("1")||place.startsWith("2")){
            int secondPlace = Integer.parseInt(place.substring(2));
            if(secondPlace > 5 || secondPlace < 1) {
                return null;
            }
            if(place.startsWith("1")){
                return monsters.get(secondPlace - 1);
            }
            if(place.startsWith("2")){
                return spellsAndTraps.get(secondPlace - 1);
            }
        }
        if(place.startsWith("3")){
            int size = graveyard.size();
            if(size == 0) return null;
            int secondPlace = Integer.parseInt(place.substring(2));
            if(secondPlace > size) return null;
            return graveyard.get(secondPlace - 1);
        }
        if(place.startsWith("4")){
            int size = deckZone.size();
            if(size == 0) return null;
            return deckZone.get(size - 1);
        }
        if(place.startsWith("5")){
            return fieldZone.get(0);
        }
        if(place.startsWith("hand")){
            int secondPlace = Integer.parseInt(place.substring(5));
            return cardsInHand.get(secondPlace - 1);
        }
        return null;
    }
}
