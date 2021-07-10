package Views;


import Controllers.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GameStages extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public void start(GameController gameController) throws Exception {
        for(int i = 0; i < 2; i++){
            gameController.stage[i] = new Stage();
            gameController.stage[i].setScene(new Scene(Duel.panes[i].pane));
            gameController.stage[i].show();
        }
    }

    public static void showNewStage(Pane pane){
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.show();
    }

}
