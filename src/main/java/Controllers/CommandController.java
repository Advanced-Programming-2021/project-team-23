package Controllers;

import Controllers.action.Action;
import Controllers.action.AdvancedRitualArt;
import Models.Board;
import Models.Card;
import Views.DuelMenu;
import Views.GameView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandController {

    public GameController gameController;

    public CommandController(GameController gameController){
        this.gameController = gameController;
    }

    public String select(Matcher inputMatcher) throws Exception {
        String address = inputMatcher.group(1);
        Matcher matcher, matcher1;
        Board myBoard = gameController.getBoard(gameController.getCurrentPlayer());
        Board opponentBoard = gameController.getBoard(gameController.getCurrentPlayer() + 1);
        matcher = getCommandMatcher(address, "monster (\\d)");
        if(matcher.matches()){
            int number = Integer.parseInt(matcher.group(1));
            if(number < 1 || number > 5) {
                throw new Exception("invalid selection");
            }
            Card card = myBoard.getMonsters().get(number - 1);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "spell (\\d)");
        if(matcher.matches()){
            int number = Integer.parseInt(matcher.group(1));
            if(number < 1 || number > 5) {
                throw new Exception("invalid selection");
            }
            Card card = myBoard.getSpellsAndTraps().get(number - 1);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "monster opponent (\\d)");
        matcher1 = getCommandMatcher(address, "opponent monster (\\d)");
        if(matcher.matches() || matcher1.matches()){
            int number;
            if(matcher.matches()) number = Integer.parseInt(matcher.group(1));
            else number = Integer.parseInt(matcher1.group(1));
            if(number < 1 || number > 5) {
                throw new Exception("invalid selection");
            }
            Card card = opponentBoard.getMonsters().get(number - 1);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "spell opponent (\\d)");
        matcher1 = getCommandMatcher(address, "opponent spell (\\d)");
        if(matcher.matches() || matcher1.matches()){
            int number;
            if(matcher.matches()) number = Integer.parseInt(matcher.group(1));
            else number = Integer.parseInt(matcher1.group(1));
            if(number < 1 || number > 5) {
                throw new Exception("invalid selection");
            }
            Card card = opponentBoard.getSpellsAndTraps().get(number - 1);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "field opponent");
        matcher1 = getCommandMatcher(address, "opponent field");
        if(matcher.matches() || matcher1.matches()){
            Card card = opponentBoard.getFieldZone().get(0);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "field");
        if(matcher.matches()){
            Card card = myBoard.getFieldZone().get(0);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "hand (\\d)");
        if(matcher.matches()){
            ArrayList<Card> cardsInHand = myBoard.getCardsInHand();
            int number = Integer.parseInt(matcher.group(1));
            if(number < 1 || number > 6 || cardsInHand.size() < number) {
                throw new Exception("invalid selection");
            }
            Card card = myBoard.getCardsInHand().get(number - 1);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                gameController.setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "-d");
        if(matcher.matches()){
            if(gameController.getSelectedCard() == null){
                throw new Exception("no card is selected yet");
            } else {
                gameController.setSelectedCard(null);
                return ("card deselected");
            }
        }
        throw new Exception("invalid selection");
    }

    public String summon() throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if((!gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand().contains(gameController.getSelectedCard())) ||
                (!gameController.getSelectedCard().getType().startsWith("Monster"))){
            throw new Exception("you can't summon this card");
        }
        if(gameController.getPhaseNumber() != 3 && gameController.getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if(CardController.arrayListOfCardsIsFull(gameController.getBoard(gameController.getCurrentPlayer()).getMonsters(), 5)){
            throw new Exception("monster card zone is full");
        }
        if(gameController.isCardSummonedOrSetInTurn()){
            throw new Exception("you already summoned/set on this turn");
        }

        gameController.lastActions[gameController.getCurrentPlayer()] = "summon";
        gameController.lastCards[gameController.getCurrentPlayer()] = gameController.getSelectedCard();
        if(Action.canEffectBeActivatedForCard(gameController, gameController.getSelectedCard(), null)){
            Action.runActionForCard(gameController, gameController.getSelectedCard(), null);
        }

        int numberOfTributes = gameController.getSelectedCard().getNumberOfTributesNeeded();
        if(numberOfTributes > 0) {
            if (!CardController.areThereEnoughCardsInMonsterZoneToTribute(gameController.getBoard(gameController.getCurrentPlayer()), numberOfTributes)) {
                throw new Exception("there are not enough cards for tribute");
            }
            if(gameController.isAI){
                for(int i = 0; i < numberOfTributes; i++){
                    Card tribute = AI.getLowestAttackMonster(gameController.boards[gameController.currentPlayer].getMonsters());
                    CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer()), tribute);
                }
            } else {
                ArrayList<Card> tributes = GameView.
                        getCardsByAddressFromZone(gameController.getBoard(gameController.getCurrentPlayer()), 1,
                                String.valueOf(numberOfTributes));
                for (Card tribute : tributes) {
                    CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer()), tribute);
                }
            }
        }
        CardController.moveCardFromFirstArrayToSecondArray(gameController.getSelectedCard(),
                gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand(),
                gameController.getBoard(gameController.getCurrentPlayer()).getMonsters(), "1");
        gameController.getSelectedCard().setMode("OO");
        gameController.setCardSummonedOrSetInTurn(true);
        gameController.setSelectedCard(null);

        gameController.createAndRunChain();

        gameController.chain.clear();
        gameController.lastCards[0] = null;
        gameController.lastCards[1] = null;
        gameController.lastActions[0] = null;
        gameController.lastActions[1] = null;
        return ("summoned successfully");
    }

    public String set() throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand().contains(gameController.getSelectedCard())){
            throw new Exception("you can't set this card");
        }
        if(gameController.getPhaseNumber() != 3 && gameController.getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if(gameController.getSelectedCard().getType().startsWith("Monster")) return setMonster();
        if(gameController.getSelectedCard().getType().startsWith("Spell") ||
                gameController.getSelectedCard().getType().startsWith("Trap")) {
            return setSpellOrTrap();
        }
        return null;
    }

    public String setMonster() throws Exception {
        if(CardController.arrayListOfCardsIsFull(gameController.getBoard(gameController.getCurrentPlayer()).getMonsters(), 5)){
            throw new Exception("monster card zone is full");
        }
        if(gameController.isCardSummonedOrSetInTurn()){
            throw new Exception("you already summoned/set on this turn");
        }
        CardController.moveCardFromFirstArrayToSecondArray(gameController.getSelectedCard(),
                gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand(),
                gameController.getBoard(gameController.getCurrentPlayer()).getMonsters(), "1");
        gameController.getSelectedCard().setMode("DH");
        gameController.setCardSummonedOrSetInTurn(true);
        gameController.getSelectedCard().setPositionChangedInTurn(true);
        gameController.setSelectedCard(null);
        return ("set successfully");
    }

    public String setSpellOrTrap() throws Exception {
        if(CardController.arrayListOfCardsIsFull(gameController.getBoard(gameController.getCurrentPlayer()).getSpellsAndTraps(), 5)){
            throw new Exception("spell card zone is full");
        }
        CardController.moveCardFromFirstArrayToSecondArray(gameController.getSelectedCard(),
                gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand(),
                gameController.getBoard(gameController.getCurrentPlayer()).getSpellsAndTraps(), "2");
        gameController.getSelectedCard().setMode("H");
        gameController.setSelectedCard(null);
        return ("set successfully");
    }


    public String changePosition(String position) throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).getMonsters().contains(gameController.getSelectedCard())){
            throw new Exception("you can't change this card position");
        }
        if(gameController.getPhaseNumber() != 3 && gameController.getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if((position.equals("attack") && (!gameController.getSelectedCard().getMode().equals("DO"))) ||
                (position.equals("defense") && (!gameController.getSelectedCard().getMode().equals("OO")))){
            throw new Exception("this card is already in the wanted position");
        }
        if(gameController.getSelectedCard().isPositionChangedInTurn()){
            throw new Exception("you already changed this card position in this turn");
        }
        String newMode;
        if(position.equals("attack")) newMode = "OO";
        else newMode = "DO";
        gameController.getSelectedCard().setMode(newMode);
        gameController.getSelectedCard().setPositionChangedInTurn(true);
        gameController.setSelectedCard(null);
        return ("monster card position changed successfully");
    }


    public String flipSummon() throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).getMonsters().contains(gameController.getSelectedCard())){
            throw new Exception("this card is not in your monster zone");
        }
        if(gameController.getPhaseNumber() != 3 && gameController.getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if((!gameController.getSelectedCard().getMode().equals("DH")) ||
                gameController.getSelectedCard().isPositionChangedInTurn()){
            throw new Exception("you can't flip summon this card");
        }
        gameController.getSelectedCard().setMode("OO");

        gameController.lastActions[gameController.getCurrentPlayer()] = "flipSummon";
        gameController.lastCards[gameController.getCurrentPlayer()] = gameController.getSelectedCard();
        if(Action.canEffectBeActivatedForCard(gameController, gameController.getSelectedCard(), null)){
            Action.runActionForCard(gameController, gameController.getSelectedCard(), null);
        }

        gameController.setSelectedCard(null);

        gameController.createAndRunChain();

        gameController.chain.clear();
        gameController.lastCards[0] = null;
        gameController.lastCards[1] = null;
        gameController.lastActions[0] = null;
        gameController.lastActions[1] = null;
        return ("flip summoned successfully");
    }

    public String attack(String number) throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).getMonsters().contains(gameController.getSelectedCard())){
            throw new Exception("you can't attack with this card");
        }
        if(gameController.getPhaseNumber() != 4){
            throw new Exception("action not allowed in this phase");
        }
        if(gameController.getSelectedCard().hasAttackedInTurn()){
            throw new Exception("this card already attacked");
        }
        Card opponentCard = gameController.getBoard(gameController.getCurrentPlayer() + 1)
                .getMonsters().get(Integer.parseInt(number) - 1);
        if(opponentCard == null){
            throw new Exception("there is no card to attack here");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).canMonstersAttack()){
            throw new Exception("monsters of this board can not attack");
        }

        String message = null;

        if(opponentCard.canAnyoneAttack() &&
                gameController.getSelectedCard().canThisCardAttack() &&
                gameController.getBoard(gameController.getCurrentPlayer()).canMonstersAttack()) {

            gameController.getSelectedCard().setAttackedInTurn(true);

            gameController.lastActions[gameController.getCurrentPlayer()] = "attack";
            gameController.lastCards[gameController.getCurrentPlayer()] = gameController.getSelectedCard();
            gameController.lastCards[1 - gameController.getCurrentPlayer()] = opponentCard;
            if(Action.canEffectBeActivatedForCard(gameController, opponentCard, gameController.getSelectedCard())){
                gameController.chain.add(new Card[]{opponentCard, gameController.getSelectedCard()});
            }
            gameController.createAndRunChain();


            if (opponentCard.getMode().equals("OO")) {
                int myAttack = gameController.getSelectedCard().getAttack();
                int opponentAttack = opponentCard.getAttack();
                if (myAttack > opponentAttack) {
                    gameController.increaseLp(gameController.getCurrentPlayer() + 1, opponentAttack - myAttack);
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer() + 1), opponentCard);
                    message = ("your opponent's monster is destroyed and your opponent receives "
                            + (myAttack - opponentAttack) + " battle damage");
                }
                if (myAttack == opponentAttack) {
                    if(gameController.getSelectedCard().canBeDestroyed()) CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer()), gameController.getSelectedCard());
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer() + 1), opponentCard);
                    message = ("both you and your opponent monster cards are destroyed and no one receives damage");
                }
                if (myAttack < opponentAttack) {
                    gameController.increaseLp(gameController.getCurrentPlayer(), myAttack - opponentAttack);
                    if(gameController.getSelectedCard().canBeDestroyed()) CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer()), gameController.getSelectedCard());
                    message = ("your monster card is destroyed and you received "
                            + (opponentAttack - myAttack) + " battle damage");
                }
            }
            if (opponentCard.getMode().startsWith("D")) {
                int myAttack = gameController.getSelectedCard().getAttack();
                int opponentDefense = opponentCard.getDefense();
                if (myAttack > opponentDefense) {
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(gameController.getBoard(gameController.getCurrentPlayer() + 1), opponentCard);
                    if (opponentCard.getMode().equals("DH")) {
                        message = ("opponent's monster card was " + opponentCard.getName());
                    }
                    message += ("\nthe defense position monster is destroyed");
                }
                if (myAttack == opponentDefense) {
                    if (opponentCard.getMode().equals("DH")) {
                        message = ("opponent's monster card was " + opponentCard.getName());
                    }
                    message += ("\nno card is destroyed");
                }
                if (myAttack < opponentDefense) {
                    gameController.increaseLp(gameController.getCurrentPlayer(), myAttack - opponentDefense);
                    if (opponentCard.getMode().equals("DH")) {
                        message = ("opponent's monster card was " + opponentCard.getName());
                    }
                    message += ("\nno card is destroyed and you received " +
                            (opponentDefense - myAttack) + " battle damage");
                }
            }

            gameController.setIsDamageToPlayersCalculated(true);

            if (Action.canEffectBeActivatedForCard(gameController, opponentCard, gameController.getSelectedCard())) {
                Action.runActionForCard(gameController, opponentCard, gameController.getSelectedCard());
            }

            gameController.setIsDamageToPlayersCalculated(false);
        }

        opponentCard.setCanAnyoneAttack(true);
        gameController.getSelectedCard().setCanThisCardAttack(true);

        gameController.setSelectedCard(null);

        gameController.chain.clear();
        gameController.lastCards[0] = null;
        gameController.lastCards[1] = null;
        gameController.lastActions[0] = null;
        gameController.lastActions[1] = null;
        return message;
    }

    public String directAttack() throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getBoard(gameController.getCurrentPlayer()).getMonsters().contains(gameController.getSelectedCard())){
            throw new Exception("you can't attack with this card");
        }
        if(gameController.getPhaseNumber() != 4){
            throw new Exception("action not allowed in this phase");
        }
        if(gameController.getSelectedCard().hasAttackedInTurn()){
            throw new Exception("this card already attacked");
        }
        if(!CardController.cardsOfArrayListAreAllNull(gameController.getBoard(gameController.getCurrentPlayer() + 1).getMonsters())){
            throw new Exception("you can't attack the opponent directly");
        }
        gameController.getSelectedCard().setAttackedInTurn(true);
        int damage = gameController.getSelectedCard().getAttack();
        gameController.increaseLp(gameController.getCurrentPlayer() + 1, -damage);
        gameController.setSelectedCard(null);
        return ("your opponent receives " + (damage) + " battle damage");
    }


    public String activateSpell() throws Exception {
        if(gameController.getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!gameController.getSelectedCard().getType().startsWith("Spell")){
            throw new Exception("activate effect is only for spell cards");
        }
        if(gameController.getPhaseNumber() != 3 && gameController.getPhaseNumber() != 5){
            throw new Exception("you can't activate an effect on this turn");
        }
        if(gameController.getSelectedCard().getMode() != null &&
                gameController.getSelectedCard().getMode().equals("O")){
            throw new Exception("you have already activated this card");
        }
        if(gameController.getBoard(gameController.getCurrentPlayer()).getCardsInHand().contains(gameController.getSelectedCard()) &&
                (!gameController.getSelectedCard().getType().contains("Field")) &&
                CardController.arrayListOfCardsIsFull(gameController.getBoard(gameController.getCurrentPlayer()).getSpellsAndTraps(), 5)){
            throw new Exception("spell card zone is full");
        }
        if(!Action.canEffectBeActivatedForCard(gameController, gameController.getSelectedCard(), null)){
            if(gameController.getSelectedCard().getType().contains("Ritual")){
                throw new Exception("there is no way you could ritual summon a monster");
            }
            throw new Exception("preparations of this spell are not done yet");
        }
        String message = "spell activated";
        gameController.getSelectedCard().setIsEffectActive(true);
        gameController.getSelectedCard().setMode("O");
        Board board = gameController.getBoard(gameController.getCurrentPlayer());
        if(gameController.getSelectedCard().getType().contains("Field") && gameController.getSelectedCard().getPlace().startsWith("hand")){
            ArrayList<Card> fieldZone = board.getFieldZone();
            if(fieldZone.get(0) != null){
                CardController.moveCardToGraveyard(board, fieldZone.get(0));
            }
            CardController.moveCardFromFirstArrayToSecondArray(gameController.getSelectedCard(), board.getCardsInHand(), fieldZone, "5");
        } else {
            if(gameController.getSelectedCard().getPlace().startsWith("hand")) {
                CardController.moveCardFromFirstArrayToSecondArray(gameController.getSelectedCard(), board.getCardsInHand(), board.getSpellsAndTraps(), "2");
            }
        }

        Card card = gameController.getSelectedCard();

        gameController.lastActions[gameController.getCurrentPlayer()] = "activateSpell";
        gameController.lastCards[gameController.getCurrentPlayer()] = gameController.getSelectedCard();

        if(card.getType().contains("Ritual")) new DuelMenu(gameController).ritualSummon(card);
        else {
            gameController.chain.add(new Card[]{card, null});
            gameController.createAndRunChain();
        }

        gameController.setSelectedCard(null);

        gameController.chain.clear();
        gameController.lastCards[0] = null;
        gameController.lastCards[1] = null;
        gameController.lastActions[0] = null;
        gameController.lastActions[1] = null;
        return message;
    }



    public Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

}
