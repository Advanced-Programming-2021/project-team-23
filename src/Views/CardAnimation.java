package Views;

import Models.Card;
import Models.CardCoordinates;
import Models.CardLocation;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CardAnimation {


    public ImageView imageView;
    public CardCoordinates cardCoordinates1;
    public CardCoordinates cardCoordinates2;

    public CardAnimation(ImageView imageView, CardCoordinates cardCoordinates1, CardCoordinates cardCoordinates2) {
        this.imageView = imageView;
        this.cardCoordinates1 = cardCoordinates1;
        this.cardCoordinates2 = cardCoordinates2;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(1);
    }

    @Override
    protected void interpolate(double v) {
        imageView.setLayoutX(v * (cardCoordinates2.x - cardCoordinates1.x) + cardCoordinates1.x);
        imageView.setLayoutY(v * (cardCoordinates2.y - cardCoordinates1.y) + cardCoordinates1.y);
    }

}
