package Views;

import Controllers.CardController;
import Models.Board;
import Models.Card;

import java.util.ArrayList;
import java.util.Scanner;

public class GameView {

    static Scanner scanner = new Scanner(System.in);


    public static ArrayList<Card> getCardsByAddressFromZone(Board board, int zoneNumber, String numberOfCards) {
        // zoneNumber must be 1 or 2
        while (true) {
            System.out.println("please enter " + numberOfCards + " address from 1 to 5 : ");
            String inputString = scanner.nextLine();

            String[] digits = inputString.split("[ ,]+");
            ArrayList<Card> cards = new ArrayList<>();

            boolean isAnyCardNull = false;

            for (String digit : digits) {
                Card card = board.getCardByPlace(zoneNumber + "_" + digit);
                if (card == null) isAnyCardNull = true;
                else cards.add(card);
            }

            if (isAnyCardNull) {
                if (numberOfCards.charAt(0) == '1') {
                    System.out.println("there is no card on this address");
                } else {
                    System.out.println("there is no card on one of these addresses");
                }
            } else {
                return cards;
            }
        }
    }

    public static int howManyCardsDoesPlayerWantToDestroy(int maximumNumberPossible){
        while(true) {
            System.out.println("how many cards do you want to destroy ? please enter a positive number up to " + maximumNumberPossible);
            String input = scanner.nextLine();
            int number = Integer.parseInt(input);
            if (number < 1 || number > maximumNumberPossible){
                System.out.println("invalid number");
            } else {
                return number;
            }
        }
    }

    public static ArrayList<Card> getMonstersFromGraveyard(Board board, int numberOfCards) {
        while (true) {
            System.out.println("please enter " + numberOfCards + " positive number to get card from graveyard : ");
            String inputString = scanner.nextLine();

            String[] digits = inputString.split("[ ,]+");
            ArrayList<Card> cards = new ArrayList<>();

            boolean isAnyCardNull = false;
            boolean isAnyCardNotMonster = false;

            for (String digit : digits) {
                Card card = board.getCardByPlace(3 + "_" + digit);
                if (card == null) isAnyCardNull = true;
                else {
                    if (!card.getType().startsWith("Monster")) isAnyCardNotMonster = true;
                    else cards.add(card);
                }
            }

            if (isAnyCardNull) {
                if (numberOfCards == 1) {
                    System.out.println("there is no card on this address");
                } else {
                    System.out.println("there is no card on one of these addresses");
                }
            } else {
                if (isAnyCardNotMonster) {
                    if (numberOfCards == 1) {
                        System.out.println("the card chosen is not a monster");
                    } else {
                        System.out.println("one of the cards chosen is not a monster");
                    }
                } else {
                    return cards;
                }
            }
        }
    }

    public static Card getAMonsterInGraveyardsFromUser(Board myBoard, Board opponentBoard) {
        if(!CardController.arrayListContainsMonster(myBoard.getGraveyard())){
            System.out.println("you should choose a card from opponent graveyard");
            return getMonstersFromGraveyard(opponentBoard, 1).get(0);
        }
        if(!CardController.arrayListContainsMonster(opponentBoard.getGraveyard())){
            System.out.println("you should choose a card from your own graveyard");
            return getMonstersFromGraveyard(myBoard, 1).get(0);
        }
        System.out.println("do you want to choose the monster from your own graveyard ?");
        String input = scanner.nextLine();
        if (input.equals("yes")) {
            return getMonstersFromGraveyard(myBoard, 1).get(0);
        }
        else return getMonstersFromGraveyard(opponentBoard, 1).get(0);
    }


    public static int getACardNumberInHandFromUser(Board board) {
        ArrayList<Card> cardsInHand = board.getCardsInHand();
        while (true) {
            System.out.println("please enter a number from 1 to 6 to choose a card from hand");
            String input = scanner.nextLine();
            if (cardsInHand.get(Integer.parseInt(input) - 1) == null) {
                System.out.println("there is no card on this address");
            } else {
                return Integer.parseInt(input);
            }
        }
    }

    public static String getZoneNumber(String possibleZones){
        // possibleZones may contains 1, 2, 3, 4, 5, hand
        System.out.println("where do want to choose a card from ? please enter " + possibleZones);
        return scanner.nextLine();
    }

    public static String getACardNameFromUser(){
        System.out.println("please enter a card name :");
        return scanner.nextLine();
    }

    public static boolean isThereAnyMonsterInGraveyardAndPrintMessage(Board board) {
        ArrayList<Card> graveyard = board.getGraveyard();
        if (cardsOfArrayListAreAllNull(graveyard)) {
            System.out.println("graveyard is empty");
            return false;
        }
        for (Card card : graveyard) {
            if (card.getType().startsWith("Monster")) return true;
        }
        System.out.println("there is no monster in graveyard");
        return false;
    }


    public static void printThatThereIsNoMonsterWithType(String type){
        System.out.println("there is no monster with type " + type);
    }


    public static boolean doesUserWantToUseOptionalNumberForTributes(int number) {
        System.out.println("do you want to summon this card with " + number + " tributes ?");
        String input = scanner.nextLine();  // yes or no
        return input.equals("yes");
    }

    public static boolean doesUserWantToUseEffectOfCard(Card card) {
        System.out.println("do you want to use effect of " + card.getName() + " right now ?");
        String input = scanner.nextLine();  // yes or no
        return input.equals("yes");
    }


    public static boolean doesUserWantToDestroyHisCard(Card card){
        System.out.println("do you want to destroy your card " + card.getName() + " ?");
        String input = scanner.nextLine();  // yes or no
        return input.equals("yes");
    }

    public static boolean doesPlayerWantToSetCardInDefensiveMode(){
        System.out.println("how do you like to set your card ? defensive(d) or attacking(a) ?");
        String input = scanner.nextLine();
        return (input.startsWith("d"));
    }

    public static boolean cardsOfArrayListAreAllNull(ArrayList<Card> arrayList) {
        for (Card card : arrayList) {
            if (card != null) return false;
        }
        return true;
    }

}
