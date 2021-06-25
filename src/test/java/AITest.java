import Views.IMenu;
import Views.RegisterMenu;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

public class AITest {

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
    public void test() throws IOException {
        provideInput("user create u 1 n 1 p 1\n" +
                "user login username 1 password 1\n" +
                "menu enter deck menu\n" +
                "deck set-active 1 deck\n" +
                "exit\n" +
                "menu enter duel menu\n" +
                "duel new ai rounds 1\n" +
                "next phase\nnext phase\nnext phase\nnext phase\nnext phase\n" +
                "surrender\n" +
                "exit\n" +
                "logout\n" +
                "exit\n");

        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.run();
        IMenu.scan.close();

        String board =
                "1: 8000\n" +
                "    c   c   c   c   c   c   \n" +
                "54\n" +
                "    E   E   E   E   E   \n" +
                "    E   E   E   E   E   \n" +
                "0                       E\n" +
                "\n" +
                "-----------------------------\n" +
                "\n" +
                "E                       0\n" +
                "    E   E   E   E   E   \n" +
                "    E   E   E   E   E   \n" +
                "                        54\n" +
                "    c   c   c   c   c   c   \n" +
                "AI: 8000";

        board = board.replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
        String output = getOutput().replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        Assert.assertTrue(output.contains("deck activated successfully!"));
        Assert.assertTrue(output.contains("Duel Started"));
        Assert.assertTrue(output.contains("it is AI's turn"));
        Assert.assertTrue(output.contains("it is 1's turn"));
        Assert.assertTrue(output.contains(board));

    }

}
