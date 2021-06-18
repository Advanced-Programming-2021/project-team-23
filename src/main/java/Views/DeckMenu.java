package Views;

import Models.Card;
import Models.Deck;
import Models.ICheatCode;
import Models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu implements IMenu , ICheatCode {

    @Override
    public void processCommand(String command) throws IOException {
        User user = MainMenu.currentUser;
        try {
            String message = processDeckCommands(command, user);
            if(message != null) System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String processDeckCommands(String command, User user) throws Exception {
        Matcher matcher;
        matcher = getCommandMatcher(command, "deck create (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = Deck.getDeckByName(deckName);
            if(deck != null){
                throw new Exception("deck with name " + deckName + " already exists");
            }
            deck = new Deck(deckName);
            user.addDeck(deck);
            return ("deck created successfully!");
        }
        matcher = getCommandMatcher(command, "deck delete (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            user.deleteDeck(deck);
            return ("deck deleted successfully!");
        }
        matcher = getCommandMatcher(command, "deck set-active (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            deck.isActive = true;
            user.activeDeck = deck;
            return ("deck activated successfully!");
        }
        matcher = getCommandMatcher(command, "deck add-card card (.+) deck (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            Card card = user.getCardByNameFromCardsInNoDeck(cardName);
            if(card == null){
                throw new Exception("card with name " + cardName + " does not exist");
            }
            String deckName = matcher.group(2);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck add-card card (.+) deck (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(2);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            if(isSideDeck && deck.sideDeck.size() == 15){
                throw new Exception("side deck is full");
            }
            if((!isSideDeck) && deck.mainDeck.size() == 60){
                throw new Exception("main deck is full");
            }
            int number = deck.getNumberOfCardByName(cardName);
            if((card.getStatus().equals("Limited") && number == 1)
                    || number == 3) {
                throw new Exception("there are already " + number + " cards with name " + cardName + " in deck " + deckName);
            }
            user.addCardToDeck(card, deck, isSideDeck);
            return "card added to deck successfully";
        }
        matcher = getCommandMatcher(command, "deck rm-card card (.+) deck (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            String deckName = matcher.group(2);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck rm-card card (.+) deck (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(2);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            Card card;
            if(isSideDeck) {
                card = deck.getCardByNameInSideDeck(cardName);
                if (card == null) {
                    throw new Exception("card with name " + cardName + " does not exist in side deck");
                }
                deck.sideDeck.remove(card);
            } else {
                card = deck.getCardByNameInMainDeck(cardName);
                if (card == null) {
                    throw new Exception("card with name " + cardName + " does not exist in main deck");
                }
                deck.mainDeck.remove(card);
            }
            user.cardsInNoDeck.add(card);
            User.setUserInFile(user);
            return "card removed from deck successfully";
        }
        matcher = getCommandMatcher(command, "deck show all");
        if(matcher.matches()) {
            DeckMenu.showAllDecks(user);
            return null;
        }
        matcher = getCommandMatcher(command, "deck show deck-name (.+)");
        if(matcher.matches()){
            String deckName = matcher.group(1);
            boolean isSideDeck = false;
            matcher = getCommandMatcher(command, "deck show deck-name (.+) side");
            if(matcher.matches()){
                deckName = matcher.group(1);
                isSideDeck = true;
            }
            Deck deck = user.getDeckByName(deckName);
            if(deck == null){
                throw new Exception("deck with name " + deckName + " does not exist");
            }
            DeckMenu.showDeck(user, deck, isSideDeck);
            return null;
        }
        matcher = getCommandMatcher(command, "deck show cards");
        if(matcher.matches()) {
            DeckMenu.showAllCards(user);
            return null;
        }
        throw new Exception("invalid command");
    }


    public static void showAllDecks(User user){
        System.out.println("Decks:");
        System.out.println("Active decks:");
        Deck activeDeck = user.activeDeck;
        if(activeDeck != null){
            System.out.println(activeDeck.getStringFormat());
        }
        System.out.println("Other decks:");
        for(Deck deck: user.listOfDecks){
            if(!deck.isActive) System.out.println(deck.getStringFormat());
        }
    }

    public static void showDeck(User user, Deck deck, boolean isSideDeck){
        ArrayList<Card> deckCards;
        if(isSideDeck) deckCards = deck.sideDeck;
        else deckCards = deck.mainDeck;
        ArrayList<String> monsters = new ArrayList<>(), spellsAndTraps = new ArrayList<>();
        for(Card card: deckCards){
            if(card.isMonster()) monsters.add(card.getName() + ": " + card.getDescription());
            else spellsAndTraps.add(card.getName() + ": " + card.getDescription());
        }
        String[] monstersArray = (String[]) monsters.toArray();
        String[] spellsAndTrapsArray = (String[]) spellsAndTraps.toArray();
        Arrays.sort(monstersArray);
        Arrays.sort(spellsAndTrapsArray);
        System.out.println("Deck: " + deck.name);
        if(isSideDeck) System.out.println("Side deck:");
        else System.out.println("Main deck:");

        System.out.println("Monsters:");
        for(String string: monstersArray){
            System.out.println(string);
        }
        System.out.println("Spell and Traps:");
        for(String string: spellsAndTrapsArray){
            System.out.println(string);
        }
    }

    public static void showAllCards(User user){
        ArrayList<Card> allCards = new ArrayList<>(user.cardsInNoDeck);
        for(Deck deck: user.listOfDecks){
            allCards.addAll(deck.mainDeck);
            allCards.addAll(deck.sideDeck);
        }

        ArrayList<String> cardStrings = new ArrayList<>();
        for(Card card: allCards){
            cardStrings.add(card.getName() + ": " + card.getDescription());
        }
        String[] cardsArray = (String[]) cardStrings.toArray();
        Arrays.sort(cardsArray);

        for(String string: cardsArray){
            System.out.println(string);
        }
    }


    @Override
    public void increaseLP(Matcher matcher) {

    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }

}
