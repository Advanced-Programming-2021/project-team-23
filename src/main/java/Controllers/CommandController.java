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

public class CommandController  extends GameController {

    public String select(Matcher inputMatcher) throws Exception {
        String address = inputMatcher.group(1);
        Matcher matcher, matcher1;
        Board myBoard = getBoard(getCurrentPlayer());
        Board opponentBoard = getBoard(getCurrentPlayer() + 1);
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
                setSelectedCard(card);
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
                setSelectedCard(card);
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
                setSelectedCard(card);
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
                setSelectedCard(card);
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
                setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "field");
        if(matcher.matches()){
            Card card = myBoard.getFieldZone().get(0);
            if(card == null){
                throw new Exception("no card found in the given position");
            } else {
                setSelectedCard(card);
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
                setSelectedCard(card);
                return ("card selected");
            }
        }
        matcher = getCommandMatcher(address, "-d");
        if(matcher.matches()){
            if(getSelectedCard() == null){
                throw new Exception("no card is selected yet");
            } else {
                setSelectedCard(null);
                return ("card deselected");
            }
        }
        throw new Exception("invalid selection");
    }

    public String summon() throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if((!getBoard(getCurrentPlayer()).getCardsInHand().contains(getSelectedCard())) ||
                (!getSelectedCard().getType().startsWith("Monster"))){
            throw new Exception("you can't summon this card");
        }
        if(getPhaseNumber() != 3 && getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if(CardController.arrayListOfCardsIsFull(getBoard(getCurrentPlayer()).getMonsters(), 5)){
            throw new Exception("monster card zone is full");
        }
        if(isCardSummonedOrSetInTurn()){
            throw new Exception("you already summoned/set on this turn");
        }

        lastActions[getCurrentPlayer()] = "summon";
        lastCards[getCurrentPlayer()] = getSelectedCard();
        if(Action.canEffectBeActivatedForCard(this, getSelectedCard(), null)){
            Action.runActionForCard(this, getSelectedCard(), null);
        }

        int numberOfTributes = getSelectedCard().getNumberOfTributesNeeded();
        if(numberOfTributes > 0) {
            if (!CardController.areThereEnoughCardsInMonsterZoneToTribute(getBoard(getCurrentPlayer()), numberOfTributes)) {
                throw new Exception("there are not enough cards for tribute");
            }
            if(isAI){
                for(int i = 0; i < numberOfTributes; i++){
                    Card tribute = AI.getLowestAttackMonster(boards[currentPlayer].getMonsters());
                    CardController.moveCardToGraveyard(getBoard(getCurrentPlayer()), tribute);
                }
            } else {
                ArrayList<Card> tributes = GameView.
                        getCardsByAddressFromZone(getBoard(getCurrentPlayer()), 1,
                                String.valueOf(numberOfTributes));
                for (Card tribute : tributes) {
                    CardController.moveCardToGraveyard(getBoard(getCurrentPlayer()), tribute);
                }
            }
        }
        CardController.moveCardFromFirstArrayToSecondArray(getSelectedCard(),
                getBoard(getCurrentPlayer()).getCardsInHand(),
                getBoard(getCurrentPlayer()).getMonsters(), "1");
        getSelectedCard().setMode("OO");
        setCardSummonedOrSetInTurn(true);
        setSelectedCard(null);

        createAndRunChain();

        chain.clear();
        lastCards[0] = null;
        lastCards[1] = null;
        lastActions[0] = null;
        lastActions[1] = null;
        return ("summoned successfully");
    }

    public String set() throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getBoard(getCurrentPlayer()).getCardsInHand().contains(getSelectedCard())){
            throw new Exception("you can't set this card");
        }
        if(getPhaseNumber() != 3 && getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if(getSelectedCard().getType().startsWith("Monster")) return setMonster();
        if(getSelectedCard().getType().startsWith("Spell") ||
                getSelectedCard().getType().startsWith("Trap")) {
            return setSpellOrTrap();
        }
        return null;
    }

    public String setMonster() throws Exception {
        if(CardController.arrayListOfCardsIsFull(getBoard(getCurrentPlayer()).getMonsters(), 5)){
            throw new Exception("monster card zone is full");
        }
        if(isCardSummonedOrSetInTurn()){
            throw new Exception("you already summoned/set on this turn");
        }
        CardController.moveCardFromFirstArrayToSecondArray(getSelectedCard(),
                getBoard(getCurrentPlayer()).getCardsInHand(),
                getBoard(getCurrentPlayer()).getMonsters(), "1");
        getSelectedCard().setMode("DH");
        setCardSummonedOrSetInTurn(true);
        getSelectedCard().setPositionChangedInTurn(true);
        setSelectedCard(null);
        return ("set successfully");
    }

    public String setSpellOrTrap() throws Exception {
        if(CardController.arrayListOfCardsIsFull(getBoard(getCurrentPlayer()).getSpellsAndTraps(), 5)){
            throw new Exception("spell card zone is full");
        }
        CardController.moveCardFromFirstArrayToSecondArray(getSelectedCard(),
                getBoard(getCurrentPlayer()).getCardsInHand(),
                getBoard(getCurrentPlayer()).getSpellsAndTraps(), "2");
        getSelectedCard().setMode("H");
        setSelectedCard(null);
        return ("set successfully");
    }


    public String changePosition(String position) throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getBoard(getCurrentPlayer()).getMonsters().contains(getSelectedCard())){
            throw new Exception("you can't change this card position");
        }
        if(getPhaseNumber() != 3 && getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if((position.equals("attack") && (!getSelectedCard().getMode().equals("DO"))) ||
                (position.equals("defense") && (!getSelectedCard().getMode().equals("OO")))){
            throw new Exception("this card is already in the wanted position");
        }
        if(getSelectedCard().isPositionChangedInTurn()){
            throw new Exception("you already changed this card position in this turn");
        }
        String newMode;
        if(position.equals("attack")) newMode = "OO";
        else newMode = "DO";
        getSelectedCard().setMode(newMode);
        getSelectedCard().setPositionChangedInTurn(true);
        setSelectedCard(null);
        return ("monster card position changed successfully");
    }


    public String flipSummon() throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getBoard(getCurrentPlayer()).getMonsters().contains(getSelectedCard())){
            throw new Exception("this card is not in your monster zone");
        }
        if(getPhaseNumber() != 3 && getPhaseNumber() != 5){
            throw new Exception("action not allowed in this phase");
        }
        if((!getSelectedCard().getMode().equals("DH")) ||
                getSelectedCard().isPositionChangedInTurn()){
            throw new Exception("you can't flip summon this card");
        }
        getSelectedCard().setMode("OO");

        lastActions[getCurrentPlayer()] = "flipSummon";
        lastCards[getCurrentPlayer()] = getSelectedCard();
        if(Action.canEffectBeActivatedForCard(this, getSelectedCard(), null)){
            Action.runActionForCard(this, getSelectedCard(), null);
        }

        setSelectedCard(null);

        createAndRunChain();

        chain.clear();
        lastCards[0] = null;
        lastCards[1] = null;
        lastActions[0] = null;
        lastActions[1] = null;
        return ("flip summoned successfully");
    }

    public String attack(String number) throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getBoard(getCurrentPlayer()).getMonsters().contains(getSelectedCard())){
            throw new Exception("you can't attack with this card");
        }
        if(getPhaseNumber() != 4){
            throw new Exception("action not allowed in this phase");
        }
        if(getSelectedCard().hasAttackedInTurn()){
            throw new Exception("this card already attacked");
        }
        Card opponentCard = getBoard(getCurrentPlayer() + 1)
                .getMonsters().get(Integer.parseInt(number) - 1);
        if(opponentCard == null){
            throw new Exception("there is no card to attack here");
        }
        if(!getBoard(getCurrentPlayer()).canMonstersAttack()){
            throw new Exception("monsters of this board can not attack");
        }

        String message = null;

        if(opponentCard.canAnyoneAttack() &&
                getSelectedCard().canThisCardAttack() &&
                getBoard(getCurrentPlayer()).canMonstersAttack()) {

            getSelectedCard().setAttackedInTurn(true);

            lastActions[getCurrentPlayer()] = "attack";
            lastCards[getCurrentPlayer()] = getSelectedCard();
            lastCards[1 - getCurrentPlayer()] = opponentCard;
            if(Action.canEffectBeActivatedForCard(this, opponentCard, getSelectedCard())){
                chain.add(new Card[]{opponentCard, getSelectedCard()});
            }
            createAndRunChain();


            if (opponentCard.getMode().equals("OO")) {
                int myAttack = getSelectedCard().getAttack();
                int opponentAttack = opponentCard.getAttack();
                if (myAttack > opponentAttack) {
                    increaseLp(getCurrentPlayer() + 1, opponentAttack - myAttack);
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(getBoard(getCurrentPlayer() + 1), opponentCard);
                    message = ("your opponent's monster is destroyed and your opponent receives "
                            + (myAttack - opponentAttack) + " battle damage");
                }
                if (myAttack == opponentAttack) {
                    if(getSelectedCard().canBeDestroyed()) CardController.moveCardToGraveyard(getBoard(getCurrentPlayer()), getSelectedCard());
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(getBoard(getCurrentPlayer() + 1), opponentCard);
                    message = ("both you and your opponent monster cards are destroyed and no one receives damage");
                }
                if (myAttack < opponentAttack) {
                    increaseLp(getCurrentPlayer(), myAttack - opponentAttack);
                    if(getSelectedCard().canBeDestroyed()) CardController.moveCardToGraveyard(getBoard(getCurrentPlayer()), getSelectedCard());
                    message = ("your monster card is destroyed and you received "
                            + (opponentAttack - myAttack) + " battle damage");
                }
            }
            if (opponentCard.getMode().startsWith("D")) {
                int myAttack = getSelectedCard().getAttack();
                int opponentDefense = opponentCard.getDefense();
                if (myAttack > opponentDefense) {
                    if(opponentCard.canBeDestroyed()) CardController.moveCardToGraveyard(getBoard(getCurrentPlayer() + 1), opponentCard);
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
                    increaseLp(getCurrentPlayer(), myAttack - opponentDefense);
                    if (opponentCard.getMode().equals("DH")) {
                        message = ("opponent's monster card was " + opponentCard.getName());
                    }
                    message += ("\nno card is destroyed and you received " +
                            (opponentDefense - myAttack) + " battle damage");
                }
            }

            setIsDamageToPlayersCalculated(true);

            if (Action.canEffectBeActivatedForCard(this, opponentCard, getSelectedCard())) {
                Action.runActionForCard(this, opponentCard, getSelectedCard());
            }

            setIsDamageToPlayersCalculated(false);
        }

        opponentCard.setCanAnyoneAttack(true);
        getSelectedCard().setCanThisCardAttack(true);

        setSelectedCard(null);

        chain.clear();
        lastCards[0] = null;
        lastCards[1] = null;
        lastActions[0] = null;
        lastActions[1] = null;
        return message;
    }

    public String directAttack() throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getBoard(getCurrentPlayer()).getMonsters().contains(getSelectedCard())){
            throw new Exception("you can't attack with this card");
        }
        if(getPhaseNumber() != 4){
            throw new Exception("action not allowed in this phase");
        }
        if(getSelectedCard().hasAttackedInTurn()){
            throw new Exception("this card already attacked");
        }
        if(CardController.cardsOfArrayListAreAllNull(getBoard(getCurrentPlayer() + 1).getMonsters())){
            throw new Exception("you can't attack the opponent directly");
        }
        getSelectedCard().setAttackedInTurn(true);
        int damage = getSelectedCard().getAttack();
        increaseLp(getCurrentPlayer() + 1, -damage);
        setSelectedCard(null);
        return ("your opponent receives " + (damage) + " battle damage");
    }


    public String activateSpell() throws Exception {
        if(getSelectedCard() == null){
            throw new Exception("no card is selected yet");
        }
        if(!getSelectedCard().getType().startsWith("Spell")){
            throw new Exception("activate effect is only for spell cards");
        }
        if(getPhaseNumber() != 3 && getPhaseNumber() != 5){
            throw new Exception("you can't activate an effect on this turn");
        }
        if(getSelectedCard().getMode().equals("O")){
            throw new Exception("you have already activated this card");
        }
        if(getBoard(getCurrentPlayer()).getCardsInHand().contains(getSelectedCard()) &&
                (!getSelectedCard().getType().contains("Field")) &&
                CardController.arrayListOfCardsIsFull(getBoard(getCurrentPlayer()).getSpellsAndTraps(), 5)){
            throw new Exception("spell card zone is full");
        }
        if(!Action.canEffectBeActivatedForCard(this, getSelectedCard(), null)){
            if(getSelectedCard().getType().contains("Ritual")){
                throw new Exception("there is no way you could ritual summon a monster");
            }
            throw new Exception("preparations of this spell are not done yet");
        }
        String message = "spell activated";
        getSelectedCard().setIsEffectActive(true);
        getSelectedCard().setMode("O");
        Board board = getBoard(getCurrentPlayer());
        if(getSelectedCard().getType().contains("Field") && getSelectedCard().getPlace().startsWith("hand")){
            ArrayList<Card> fieldZone = board.getFieldZone();
            if(fieldZone.get(0) != null){
                CardController.moveCardToGraveyard(board, getSelectedCard());
            }
            CardController.moveCardFromFirstArrayToSecondArray(getSelectedCard(), board.getCardsInHand(), fieldZone, "5");
        } else {
            if(getSelectedCard().getPlace().startsWith("hand")) {
                CardController.moveCardFromFirstArrayToSecondArray(getSelectedCard(), board.getCardsInHand(), board.getSpellsAndTraps(), "2");
            }
        }

        Card card = getSelectedCard();

        lastActions[getCurrentPlayer()] = "activateSpell";
        lastCards[getCurrentPlayer()] = getSelectedCard();

        if(card.getType().contains("Ritual")) new DuelMenu().ritualSummon(card);
        else {
            chain.add(new Card[]{card, null});
            createAndRunChain();
        }

        setSelectedCard(null);

        chain.clear();
        lastCards[0] = null;
        lastCards[1] = null;
        lastActions[0] = null;
        lastActions[1] = null;
        return message;
    }



    public Matcher getCommandMatcher(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

}
