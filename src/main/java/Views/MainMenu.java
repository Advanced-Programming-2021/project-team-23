package Views;

import Models.ICheatCode;
import Models.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu implements IMenu , ICheatCode {

    public static User currentUser;

    public MainMenu(User currentUser){
        MainMenu.currentUser = currentUser;
    }

    @Override
    public void run() throws IOException {

        String command;

        while(true){
            command = scan.nextLine();
            if(command.matches("^[ ]*logout[ ]*$")) {
                System.out.print("user logged out successfully!\n");
                return;
            }
            processCommand(command);
        }

    }

    @Override
    public void processCommand(String command) throws IOException {

        Pattern pattern = Pattern.compile("^menu show-current$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            System.out.println("main menu");
            return;
        }

        pattern = Pattern.compile("^(menu enter [a-z ]*)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            enterMenu(matcher);
            return;
        }

        System.out.print("invalid command\n");
    }

    public void enterMenu(Matcher matcher) throws IOException {


        if(matcher.group(1).equals("menu enter duel menu")){
            StartDuelMenu startDuel = new StartDuelMenu();
            startDuel.run();
            return;
        }

        if(matcher.group(1).equals("menu enter deck menu")){
            DeckMenu deckMenu= new DeckMenu();
            deckMenu.run();
            return;
        }

        if(matcher.group(1).equals("menu enter scoreboard menu")){
            ScoreBoardMenu scoreboardMenu= new ScoreBoardMenu();
            scoreboardMenu.run();
            return;
        }

        if(matcher.group(1).equals("menu enter profile menu")){
            ProfileMenu profileMenu= new ProfileMenu(currentUser);
            profileMenu.run();
            return;
        }

        if(matcher.group(1).equals("menu enter shop menu")){
            ShopMenu shopMenu= new ShopMenu();
            shopMenu.run();
            return;
        }

        if(matcher.group(1).equals("menu enter import menu")){
            ImportMenu importMenu = new ImportMenu();
            importMenu.run();
            return;
        }

        System.out.print("menu navigation is not possible\n");
    }

    @Override
    public void increaseLP(Matcher matcher) {

    }


    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
