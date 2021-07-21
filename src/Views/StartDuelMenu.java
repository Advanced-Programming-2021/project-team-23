package Views;

import Controllers.AI;
import Controllers.GameController;
import Models.ICheatCode;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartDuelMenu implements IMenu, ICheatCode {

    public static TextField numberOfRoundsTextField;
    public boolean isFirstPlayer;

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/startDuel.fxml"));
            Main.stage.setScene(new Scene(pane));
            numberOfRoundsTextField = (TextField) pane.getChildrenUnmodifiable().get(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processCommand(String command) throws Exception {
        Matcher matcher;
        matcher = getCommandMatcher(command, "duel new second-player (.+) rounds ([\\d]+)");
        if(matcher.find()){
            String username = matcher.group(1);
            int rounds = Integer.parseInt(matcher.group(2));
            User user = User.getUserByUsername(username);
            if(user == null){
                throw new Exception("there is no player with this username");
            }
            if(MainMenu.currentUser.activeDeck == null){
                throw new Exception(MainMenu.currentUser.username + " has no active deck");
            }
            if(user.activeDeck == null){
                throw new Exception(user.username + " has no active deck");
            }
            if(!MainMenu.currentUser.activeDeck.isValid()){
                throw new Exception(MainMenu.currentUser.username + "'s deck is invalid");
            }
            if(!user.activeDeck.isValid()){
                throw new Exception(user.username + "'s deck is invalid");
            }
            if(rounds != 1 && rounds != 3){
                throw new Exception("number of rounds is not supported");
            }
            GameController gameController = new GameController(user, MainMenu.currentUser, rounds);
            if(isFirstPlayer) gameController = new GameController(MainMenu.currentUser, user, rounds);
            RegisterMenu.gameController = gameController;
            gameController.phaseController(1);
            //gameController.runGameController(MainMenu.currentUser, user, rounds);
            return;
        }
        matcher = getCommandMatcher(command, "duel new ai rounds ([\\d]+)");
        if(matcher.find()){
            if(MainMenu.currentUser.activeDeck == null){
                throw new Exception("you have no active deck");
            }
            if(!MainMenu.currentUser.activeDeck.isValid()){
                throw new Exception("your deck is invalid");
            }
            int rounds = Integer.parseInt(matcher.group(1));
            if(rounds != 1 && rounds != 3){
                throw new Exception("number of rounds is not supported");
            }
            User aiUser = AI.getAIUser();
            GameController gameController = new GameController(MainMenu.currentUser, aiUser, rounds);
            gameController.isAI = true;
            gameController.ai = new AI(gameController);
            RegisterMenu.gameController = gameController;
            gameController.phaseController(1);
            //gameController.runGameController(MainMenu.currentUser, aiUser, rounds);
            return;
        }
        throw new Exception("invalid command");
    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
