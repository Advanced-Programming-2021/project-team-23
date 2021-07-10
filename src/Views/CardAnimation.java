package Views;

import Models.Card;
import Models.CardCoordinates;
import Models.CardLocation;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CardAnimation {


    public static void translate(ImageView imageView, CardCoordinates cardCoordinates1, CardCoordinates cardCoordinates2){
//        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), imageView);
//        translateTransition.setFromX(cardCoordinates1.x);
//        translateTransition.setFromY(cardCoordinates1.y);
//        translateTransition.setToX(cardCoordinates2.x);
//        translateTransition.setToY(cardCoordinates2.y);
//        translateTransition.play();
        imageView.setLayoutX(cardCoordinates2.x);
        imageView.setLayoutY(cardCoordinates2.y);
    }

}
