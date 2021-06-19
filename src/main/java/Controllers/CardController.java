package Controllers;

import Models.Board;
import Models.Card;
import Views.GameView;

import java.util.ArrayList;

public class CardController {


    public static void setCardInPlace(Card card, Board board, String place) {
        if (place.startsWith("1")) {
            int secondPlace = Integer.parseInt(place.substring(2));
            ArrayList<Card> monsters = board.getMonsters();
            monsters.set(secondPlace, card);
            board.setMonsters(monsters);
            if (card != null) card.setPlace(place);
        }
        if (place.startsWith("2")) {
            int secondPlace = Integer.parseInt(place.substring(2));
            ArrayList<Card> spellsAndTraps = board.getSpellsAndTraps();
            spellsAndTraps.set(secondPlace, card);
            board.setSpellsAndTraps(spellsAndTraps);
            if (card != null) card.setPlace(place);
        }
        if (place.startsWith("3")) {
            ArrayList<Card> graveyard = board.getGraveyard();
            graveyard.add(card);
            board.setGraveyard(graveyard);
            if (card != null) card.setPlace("3");
        }
        if (place.startsWith("4")) {
            ArrayList<Card> deckZone = board.getDeckZone();
            deckZone.add(card);
            board.setDeckZone(deckZone);
            if (card != null) card.setPlace("4");
        }
        if (place.startsWith("5")) {
            ArrayList<Card> fieldZone = board.getFieldZone();
            fieldZone.add(card);
            board.setFieldZone(fieldZone);
            if (card != null) card.setPlace("5");
        }
    }


    public static void moveCardToGraveyard(Board board, Card card) {
        String place = card.getPlace();
        if (!place.startsWith("3")) {
            if (place.startsWith("1") || place.startsWith("2")) {
                setCardInPlace(null, board, place);
            }
            if (place.startsWith("4")) {
                ArrayList<Card> deckZone = board.getDeckZone();
                deckZone.remove(card);
                board.setDeckZone(deckZone);
            }
            if (place.startsWith("5")) {
                ArrayList<Card> fieldZone = board.getFieldZone();
                fieldZone.remove(card);
                board.setFieldZone(fieldZone);
            }
            if(place.startsWith("hand")) {
                ArrayList<Card> cardsInHand = board.getCardsInHand();
                cardsInHand.remove(card);
                board.setCardsInHand(cardsInHand);
            }
            setCardInPlace(card, board, "3");
        }
    }


    public static void getAndDestroyMonstersInBoard(Board board, int numberOfCards) {
        ArrayList<Card> cards = GameView.getCardsByAddressFromZone(board, 1, String.valueOf(numberOfCards));
        for (Card card : cards) {
            CardController.moveCardToGraveyard(board, card);
        }
    }


    public static boolean cardsOfArrayListAreAllNull(ArrayList<Card> arrayList) {
        for (Card card : arrayList) {
            if (card != null) return false;
        }
        return true;
    }


    public static boolean arrayListContainsMonster(ArrayList<Card> arrayList){
        if(arrayList.size()==0) return false;
        for(Card card : arrayList){
            if(card.isMonster()) return true;
        }
        return false;
    }

    public static boolean arrayListContainsMonsterWithType(ArrayList<Card> arrayList, String type){
        for(Card card : arrayList){
            if(card.getType().startsWith("Monster") && card.getMonsterType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public static boolean arrayListContainsCardWithName(ArrayList<Card> arrayList, String name){
        for(Card card : arrayList){
            if(card.getName().equals(name)) return true;
        }
        return false;
    }

    public static boolean arrayListOfCardsIsFull(ArrayList<Card> arrayList, int maximumSizePossible){
        if(arrayList.size() < maximumSizePossible) return false;
        for (Card card : arrayList) {
            if (card == null) return false;
        }
        return true;
    }


    public static void destroyAllCardsInBoard(Board board){
        destroyAllMonstersInBoard(board);
        destroyAllSpellsAndTrapsInBoard(board);
        moveCardToGraveyard(board, board.getFieldZone().get(0));
    }

    public static void destroyAllMonstersInBoard(Board board){
        ArrayList<Card> monsters = board.getMonsters();
        for(int i = 4; i >= 0; i--){
            moveCardToGraveyard(board, monsters.get(i));
        }
    }

    public static void destroyAllSpellsAndTrapsInBoard(Board board){
        ArrayList<Card> spellsAndTraps = board.getSpellsAndTraps();
        for(int i = 4; i >= 0; i--){
            moveCardToGraveyard(board, spellsAndTraps.get(i));
        }
    }


    public static void addCardToHandFromDeck(Board board, Card card){
        // only call when you are sure hand is not full
        if(card == null) return;
        ArrayList<Card> deckZone = board.getDeckZone();
        ArrayList<Card> cardsInHand = board.getCardsInHand();
        moveCardFromFirstArrayToSecondArray(card, deckZone, cardsInHand, "hand");
    }

    public static void moveCardFromFirstArrayToSecondArray(Card card, ArrayList<Card> arrayList1, ArrayList<Card> arrayList2, String newZoneNumber){
        // only call when you are sure arrayList2 is not full
        if(card == null) return;
        if(arrayList2.contains(card)) return;
        if(arrayList1.contains(card)) arrayList1.set(arrayList1.indexOf(card), null);
        int size = arrayList2.size();
        for (int i = 0; i < size; i++) {
            if (arrayList2.get(i) == null) {
                arrayList2.set(i, card);
                card.setPlace(newZoneNumber + "_" + (i + 1));
                if(newZoneNumber.equals("5")) card.setPlace("5");
            }
        }
    }


    public static boolean boardContainsCard(Board board, Card card){
        return (board.getMonsters().contains(card) ||
                board.getSpellsAndTraps().contains(card) ||
                board.getFieldZone().contains(card) ||
                board.getCardsInHand().contains(card) ||
                board.getDeckZone().contains(card));
    }

    public static boolean areThereEnoughCardsInMonsterZoneToTribute(Board board, int number) {
        int existingMonsters = 0;
        ArrayList<Card> monsters = board.getMonsters();
        for(Card card : monsters){
            if(card != null){
                existingMonsters++;
            }
        }
        return existingMonsters >= number;
    }


    public static Card getAMonsterFromGraveyard(Board board){
        for(Card card: board.getGraveyard()){
            if(card.isMonster()) return card;
        }
        return null;
    }

    public static ArrayList<Card> getSomeCardsFromZone(Board board, int zoneNumber, String numberOfCards){
        // zoneNumber must be 1 or 2
        ArrayList<Card> cards = new ArrayList<>();

        int number = 1;
        if(!numberOfCards.startsWith("s")) number = Integer.parseInt(numberOfCards);

        for(int i = 0; i <= 4; i++){
            Card card = board.getCardByPlace(zoneNumber + "_" + i + 1);
            if (card != null) cards.add(card);
            if(cards.size() == number) break;
        }

        return cards;
    }

}


