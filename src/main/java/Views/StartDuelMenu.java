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
        Matcher matcher;
        matcher = getCommandMatcher(command, "duel new second-player ([\\S]+) round ([\\d]+)");
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
            GameController gameController = new GameController();
            gameController.runGameController(MainMenu.currentUser, user, rounds);
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
            GameController gameController = new GameController();
            gameController.isAI = true;
            User aiUser = AI.getAIUser();
            gameController.runGameController(MainMenu.currentUser, aiUser, rounds);
        }
    }

    @Override
    public void increaseLP(Matcher matcher) {

    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }

}
