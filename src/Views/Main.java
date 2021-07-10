package Views;

import Models.GamePane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Stage stage;
    public static Pane pane;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        stage = mainStage;
        pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/registerMenu.fxml"));
        //pane = FXMLLoader.load(getClass().getResource("/main/resources/fxmls/game.fxml"));
        stage.setScene(new Scene(pane));
//        Pane gamePane = new GamePane(0, null).pane;
//        stage.setScene(new Scene(gamePane));

        stage.show();

        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.show();
    }
}
