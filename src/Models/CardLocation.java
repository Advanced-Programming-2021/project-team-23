package Models;

public class CardLocation {

    public double downX;
    public double downY;
    public double upX;
    public double upY;
    public static double cardWidth = 55.000000;
    public static double cardRatioOfWidthToHeight = 421.000000 / 614;
    public static double smallDistance = 0.1;

    public CardLocation(double downX, double downY, double upX, double upY) {
        this.downX = downX;
        this.downY = downY;
        this.upX = upX;
        this.upY = upY;
    }

    //        213    295  370  446  528  604    693
//    105
//    201
//    321
//    425

    public static double[] x = new double[]{446, 528, 370, 604, 295};
    public static double[] y = new double[]{105, 201, 321, 425};

    public static CardLocation getCardLocationByPlace(String place){
        CardLocation cardLocation = new CardLocation(0, 0, 0, 0);

        if(place.startsWith("hand")){
            int index = Integer.parseInt(place.substring(5)) - 1;
            cardLocation.downX = 283 + index * 64;
            cardLocation.downY = 530;
            cardLocation.upX = cardLocation.downX;
            cardLocation.upY = -6;
        }
        if(place.startsWith("3")) {
            cardLocation.downX = 693;
            cardLocation.downY = y[2];
            cardLocation.upX = 213;
            cardLocation.upY = y[1];
        }
        if(place.startsWith("4")) {
            cardLocation.downX = 693;
            cardLocation.downY = 425;
            cardLocation.upX = 213;
            cardLocation.upY = 105;
        }
        if(place.startsWith("5")){
            cardLocation.downX = 213;
            cardLocation.downY = 425;
            cardLocation.upX = 693;
            cardLocation.upY = 105;
        }
        if(place.startsWith("1") || place.startsWith("2")){
            int firstIndex = Integer.parseInt(place.substring(0, 1)) - 1;
            int secondIndex = Integer.parseInt(place.substring(2));
            cardLocation.downX = x[secondIndex - 1];
            cardLocation.downY = y[firstIndex + 2];
            cardLocation.upX = cardLocation.downX;
            if(secondIndex == 2 || secondIndex == 3) cardLocation.upX = x[4 - secondIndex];
            if(secondIndex == 4 || secondIndex == 5) cardLocation.upX = x[8 - secondIndex];
            cardLocation.upY = y[1 - firstIndex];
        }
        return cardLocation;
    }
}

