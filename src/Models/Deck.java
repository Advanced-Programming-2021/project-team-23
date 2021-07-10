package Models;

import Views.IMenu;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


public class Deck {

    public String name;
    public boolean isActive;

    public ArrayList<Card> mainDeck;
    public ArrayList<Card> sideDeck;

    public Deck(String name){
        this.name = name;
        mainDeck = new ArrayList<>();
        sideDeck = new ArrayList<>();
    }

    public static Deck getDeckByName(String deckName) throws IOException {
        File users = new File(User.projectAddress + "\\src\\main\\resources\\users");
        File[] filesList = users.listFiles();
        for(File file : filesList) {
            Gson gson = new Gson();
            User aUser = gson.fromJson(Files.readString(file.toPath()), User.class);
            for(Deck deck: aUser.listOfDecks){
                if(deck.name.equals(deckName)) return deck;
            }
        }
        return null;
    }

    public boolean isValid(){
        return mainDeck.size() >= 40;
    }

    public int getNumberOfCardByName(String cardName){
        int count = 0;
        for(Card card: mainDeck){
            if(card.getName().equals(cardName)) count++;
        }
        for(Card card: sideDeck){
            if(card.getName().equals(cardName)) count++;
        }
        return count;
    }

    public Card getCardByNameInMainDeck(String cardName){
        for(Card card: mainDeck){
            if(card.getName().equals(cardName)) return card;
        }
        return null;
    }

    public Card getCardByNameInSideDeck(String cardName){
        for(Card card: sideDeck){
            if(card.getName().equals(cardName)) return card;
        }
        return null;
    }

    public String getStringFormat(){
        String string = name + ": " + "main deck " + mainDeck.size() + ", side deck " + sideDeck.size() + ", ";
        if(this.isValid()) string += "valid";
        else string += "invalid";
        return string;
    }

}
