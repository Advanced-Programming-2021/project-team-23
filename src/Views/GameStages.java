package Views;


import Controllers.GameController;
import Models.User;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class GameStages extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    }

    public void start(GameController gameController) throws Exception {
        for(int i = 0; i < 2; i++){
            gameController.stage[i] = new Stage();
            gameController.stage[i].setScene(new Scene(Duel.panes[i].pane));
        }
        gameController.stage[0].show();

        deleteTVForUser(Duel.gameController.users[0]);
        Duel.panes[0].pane.setOnMouseMoved(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnMouseClicked(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnMouseEntered(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnMouseExited(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnMousePressed(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnMouseReleased(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnScroll(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnScrollStarted(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.setOnScrollFinished(mouseEvent -> saveScreenInFile());
        Duel.panes[0].pane.requestFocus();
    }

    public void saveScreenInFile(){
        WritableImage writableImage = Duel.panes[0].pane.snapshot(new SnapshotParameters(), null);
        File file = new File(User.projectAddress + "\\src\\main\\resources\\TV\\" + Duel.gameController.users[0].username + "_" + (Duel.screenIndex++) + ".png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTVForUser(User user){
        try {
            File file = new File(User.projectAddress + "\\src\\main\\resources\\TV");
            File[] files = file.listFiles();
            assert files != null;
            for(File file1: files){
                if(file1.getName().startsWith(user.username + "_")) file1.delete();
            }
        } catch (Exception ignored){

        }
    }

    public static void showNewStage(Pane pane){
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.show();
    }

}
