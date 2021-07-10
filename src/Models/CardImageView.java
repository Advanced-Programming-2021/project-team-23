package Models;

import Controllers.CardController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

public class CardImageView extends ImageView {

    public Card card;

    public CardImageView(Image image, Card card){
        setImage(image);
        this.card = card;
    }

    public void setEvents(GamePane gamePane){
        setShowCardEvent(gamePane);
        setContextMenuEvent(gamePane);
    }

    public void setShowCardEvent(GamePane gamePane){
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!isCardVisible(gamePane)) return;
                gamePane.cardImage.setImage(card.imageView.getImage());
                String text = card.getName() + "\n";
                if(card.isMonster()) text += ("ATK:" + card.getAttack() + " DEF:" + card.getDefense() + "\n");
                text += "\n";
                text += card.getDescription();
                gamePane.cardLabel.setText(text);
            }
        });
    }

    public boolean isCardVisible(GamePane gamePane){
        Board myBoard = gamePane.gameController.boards[gamePane.playerNumber];
        Board opponentBoard = gamePane.gameController.boards[1 - gamePane.playerNumber];
        if(myBoard.getCardsInHand().contains(card) ||
                myBoard.getMonsters().contains(card) ||
                myBoard.getSpellsAndTraps().contains(card)) return true;
        if((opponentBoard.getMonsters().contains(card) || opponentBoard.getSpellsAndTraps().contains(card)) &&
                card.getMode() != null && !card.getMode().contains("H")) return true;
        return false;
    }

    public void setContextMenuEvent(GamePane gamePane){
        Board myBoard = gamePane.gameController.boards[gamePane.playerNumber];
        Board opponentBoard = gamePane.gameController.boards[1 - gamePane.playerNumber];
        ContextMenu contextMenu = new ContextMenu();
        MenuItem setItem = new MenuItem("set");
        setItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                gamePane.gameController.duel.set();
            }
        });
        MenuItem activateSpellItem = new MenuItem("activate");
        activateSpellItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                gamePane.gameController.duel.activateSpell();
            }
        });
        MenuItem summonItem = new MenuItem("summon");
        summonItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                gamePane.gameController.duel.summon();
            }
        });
        MenuItem flipSummonItem = new MenuItem("flip summon");
        flipSummonItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                gamePane.gameController.duel.flipSummon();
            }
        });
        MenuItem attackItem = new MenuItem("attack");
        attackItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                Card card = CardController.getSomeCardsFromZone(opponentBoard, 1, "1").get(0);
                String position = card.getPlace().substring(2);
                gamePane.gameController.duel.attack(position);
            }
        });
        MenuItem directAttackItem = new MenuItem("direct attack");
        directAttackItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.gameController.selectedCard = card;
                gamePane.gameController.duel.directAttack();
            }
        });

        setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                //if(!(isCardVisible(gamePane) && CardController.boardContainsCard(myBoard, card))) return;
                if(!CardController.boardContainsCard(myBoard, card)) return;
                //if(myBoard.getCardsInHand().contains(card) && !card.getType().contains("Field"))
                if(!card.getType().contains("Field")) contextMenu.getItems().add(setItem);
                //if(card.isSpell() && !(card.getMode() != null && card.getMode().equals("O")))
                if(card.isSpell()) contextMenu.getItems().add(activateSpellItem);
                //if(card.isMonster() && myBoard.getCardsInHand().contains(card))
                if(card.isMonster()) {
                    contextMenu.getItems().add(summonItem);
                    contextMenu.getItems().add(flipSummonItem);
                    contextMenu.getItems().add(directAttackItem);
                    contextMenu.getItems().add(attackItem);
                }
//                if(myBoard.getMonsters().contains(card) && card.getMode().equals("DH"))
//                if(myBoard.getMonsters().contains(card)) {
//                    if(CardController.cardsOfArrayListAreAllNull(opponentBoard.getMonsters())) contextMenu.getItems().add(directAttackItem);
//                    else contextMenu.getItems().add(attackItem);
//                }
                contextMenu.show(CardImageView.this, event.getScreenX(), event.getScreenY());
            }
        });
    }
}
