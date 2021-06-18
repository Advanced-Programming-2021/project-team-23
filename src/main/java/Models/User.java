package Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class User{

    public String username;
    public String password;
    public String nickname;
    public int score;
    public int wallet;

    public ArrayList<Card> cardsInNoDeck;
    public ArrayList<Deck> listOfDecks;
    public Deck activeDeck;

    public User(String username, String password, String nickname) throws FileNotFoundException {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = 0;
        this.wallet = 100000;
        cardsInNoDeck = new ArrayList<>();
        listOfDecks = new ArrayList<>();
        fillCardsInNoDeck();
        createDefaultDeck();
        setUserInFile(this);
    }

    public static void setUserInFile(User user) throws FileNotFoundException {
        File file = new File("D:\\project-team23\\src\\main\\resources\\users\\" + user.username + ".json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(gson.toJson(user));

        printWriter.flush();
        printWriter.close();
    }

    public static ArrayList<User> getAllUsers() throws IOException {
        ArrayList<User> allUsers = new ArrayList<>();
        File users = new File("D:\\project-team23\\src\\main\\resources\\users");
        File[] filesList = users.listFiles();
        Gson gson = new Gson();
        for(File file : filesList) {
            allUsers.add(gson.fromJson(Files.readString(file.toPath()), User.class));
        }
        return allUsers;
    }

    public static User getUserByUsername(String username) throws IOException {
        File users = new File("D:\\project-team23\\src\\main\\resources\\users");
        File[] filesList = users.listFiles();
        for(File file : filesList) {
            if(file.getName().startsWith(username)){
                Gson gson = new Gson();
                return gson.fromJson(Files.readString(file.toPath()), User.class);
            }
        }
        return null;
    }

    public static User getUserByNickname(String nickname) throws IOException {
        File users = new File("D:\\project-team23\\src\\main\\resources\\users");
        File[] filesList = users.listFiles();
        for(File file : filesList) {
            Gson gson = new Gson();
            User aUser = gson.fromJson(Files.readString(file.toPath()), User.class);
            if(aUser.nickname.equals(nickname)) return aUser;
        }
        return null;
    }

    public static User getUserByUsernameAndPassword(String username, String password) throws IOException {
        File users = new File("D:\\project-team23\\src\\main\\resources\\users");
        File[] filesList = users.listFiles();
        for(File file : filesList) {
            Gson gson = new Gson();
            User aUser = gson.fromJson(Files.readString(file.toPath()), User.class);
            if(aUser.username.equals(username) && aUser.password.equals(password)) return aUser;
        }
        return null;
    }

    public void addDeck(Deck deck) throws FileNotFoundException {
        this.listOfDecks.add(deck);
        setUserInFile(this);
    }

    public void deleteDeck(Deck deck) throws FileNotFoundException {
        cardsInNoDeck.addAll(deck.mainDeck);
        cardsInNoDeck.addAll(deck.sideDeck);
        listOfDecks.remove(deck);
        if(activeDeck == deck) activeDeck = null;
        setUserInFile(this);
    }

    public void addCardToDeck(Card card, Deck deck, boolean isSideDeck) throws FileNotFoundException {
        cardsInNoDeck.remove(card);
        if(isSideDeck) deck.sideDeck.add(card);
        else deck.mainDeck.add(card);
        setUserInFile(this);
    }

    public Deck getDeckByName(String deckName){
        for(Deck deck: listOfDecks){
            if(deck.name.equals(deckName)) return deck;
        }
        return null;
    }

    public Card getCardByNameFromCardsInNoDeck(String cardName){
        for(Card card: cardsInNoDeck){
            if(card.getName().equals(cardName)) return card;
        }
        return null;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int amount) {
        this.score = amount;
    }

    public String getUsername(){
        return this.username;
    }

    public String getNickname(){
       return this.nickname;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }


    public void changePassword(String newPassword) {

    }

    public void changeNickname(String newNickname) {

    }


    public void fillCardsInNoDeck(){
        FileReader fileReader;
        String[] row;
        try {
            fileReader = new FileReader("D:\\project-team23\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(!(row[0].equals("Name"))) cardsInNoDeck.add(new Card(row[0]));
            }
            fileReader = new FileReader("D:\\project-team23\\src\\main\\resources\\SpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                if(!(row[0].equals("Name"))) cardsInNoDeck.add(new Card(row[0]));
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void createDefaultDeck(){
        Deck deck = new Deck(username + " deck");
        for(int i = 0; i < 35; i++){
            deck.mainDeck.add(cardsInNoDeck.get(i));
        }
        for(int i = 50; i < 75; i++){
            deck.mainDeck.add(cardsInNoDeck.get(i));
        }
        for(int i = 35; i < 50; i++){
            deck.sideDeck.add(cardsInNoDeck.get(i));
        }
        listOfDecks.add(deck);
    }

}
