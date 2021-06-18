package Views;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IMenu {

    public Scanner scan = new Scanner(System.in);

    public default void run() throws IOException {

        String command;

        while(true){
            command = scan.nextLine();
            if(command.matches("^[ ]*exit[ ]*$")) return;
            processCommand(command);
        }

    }

    void processCommand(String command) throws IOException;

    public default Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

}
