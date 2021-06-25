import Controllers.AI;
import Controllers.GameController;
import Controllers.action.SpellAbsorption;
import Models.Card;
import Models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.regex.Matcher;

public class actionTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testRitualSummon() throws FileNotFoundException {
        provideInput("select hand 2\n" +
                "summon\n" +
                "1,2\n");
        User user1 = new User("c", "c", "c");
        User user2 = new User("d", "d", "d");
        user1.activeDeck = AI.getAIUser().activeDeck;
        user2.activeDeck = AI.getAIUser().activeDeck;
        GameController gameController = new GameController(user1, user2, 1);
        for(int i = 0; i < 6; i++) {
            gameController.boards[0].getCardsInHand().set(i, null);
            gameController.boards[1].getCardsInHand().set(i, null);
        }
        for (int i = 0; i < 2; i++) {
            gameController.boards[0].getMonsters().set(i, new Card("Battle OX"));
            gameController.boards[1].getMonsters().set(i, new Card("Battle OX"));
        }
        gameController.boards[0].getCardsInHand().set(0, new Card("Advanced Ritual Art"));
        gameController.boards[0].getCardsInHand().set(1, new Card("Crab Turtle"));

        Matcher matcher = gameController.duelMenu.getCommandMatcher("select hand 1", "select (hand 1)");
        matcher.matches();
        gameController.phaseNumber = 3;
        gameController.currentPlayer = 0;
        gameController.duelMenu.select(matcher);
        gameController.duelMenu.activateSpell();

        Assert.assertTrue(getOutput().contains("summoned successfully"));
    }
}
