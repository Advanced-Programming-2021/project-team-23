package Views;

import Models.ICheatCode;
import Models.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

public class ScoreBoardMenu implements IMenu , ICheatCode {

    @Override
    public void processCommand(String command) throws IOException {
        if (command.matches("^menu show-current$")) {
            System.out.println("scoreboard menu");
            return;
        }
        if(command.equals("scoreboard show")){
            ArrayList<User> users = sortUsers();
            int rank = 1;
            for(int i = 0; i < users.size(); i++){
                if(i > 0 && users.get(i).getScore() < users.get(i - 1).getScore()) rank++;
                System.out.println(rank + "- " + users.get(i).getNickname() + ": " + users.get(i).getScore());
            }
        } else {
            System.out.println("invalid command");
        }
    }

    public ArrayList<User> sortUsers() throws IOException {
        ArrayList<User> users = User.getAllUsers();
        for(int i = 0; i < users.size() - 1; i++){
            for(int j = i + 1; j < users.size(); j++){
                if((users.get(i).getScore() < users.get(j).getScore()) ||
                        ((users.get(i).getScore() == users.get(j).getScore()) && (users.get(i).getNickname().compareToIgnoreCase(users.get(j).getNickname()) > 0))){
                    Collections.swap(users, i, j);
                }
            }
        }
        return users;
    }

    @Override
    public void increaseLP(Matcher matcher) {

    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
