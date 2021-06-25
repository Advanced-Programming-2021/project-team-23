package Views;

import Models.Card;
import Models.ICheatCode;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportMenu implements IMenu , ICheatCode {

    @Override
    public void processCommand(String command) throws IOException {
        Matcher matcher;
        Pattern pattern = Pattern.compile("^menu show-current$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            System.out.println("import menu");
            return;
        }
        matcher = getCommandMatcher(command, "(import|export) card (.+)");
        if(matcher.matches()){
            String cardName = matcher.group(2);
            Card card = new Card(cardName);
            if(card.getType() == null) {
                System.out.println("there is no card with this name");
                return;
            }
            File file = new File(User.projectAddress + "\\src\\main\\resources\\" + cardName + ".json");
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            PrintWriter printWriter = new PrintWriter(file);
            String jsonString = gson.toJson(card);
            printWriter.write(jsonString);
            printWriter.flush();
            printWriter.close();

            if(command.startsWith("e")){
                System.out.println(jsonString);
            }

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
