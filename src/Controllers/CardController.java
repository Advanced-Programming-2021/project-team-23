package Controllers;

import Models.Board;
import Models.Card;
import Views.Duel;
import Views.GameView;
import Views.RegisterMenu;

import java.util.ArrayList;

public class CardController {


    public static void setCardInPlace(Card card, Board board, String place) {
        if (place.startsWith("1")) {
            int secondPlace = Integer.parseInt(place.substring(2));
            ArrayList<Card> monsters = board.getMonsters();
            monsters.set(secondPlace - 1, card);
            if (card != null) card.setPlace(place);
        }
        if (place.startsWith("2")) {
            int secondPlace = Integer.parseInt(place.substring(2));
            ArrayList<Card> spellsAndTraps = board.getSpellsAndTraps();
            spellsAndTraps.set(secondPlace - 1, card);
            if (card != null) card.setPlace(place);
        }
        if (place.startsWith("3")) {
            ArrayList<Card> graveyard = board.getGraveyard();
            graveyard.add(card);
            if (card != null) card.setPlace("3_" + graveyard.size());
        }
        if (place.startsWith("4")) {
            ArrayList<Card> deckZone = board.getDeckZone();
            deckZone.add(card);
            if (card != null) card.setPlace("4");
        }
        if (place.startsWith("5")) {
            ArrayList<Card> fieldZone = board.getFieldZone();
            fieldZone.set(0, card);
            if (card != null) card.setPlace("5");
        }
    }


    public static void moveCardToGraveyard(Board board, Card card) {
        if(card == null) return;
        String place = card.getPlace();
        if (!place.startsWith("3")) {
            if (place.startsWith("1") || place.startsWith("2")) {
                setCardInPlace(null, board, place);
            }
            if (place.startsWith("4")) {
                ArrayList<Card> deckZone = board.getDeckZone();
                deckZone.remove(card);
            }
            if (place.startsWith("5")) {
                ArrayList<Card> fieldZone = board.getFieldZone();
                fieldZone.set(0, null);
            }
            if(place.startsWith("hand")) {
                ArrayList<Card> cardsInHand = board.getCardsInHand();
                for (int i = 0; i < 6; i++) {
                    if (cardsInHand.get(i) == card) cardsInHand.set(i, null);
                }
            }
            for(int j = 0; j < 2; j++){
                card.currentImageView[j].setImage(card.imageView.getImage());
                Duel.panes[j].moveCard(card, place, "3");
            }
            setCardInPlace(card, board, "3");

            Duel duel = new Duel(null);
            duel.playDieSound();
        }
    }


    public static void getAndDestroyMonstersInBoard(Board board, int numberOfCards) {
        ArrayList<Card> cards;// = GameView.getCardsByAddressFromZone(board, 1, String.valueOf(numberOfCards));
        cards = CardController.getSomeCardsFromZone(board, 1, String.valueOf(numberOfCards));
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
            if(card != null && card.isMonster() && card.getMonsterType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public static boolean arrayListContainsCardWithName(ArrayList<Card> arrayList, String name){
        for(Card card : arrayList){
            if(card != null && card.getName().equals(name)) return true;
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
        deckZone.remove(card);
        for(int i = 0; i < 2; i++){
            if(RegisterMenu.gameController.boards[i] == board) card.currentImageView[i].setImage(card.imageView.getImage());
        }
        moveCardFromFirstArrayToSecondArray(card, deckZone, cardsInHand, "hand");
    }

    public static void moveCardFromFirstArrayToSecondArray(Card card, ArrayList<Card> arrayList1, ArrayList<Card> arrayList2, String newZoneNumber){
        // only call when you are sure arrayList2 is not full
        if(card == null) return;
        String initialPlace = card.getPlace();
        if(arrayList2.contains(card)) return;
        if(arrayList1.contains(card)) arrayList1.set(arrayList1.indexOf(card), null);
        int size = arrayList2.size();
        for (int i = 0; i < size; i++) {
            if (arrayList2.get(i) == null) {
                arrayList2.set(i, card);
                String secondPlace = newZoneNumber + "_" + (i + 1);
                if(newZoneNumber.equals("5")) secondPlace = "5";
                for(int j = 0; j < 2; j++){
                    Duel.panes[j].moveCard(card, initialPlace, secondPlace);
                }
                card.setPlace(secondPlace);
                return;
            }
        }
    }


    public static boolean boardContainsCard(Board board, Card card){
        return (board.getMonsters().contains(card) ||
                board.getSpellsAndTraps().contains(card) ||
                board.getFieldZone().contains(card) ||
                board.getCardsInHand().contains(card) ||
                board.getDeckZone().contains(card) ||
                board.getGraveyard().contains(card));
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
            if(card != null && card.isMonster()) return card;
        }
        return null;
    }

    public static ArrayList<Card> getSomeCardsFromZone(Board board, int zoneNumber, String numberOfCards){
        // zoneNumber must be 1 or 2
        ArrayList<Card> cards = new ArrayList<>();

        int number = 1;
        if(!numberOfCards.startsWith("s")) number = Integer.parseInt(numberOfCards);

        for(int i = 0; i <= 4; i++){
            Card card = board.getCardByPlace(zoneNumber + "_" + (i + 1));
            if (card != null) cards.add(card);
            if(cards.size() == number) break;
        }

        return cards;
    }

}

