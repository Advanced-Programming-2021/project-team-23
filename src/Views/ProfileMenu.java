package Views;

import Models.ICheatCode;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu implements IMenu , ICheatCode {

    public static User currentUser;
    public static Label nicknameLabel;
    public static TextField newNickname;
    public static TextField oldPassword;
    public static TextField newPassword;

    public ProfileMenu(User currentUser){
        ProfileMenu.currentUser = currentUser;
    }

    @Override
    public void show() {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/profile.fxml"));
            newNickname = (TextField) pane.getChildren().get(0);
            oldPassword = (TextField) pane.getChildren().get(1);
            newPassword = (TextField) pane.getChildren().get(2);
            ((Label) pane.getChildren().get(3)).setText("username: " + currentUser.username);
            nicknameLabel = (Label) pane.getChildren().get(4);
            nicknameLabel.setText("nickname: " + currentUser.nickname);
            Main.stage.setScene(new Scene(pane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String command = null;

        while(command == null || !command.matches("^[ ]*exit[ ]*$")){

            System.out.println("----------Profile Menu-----------\n\n");
            System.out.println("Username :  " + currentUser.getUsername() + "\n--------------------");
            System.out.println("Nickname :  " + currentUser.getNickname() + "\n--------------------");
            System.out.println("Score    :  " + currentUser.getScore()    + "\n--------------------");
            System.out.println("Wallet   :  " + currentUser.getWallet()   + "\n--------------------\n\n");

            command = scan.nextLine();
            processCommand(command);
        }
    }

    @Override
    public void processCommand(String command) {
        User user = currentUser;
        try {
            String message = processProfileCommands(command, user);
            if(message != null) System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String processProfileCommands(String command, User user) throws Exception {
        Matcher matcher;

        matcher = getCommandMatcher(command, "profile change nickname (.+)");
        if(matcher.matches()){
            String newNickname = matcher.group(1);
            if(User.getUserByNickname(newNickname) != null){
                throw new Exception("user with nickname " + newNickname + " already exists");
            }
            user.nickname = newNickname;
            User.setUserInFile(user);
            return "nickname changed successfully";
        }

        matcher = getCommandMatcher(command, "profile change password current (.+) new (.+)");
        if(matcher.matches()){
            String currentPassword = matcher.group(1);
            String newPassword = matcher.group(2);

            if(!user.password.equals(currentPassword)){
                throw new Exception("current password is invalid");
            }
            if(user.password.equals(newPassword)){
                throw new Exception("please enter a new password");
            }
            user.password = newPassword;
            User.setUserInFile(user);
            return "password changed successfully";
        }

        Pattern pattern = Pattern.compile("^menu show-current$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            return ("profile menu");
        }

        matcher = increaseMoneyPattern.matcher(command);
        if(matcher.find()) {
            increaseMoney(matcher);
            return null;
        }

        if(command.matches("^[ ]*exit[ ]*$")) return null;

        throw new Exception("invalid command");
    }

    @Override
    public void increaseMoney(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group(1));
        currentUser.setWallet(currentUser.getWallet() + amount);
    }
}
