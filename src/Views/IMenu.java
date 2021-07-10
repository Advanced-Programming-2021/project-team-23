package Views;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IMenu {

    public static Scanner scan = new Scanner(System.in);

    public void show();

    public default void run() throws Exception {

        String command;

        while(true){
            command = scan.nextLine();
            if(command.matches("^[ ]*exit[ ]*$")) return;
            processCommand(command);
        }

    }

    void processCommand(String command) throws Exception;

    public default Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

    public static void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
