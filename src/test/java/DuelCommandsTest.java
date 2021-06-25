import Controllers.AI;
import Controllers.GameController;
import Models.Card;
import Models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.regex.Matcher;

public class DuelCommandsTest {

    @Test
    public void test() throws FileNotFoundException {
        User user1 = new User("a", "a", "a");
        User user2 = new User("b", "b", "b");
        user1.activeDeck = AI.getAIUser().activeDeck;
        user2.activeDeck = AI.getAIUser().activeDeck;
        GameController gameController = new GameController(user1, user2, 1);
        for(int i = 0; i < 6; i++) {
            gameController.boards[0].getCardsInHand().set(i, new Card("Silver Fang"));
            gameController.boards[1].getCardsInHand().set(i, new Card("Hero of the east"));
        }
        Matcher matcher = gameController.duelMenu.getCommandMatcher("select hand 1", "select (hand 1)");
        matcher.matches();
        gameController.phaseNumber = 3;
        gameController.currentPlayer = 0;
        gameController.duelMenu.select(matcher);
        gameController.duelMenu.summon();
        gameController.currentPlayer = 1;
        gameController.duelMenu.select(matcher);
        gameController.isCardSummonedOrSetInTurn = false;
        gameController.duelMenu.summon();

        gameController.currentPlayer = 0;
        gameController.phaseNumber = 4;
        matcher = gameController.duelMenu.getCommandMatcher("select monster 1", "select (monster 1)");
        matcher.matches();
        gameController.duelMenu.select(matcher);
        gameController.isCardSummonedOrSetInTurn = false;
        gameController.duelMenu.attack("1");

        Assert.assertEquals(gameController.lp[1], 7900);
    }

}
