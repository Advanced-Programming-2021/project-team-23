import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.regex.Matcher;

public class RegisterMenuTest {
    @Test
    public void testGetCommandMatcher() throws FileNotFoundException {
        PrintStream outputForTest = new PrintStream(new File("1.txt"));
        System.setOut(outputForTest);


    }

}
