package Views;

import Models.ICheatCode;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu implements IMenu , ICheatCode {

    public static User currentUser;

    public MainMenu(User currentUser){
        MainMenu.currentUser = currentUser;
    }

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/mainMenu.fxml"));
            Main.stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processCommand(String command) {

        Pattern pattern = Pattern.compile("^(menu enter [a-z ]*)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            enterMenu(matcher);
            return;
        }

        System.out.print("invalid command\n");
    }

    public void enterMenu(Matcher matcher){


        if(matcher.group(1).equals("menu enter duel menu")){
            StartDuelMenu startDuel = new StartDuelMenu();
            startDuel.show();
            return;
        }

        if(matcher.group(1).equals("menu enter deck menu")){
            DeckMenu deckMenu= new DeckMenu();
            deckMenu.show();
            return;
        }

        if(matcher.group(1).equals("menu enter scoreboard menu")){
            ScoreBoardMenu scoreboardMenu= new ScoreBoardMenu();
            scoreboardMenu.show();
            return;
        }

        if(matcher.group(1).equals("menu enter profile menu")){
            ProfileMenu profileMenu= new ProfileMenu(currentUser);
            profileMenu.show();
            return;
        }

        if(matcher.group(1).equals("menu enter shop menu")){
            ShopMenu shopMenu= new ShopMenu();
            shopMenu.show();
            return;
        }

        if(matcher.group(1).equals("menu enter import menu")){
            ImportMenu importMenu = new ImportMenu();
            importMenu.show();
            return;
        }

        System.out.print("menu navigation is not possible\n");
    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
