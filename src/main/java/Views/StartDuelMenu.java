package Views;

import Controllers.AI;
import Controllers.GameController;
import Models.ICheatCode;
import Models.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartDuelMenu implements IMenu, ICheatCode {

    @Override
    public void processCommand(String command) throws IOException {
        if (command.matches("^menu show-current$")) {
            System.out.println("duel menu");
            return;
        }
        Matcher matcher;
        matcher = getCommandMatcher(command, "duel new second-player (.+) rounds ([\\d]+)");
        if(matcher.find()){
            String username = matcher.group(1);
            int rounds = Integer.parseInt(matcher.group(2));
            User user = User.getUserByUsername(username);
            if(user == null){
                System.out.println("there is no player with this username");
                return;
            }
            if(MainMenu.currentUser.activeDeck == null){
                System.out.println(MainMenu.currentUser.username + " has no active deck");
                return;
            }
            if(user.activeDeck == null){
                System.out.println(user.username + " has no active deck");
                return;
            }
            if(rounds != 1 && rounds != 3){
                System.out.println("number of rounds is not supported");
                return;
            }
            GameController gameController = new GameController(MainMenu.currentUser, user, rounds);
            gameController.runGameController(MainMenu.currentUser, user, rounds);
            return;
        }
        matcher = getCommandMatcher(command, "duel new ai rounds ([\\d]+)");
        if(matcher.find()){
            if(MainMenu.currentUser.activeDeck == null){
                System.out.println("you have no active deck");
                return;
            }
            int rounds = Integer.parseInt(matcher.group(1));
            if(rounds != 1 && rounds != 3){
                System.out.println("number of rounds is not supported");
                return;
            }
            User aiUser = AI.getAIUser();
            GameController gameController = new GameController(MainMenu.currentUser, aiUser, rounds);
            gameController.isAI = true;
            gameController.runGameController(MainMenu.currentUser, aiUser, rounds);
            return;
        }
        System.out.println("invalid command");
    }

    @Override
    public void increaseLP(Matcher matcher) {

    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }

}
