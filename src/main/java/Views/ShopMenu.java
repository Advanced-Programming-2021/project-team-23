package Views;

import Models.Card;
import Models.ICheatCode;
import Models.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu implements IMenu , ICheatCode {

    @Override
    public void processCommand(String command) {
        Matcher matcher;
        matcher = getCommandMatcher(command, "shop buy (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(1);
            try {
                buyCard(cardName, MainMenu.currentUser);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        if(command.equals("shop show all") || command.equals("shop all show")){
            showAllCards();
            return;
        }
        System.out.println("invalid command");
    }

    public void buyCard(String cardName, User user) throws Exception{
        Card card = Card.getCardByName(cardName);
        if(card == null){
            throw new Exception("there is no card with this name");
        }
        if(card.getPrice() > user.wallet){
            throw new Exception("not enough money");
        }
        user.wallet -= card.getPrice();
        user.cardsInNoDeck.add(card);
    }

    public void showAllCards(){
        FileReader fileReader;
        String[] row;
        ArrayList<Card> allCards = new ArrayList<>();
        try {
            fileReader = new FileReader("D:\\project-team23\\src\\main\\resources\\Monster.csv");
            CSVReader csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                allCards.add(new Card(row[0]));
            }
            fileReader = new FileReader("D:\\project-team23\\src\\main\\resources\\SpellTrap.csv");
            csvReader = new CSVReader(fileReader);
            while((row = csvReader.readNext()) != null){
                allCards.add(new Card(row[0]));
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        String[] cardStrings = new String[allCards.size()];
        Card card;
        for(int i = 0; i < allCards.size(); i++){
            card = allCards.get(i);
            cardStrings[i] = card.getName() + ": " + card.getPrice();
        }
        Arrays.sort(cardStrings);
        for(String string: cardStrings){
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
