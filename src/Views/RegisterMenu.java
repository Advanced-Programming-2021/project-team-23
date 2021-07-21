package Views;

import Controllers.GameController;
import Controllers.Server;
import Models.ICheatCode;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu implements IMenu, ICheatCode {

    public static TextField usernameField;
    public static TextField nicknameField;
    public static PasswordField passwordField;

    public static GameController gameController;
    public static User user;

    @Override
    public void show() {
        usernameField = (TextField) Main.pane.getChildrenUnmodifiable().get(1);
        nicknameField = (TextField) Main.pane.getChildrenUnmodifiable().get(2);
        passwordField = (PasswordField) Main.pane.getChildrenUnmodifiable().get(3);
    }

    @Override
    public void processCommand(String command) throws Exception {

    }

    public User processRegisterCommand(String command) throws Exception {
        Pattern pattern = Pattern.compile("^user login (?:(?:(username [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (p [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (u [A-Za-z0-9]+)))$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            return login(matcher);
        }

        pattern = Pattern.compile("^user create (?:(?:(username [A-Za-z0-9]+) (password [A-Za-z0-9]+) (nickname [A-Za-z0-9]+))|(?:(username [A-Za-z0-9]+) (nickname [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (username [A-Za-z0-9]+) (nickname [A-Za-z0-9]+))|(?:(password [A-Za-z0-9]+) (nickname [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(nickname [A-Za-z0-9]+) (password [A-Za-z0-9]+) (username [A-Za-z0-9]+))|(?:(nickname [A-Za-z0-9]+) (username [A-Za-z0-9]+) (password [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (p [A-Za-z0-9]+) (n [A-Za-z0-9]+))|(?:(u [A-Za-z0-9]+) (n [A-Za-z0-9]+) (p [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (u [A-Za-z0-9]+) (n [A-Za-z0-9]+))|(?:(p [A-Za-z0-9]+) (n [A-Za-z0-9]+) (u [A-Za-z0-9]+))|(?:(n [A-Za-z0-9]+) (p [A-Za-z0-9]+) (u [A-Za-z0-9]+))|(?:(n [A-Za-z0-9]+) (u [A-Za-z0-9]+) (p [A-Za-z0-9]+)))$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            return signup(matcher);
        }

        throw new Exception("invalid command");
    }


    public User signup(Matcher matcher) throws Exception {
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


        boolean uniqueUsername = checkUsernameExistenceForSignup(username);
        if (uniqueUsername) {
            boolean uniqueNickname = checkNicknameExistenceForSignup(nickname);
            if (uniqueNickname) {
                return new User(username, password, nickname);
            }
        }
        return null;
    }

    public User login(Matcher matcher) throws Exception {

        String username = null;
        String password = null;

        int i = 1;
        while (i < 9) {
            if (matcher.group(i) != null) {
                if (matcher.group(i).startsWith("p")) {
                    password = matcher.group(i);
                    password = password.replaceAll("password ", "");
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
            return getUserInstanceForLogin(username, password);
        }
        return null;
    }


    //signup functions
    public static boolean checkUsernameExistenceForSignup(String username) throws Exception {
        if(User.getUserByUsername(username) != null){
            throw new Exception("this username is already used");
        }
        return true;
    }

    public static boolean checkNicknameExistenceForSignup(String nickname) throws Exception {
        if(User.getUserByNickname(nickname) != null){
            throw new Exception("this nickname is already used");
        }
        return true;
    }


    public static boolean verifyInformation(String username, String password) throws Exception {
        if(User.getUserByUsernameAndPassword(username, password) == null){
            throw new Exception("something went wrong with username or password");
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
    public void increaseMoney(Matcher matcher) {

    }

    public void signupClicked(MouseEvent mouseEvent) {
        String string = "user create u " + usernameField.getText() + " p " + passwordField.getText() + " n " + nicknameField.getText();
        String result;
        try {
            result = Main.getResult(string);
            if(result.equals("")) {
                IMenu.showSuccess("registered successfully");
                user = User.getUserByUsername(usernameField.getText());
                MainMenu mainMenu = new MainMenu(user);
                mainMenu.show();
            }
            else IMenu.showErrorAlert(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            String string = "user create u " + usernameField.getText() + " p " + passwordField.getText() + " n " + nicknameField.getText();
//            String result = Main.getResult(string);
//            if(result.equals("")) IMenu.showSuccess("registered successfully");
//            else IMenu.showErrorAlert(result);
//        } catch (Exception e) {
//            IMenu.showErrorAlert(e.getMessage());
//            //e.printStackTrace();
//        }
    }

    public void loginClicked(MouseEvent mouseEvent) {
        String string = "user login u " + usernameField.getText() + " p " + passwordField.getText();
        String result;
        try {
            result = Main.getResult(string);
            if(result.equals("")) {
                user = User.getUserByUsername(usernameField.getText());
                MainMenu mainMenu = new MainMenu(user);
                mainMenu.show();
                //System.out.println(user.username);
            }
            else IMenu.showErrorAlert(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            processCommand("user login u " + usernameField.getText() + " p " + passwordField.getText());
//        } catch (Exception e) {
//            IMenu.showErrorAlert(e.getMessage());
//        }
    }




    public void profileMenu(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter profile menu");
    }

    public void deckMenu(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter deck menu");
    }

    public void duelMenu(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter duel menu");
    }

    public void shop(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter shop menu");
    }

    public void scoreboard(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter scoreboard menu");
    }

    public void importAndExport(MouseEvent mouseEvent) {
        new MainMenu(MainMenu.currentUser).processCommand("menu enter import menu");
    }

    public void logout(MouseEvent mouseEvent) {
        try {
            Main.getResult("logout");
            Pane pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/registerMenu.fxml"));
            usernameField = (TextField) pane.getChildrenUnmodifiable().get(1);
            nicknameField = (TextField) pane.getChildrenUnmodifiable().get(2);
            passwordField = (PasswordField) pane.getChildrenUnmodifiable().get(3);
            Main.stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createCard(MouseEvent mouseEvent) {
        CardCreatorMenu cardCreatorMenu = new CardCreatorMenu();
        cardCreatorMenu.show();
    }




    public void nextPhaseClicked(MouseEvent mouseEvent) {
        gameController.phaseNumber = 1 + (gameController.phaseNumber % 6);
        for(int i = 0; i < 2; i++){
            Duel.panes[i].setPhaseLabel();
        }
        gameController.phaseController(gameController.phaseNumber);
        for(int i = 0; i < 2; i++){
            Duel.panes[i].setPhaseLabel();
        }
        if (gameController.isGameEnded()) {
            if (gameController.numberOfRounds == 3) gameController.printWinnerOfMatch();
            gameController.setAwards();
            gameController.stage[0].close();
            gameController.stage[1].close();
            MainMenu mainMenu = new MainMenu(MainMenu.currentUser);
            mainMenu.show();
        }
        if(gameController.phaseNumber == 1){
            if(gameController.currentPlayer == 1 && gameController.isAI) gameController.ai.playAITurn(1);
            gameController.phaseController(1);
        }
    }

    public void pauseClicked(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("exit game?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            gameController.stage[0].close();
            gameController.stage[1].close();
            MainMenu mainMenu = new MainMenu(MainMenu.currentUser);
            mainMenu.show();
        }
    }




    public void playWithAI(MouseEvent mouseEvent) {
        StartDuelMenu startDuelMenu = new StartDuelMenu();
        try {
            startDuelMenu.processCommand("duel new ai rounds " + StartDuelMenu.numberOfRoundsTextField.getText());
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void playWithAnotherUser(MouseEvent mouseEvent) {
        StartDuelMenu startDuelMenu = new StartDuelMenu();
        try {
            String string = Main.getResult("duel " + StartDuelMenu.numberOfRoundsTextField.getText());
            if(string.startsWith("0")) startDuelMenu.isFirstPlayer = true;
            startDuelMenu.processCommand("duel new second-player " + string.substring(2) + " rounds " + StartDuelMenu.numberOfRoundsTextField.getText());
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }


    public void back(MouseEvent mouseEvent) {
        endThreads();
        new MainMenu(MainMenu.currentUser).show();
    }


    public void editDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        deckMenu.showDeck();
    }

    public void deleteDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck delete " + DeckMenu.currentDeck.name, MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setTableView();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void activateDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck set-active " + DeckMenu.currentDeck.name, MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setTableView();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void createNewDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck create " + DeckMenu.newDeckNameTextField.getText(), MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setTableView();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }


    public void removeCardFromMainDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck rm-card card " + DeckMenu.currentCardImageView.card.getName() + " deck " + DeckMenu.currentDeck.name, MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setCards();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void addCardToMainDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck add-card card " + DeckMenu.cardFromMainDeck.getText() + " deck " + DeckMenu.currentDeck.name, MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setCards();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void removeCardFromSideDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck rm-card card " + DeckMenu.currentCardImageView.card.getName() + " deck " + DeckMenu.currentDeck.name + " side", MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setCards();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void addCardToSideDeck(MouseEvent mouseEvent) {
        DeckMenu deckMenu = new DeckMenu();
        try {
            String message = deckMenu.processDeckCommands("deck add-card card " + DeckMenu.cardFromSideDeck.getText() + " deck " + DeckMenu.currentDeck.name + " side", MainMenu.currentUser);
            IMenu.showSuccess(message);
            deckMenu.setCards();
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }



    public void changeNickname(MouseEvent mouseEvent) {
        ProfileMenu profileMenu = new ProfileMenu(MainMenu.currentUser);
        try {
            String message = profileMenu.processProfileCommands("profile change nickname " + ProfileMenu.newNickname.getText(), MainMenu.currentUser);
            ProfileMenu.nicknameLabel.setText("nickname: " + ProfileMenu.newNickname.getText());
            IMenu.showSuccess(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }

    public void changePassword(MouseEvent mouseEvent) {
        ProfileMenu profileMenu = new ProfileMenu(MainMenu.currentUser);
        String oldPassword = ProfileMenu.oldPassword.getText();
        String newPassword = ProfileMenu.newPassword.getText();
        try {
            String message = profileMenu.processProfileCommands("profile change password current " + oldPassword + " new " + newPassword, MainMenu.currentUser);
            IMenu.showSuccess(message);
        } catch (Exception e) {
            IMenu.showErrorAlert(e.getMessage());
        }
    }


    public void showGraveyard0(MouseEvent mouseEvent) {
        gameController.duel.showGraveyard(0);
    }

    public void showGraveyard1(MouseEvent mouseEvent) {
        gameController.duel.showGraveyard(1);
    }



    public void enterLobby(MouseEvent mouseEvent) {
        ChatRoom.user = MainMenu.currentUser;
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.show();
    }


    private void endThreads() {

    }


    public void watchTVClicked(MouseEvent mouseEvent) {
        TV tv = new TV();
        tv.show();
    }
}
