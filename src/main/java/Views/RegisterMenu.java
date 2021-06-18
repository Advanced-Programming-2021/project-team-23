package Views;

import Models.ICheatCode;
import Models.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu implements IMenu, ICheatCode {

    @Override
    public void processCommand(String command) throws IOException {
        Pattern pattern = Pattern.compile("^user login (?:(?:(username [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (p [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (u [A-Za-z0-9]+)))$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            login(matcher);
            return;
        }

        pattern = Pattern.compile("^user create (?:(?:(username [A-Za-z0-9]+) (password [A-Za-z0-9]+) (nickname [A-Za-z0-9]+))|(?:(username [A-Za-z0-9]+) (nickname [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (username [A-Za-z0-9]+) (nickname [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (nickname [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(nickname [A-Za-z0-9]+) (password [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(nickname [A-Za-z0-9]+) (username [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (p [A-Za-z0-9]+) (n [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (n [A-Za-z0-9]+) (p [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (u [A-Za-z0-9]+) (n [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (n [A-Za-z0-9]+) (u [A-Za-z0-9]+))|(?:(n [A-Za-z0-9]+) (p [A-Za-z0-9]+) (u [A-Za-z0-9]+))|(?:(n [A-Za-z0-9]+) (u [A-Za-z0-9]+) (p [A-Za-z0-9]+)))$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            signup(matcher);
            return;
        }

        pattern = Pattern.compile("^menu enter [A-Za-z ]*$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            System.out.println("please login first");
            return;
        }

        pattern = Pattern.compile("^menu show-current$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            System.out.println("Register Menu");
            return;
        }

        System.out.println("invalid command");
    }


    public void signup(Matcher matcher) throws IOException {
        String username = null;
        String password = null;
        String nickname = null;

        int i = 1;
        while (i < 37) {
            if (matcher.group(i) != null) {
                if (matcher.group(i).startsWith("p")) {
                    password = matcher.group(i);
                    password = password.replaceAll("password ", "");
                    password = password.replaceAll("p ", "");
                }
                if (matcher.group(i).startsWith("n")) {
                    nickname = matcher.group(i);
                    nickname = nickname.replaceAll("nickname ", "");
                    nickname = nickname.replaceAll("n ", "");
                } if (matcher.group(i).startsWith("u")) {
                    username = matcher.group(i);
                    username = username.replaceAll("username ", "");
                    username = username.replaceAll("u ", "");
                }
            }
            i = i + 1;
        }

        //System.out.println(username);

        boolean uniqueUsername = checkUsernameExistenceForSignup(username);
        if (uniqueUsername) {
            boolean uniqueNickname = checkNicknameExistenceForSignup(nickname);
            if (uniqueNickname) {
                System.out.println("registered successfully");
                User newUser = new User(username, password, nickname);
                MainMenu mainMenu = new MainMenu(newUser);
                mainMenu.run();
            }
        }
    }

    public void login(Matcher matcher) throws IOException {

        String username = null;
        String password = null;

        int i = 1;
        while (i < 9) {
            if (matcher.group(i) != null) {
                if (matcher.group(i).startsWith("p")) {
                    password = matcher.group(i);
                    password = password.replaceAll("password ", " ");
                    password = password.replaceAll("p ", "");
                } else {
                    username = matcher.group(i);
                    username = username.replaceAll("username ", "");
                    username = username.replaceAll("u ", "");
                }
            }
            i = i + 1;
        }

        boolean checkUser = verifyInformation(username, password);
        if (checkUser) {
            User loggedInUser = getUserInstanceForLogin(username, password);
            MainMenu mainMenu = new MainMenu(loggedInUser);
            mainMenu.run();
        }
    }


    //signup functions
    public static boolean checkUsernameExistenceForSignup(String username) throws IOException {
        if(User.getUserByUsername(username) != null){
            System.out.println("this username is already used");
            return false;
        }
        return true;
    }

    public static boolean checkNicknameExistenceForSignup(String nickname) throws IOException {
        if(User.getUserByNickname(nickname) != null){
            System.out.println("this nickname is already used");
            return false;
        }
        return true;
    }

    public static boolean checkPasswordStrength(String password){
        return true;
    }

    public static boolean verifyInformation(String username, String password) throws IOException {
        if(User.getUserByUsernameAndPassword(username, password) == null){
            System.out.println("something went wrong with username or password");
            return false;
        }

        return true;
    }


    public static User getUserInstanceForLogin(String username, String password) throws IOException {
        if(User.getUserByUsernameAndPassword(username, password) != null){
            return User.getUserByUsername(username);
        }

        else return null;
    }

    @Override
    public void increaseLP(Matcher matcher) {

    }

    @Override
    public void increaseMoney(Matcher matcher) {

    }
}
