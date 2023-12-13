import java.util.*;

public class GameLogic {
    public String[] dealCards(String[] hand, int nums[]) {
        String cards[] = {
                "\uD83C\uDCA1", "\uD83C\uDCA2", "\uD83C\uDCA3",
                "\uD83C\uDCA4", "\uD83C\uDCA5", "\uD83C\uDCA6",
                "\uD83C\uDCA7", "\uD83C\uDCA8", "\uD83C\uDCA9",
                "\uD83C\uDCAA", "\uD83C\uDCAB", "\uD83C\uDCAD",
                "\uD83C\uDCAE", // spades

                "\uD83C\uDCB1", "\uD83C\uDCB2", "\uD83C\uDCB3",
                "\uD83C\uDCB4", "\uD83C\uDCB5", "\uD83C\uDCB6",
                "\uD83C\uDCB7", "\uD83C\uDCB8", "\uD83C\uDCB9",
                "\uD83C\uDCBA", "\uD83C\uDCBB", "\uD83C\uDCBD",
                "\uD83C\uDCBE", // hearts

                "\uD83C\uDCC1", "\uD83C\uDCC2", "\uD83C\uDCC3",
                "\uD83C\uDCC4", "\uD83C\uDCC5", "\uD83C\uDCC6",
                "\uD83C\uDCC7", "\uD83C\uDCC8", "\uD83C\uDCC9",
                "\uD83C\uDCCA", "\uD83C\uDCCB", "\uD83C\uDCCD",
                "\uD83C\uDCCE", // diamonds

                "\uD83C\uDCD1", "\uD83C\uDCD2", "\uD83C\uDCD3",
                "\uD83C\uDCD4", "\uD83C\uDCD5", "\uD83C\uDCD6",
                "\uD83C\uDCD7", "\uD83C\uDCD8", "\uD83C\uDCD9",
                "\uD83C\uDCDA", "\uD83C\uDCDB", "\uD83C\uDCDD",
                "\uD83C\uDCDE" // clubs
        };

        for(int i = 0; i < 3; i++){
            hand[i] = cards[nums[i]];
        }

        return hand;
    }

    public String[] cardsID(int nums[], String hand[]){
        String cardsID [] = {
                "s1", "s2", "s3",
                "s4", "s5", "s6",
                "s7", "s8", "s9",
                "s10", "s11", "s12",
                "s13", // spades

                "h1", "h2", "h3",
                "h4", "h5", "h6",
                "h7", "h8", "h9",
                "h10", "h11", "h12",
                "h13", //hearts

                "d1", "d2", "d3",
                "d4", "d5", "d6",
                "d7", "d8", "d9",
                "d10", "d11", "d12",
                "d13", //diamonds

                "c1", "c2", "c3",
                "c4", "c5", "c6",
                "c7", "c8", "c9",
                "c10", "c11", "c12",
                "c13" //clubs
        };

        for(int i = 0; i < hand.length; i++){
            hand[i] = cardsID[nums[i]];
        }

        return hand;

    }

    public String handType (int hand){
        String handType = "";
        if(hand == 1) handType = "High Card";
        else if(hand == 2) handType= "Pair";
        else if(hand == 3) handType = "Flush";
        else if(hand == 4) handType = "Straight";
        else if(hand == 5) handType = "Three of a Kind";
        else if(hand == 6) handType = "Straight Flush";

        return handType;
    }

    public int determineHand(String cardsID[]) {
        int hand = 0;
        Character flushCheck[] = new Character[3];
        int pairCheck[] = new int[3];
        int straightCheck[] = new int[3];
        boolean flush = false;
        boolean straight = false;
        boolean threeKind = false;
        boolean pair = false;


        for (int i = 0; i < cardsID.length; i++) {
            flushCheck[i] = cardsID[i].charAt(0);
            if(cardsID[i].length() == 3){
                pairCheck[i] = Integer.parseInt(cardsID[i].substring(1));
                straightCheck[i] = Integer.parseInt(cardsID[i].substring(1));
            }
            else{
                pairCheck[i] = Integer.parseInt(String.valueOf(cardsID[i].charAt(1)));
                straightCheck[i] = Integer.parseInt(String.valueOf(cardsID[i].charAt(1)));
            }

        }

        if(pairCheck[0] == pairCheck[1] || pairCheck[0] == pairCheck[2] || pairCheck[1] == pairCheck[2]){
            if(pairCheck[0] == pairCheck[1] && pairCheck[0] == pairCheck[2]){
                hand = 5; //three of a kind
                threeKind = true;
            }else{
                hand = 2; //pair
                pair = true;
            }

        }

        flush = flushChecker(flushCheck);
        straight = straightChecker(straightCheck);

        if(flush == true && straight == true){
            hand = 6; //straight flush
        }
        else if(flush == true && straight == false){
            hand = 3; //straight
        }
        else if(straight == true && flush == false){
            hand = 4; //flush
        }
        else if(!straight && !flush && !threeKind && !pair){
            hand = 1; //high card
        }


        return hand;
    }

    public int[] generateRandomNumbers(int[] randomNums) {
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            int randomNum;
            do {
                randomNum = rand.nextInt(52);
            } while (checkIfContained(randomNum, randomNums) == true);

            randomNums[i] = randomNum;
        }
        System.out.println("CARDS" + randomNums[0] + " " + randomNums[1] + " "  + randomNums[2] );
        return randomNums;
    }
    public boolean checkIfContained(int num, int[] arr){
        for(int i = 0; i < 3; i++){
            if(arr[i] == num){
                return true;
            }
        }


        return false;
    }

    public boolean straightChecker(int[] arr){
        Arrays.sort(arr);
        if(arr[0] + 1 == arr[1] && arr[1] + 1 == arr[2]){
            return true;
        }
        else if(arr[0] == 1 && arr[1] ==12 && arr[2] == 13){
            return true;
        }

        return false;
    }

    public boolean flushChecker(Character[] flushCheck){
        if(flushCheck[0] == flushCheck[1] && flushCheck[0] == flushCheck[2]){
            return true;
        }

        return false;
    }

    public String determineWinner(String[] playerID, String[] dealerID){
        String winner = "";
        int pHand = 0;
        int dHand = 0;
        pHand = determineHand(playerID);
        dHand = determineHand(dealerID);

        System.out.println(pHand + "VS" + dHand);

        if(pHand > dHand){
            winner = "player";
        }
        else if(dHand > pHand) {
            winner = "dealer";
        }
        //determine better high card
        else if(pHand == 1 && pHand == 1){
            winner = highCardWinner(playerID, dealerID);
        }
        //determine better pair
        else if(pHand == 2 && dHand ==2){
            winner = pairWinner(playerID, dealerID);
        }
        //determine better flush
        else if(pHand == 3 && dHand ==3){
            winner = highCardWinner(playerID, dealerID);

            //determine better straight
        } else if(pHand == 4 && dHand ==4){
            winner = straightWinner(playerID, dealerID);
        }
        else if(pHand ==5 && dHand ==5){
            winner = higher3Kind(playerID, dealerID);
        }
        //determine better straight flush
        else if(pHand ==6 && dHand == 6){
            winner = straightWinner(playerID, dealerID);
        }


        return winner;
    }

    public String highCardWinner(String[] playerID, String[] dealerID){
        String winner  = "";
        boolean highAce = false;
        int dNums[] = new int[3];
        int pNums[] = new int[3];
        for(int i = 0; i < 3; i++){
            pNums[i] = Integer.parseInt(playerID[i].substring(1));
            dNums[i] = Integer.parseInt(dealerID[i].substring(1));
        }

        //sorted least to greatest
        Arrays.sort(pNums);
        Arrays.sort(dNums);

        //check for ace
        if(pNums[0] ==1 && dNums[0] != 1){
            winner = "player";
            highAce = true;
        }else if(dNums[0] ==1 && pNums[0] != 1){
            winner = "dealer";
            highAce = true;
        }

        //Check highest card except for ace
        if(!highAce) {
            if (pNums[2] > dNums[2]) {
                winner = "player";
            } else if (dNums[2] > pNums[2]) {
                winner = "dealer";

                //Check second-highest card
            } else if (pNums[2] == dNums[2]) {
                if (pNums[1] > dNums[1]) {
                    winner = "player";
                } else if (dNums[1] > pNums[1]) {
                    winner = "dealer";

                    //Check third-highest card
                } else if (pNums[1] == dNums[1]) {
                    if (pNums[0] > dNums[0]) {
                        winner = "player";
                    } else if (dNums[0] > pNums[0]) {
                        winner = "dealer";
                    }
                }
            }
        }
        //all cards the same
        if(dNums[0] == pNums[0] && dNums[1] == pNums[1] && dNums[2] == pNums[2]){
            winner = "draw";
        }


        return winner;
    }

    String pairWinner(String[] playerID, String[] dealerID){
        String winner = "";
        int dNums[] = new int[3];
        int pNums[] = new int[3];

        int dPairVal = 0;
        int pPairVal = 0;

        for(int i = 0; i < 3; i++){
            pNums[i] = Integer.parseInt(playerID[i].substring(1));
            dNums[i] = Integer.parseInt(dealerID[i].substring(1));
        }

        if(pNums[0] == pNums[1]){
            pPairVal = pNums[0];

        }else if(pNums[1] == pNums[2]){
            pPairVal = pNums[1];

        }else if(pNums[0] == pNums[2]){
            pPairVal = pNums[0];
        }

        if(dNums[0] == dNums[1]){
            dPairVal = dNums[0];

        }else if(dNums[1] == dNums[2]){
            dPairVal = dNums[1];

        }else if(dNums[0] == dNums[2]){
            dPairVal = dNums[0];
        }

        if(pPairVal == 1) pPairVal +=100; //ace pair
        if(dPairVal == 1) dPairVal +=100; //ace pair

        if(pPairVal > dPairVal) winner = "player";
        else if(dPairVal > pPairVal) winner ="dealer";
        else if(dPairVal == pPairVal){
            dPairVal = 0;
            pPairVal = 0;

            if(pNums[0] != pNums[1]){
                pPairVal = pNums[0];

            }else if(pNums[1] != pNums[2]){
                pPairVal = pNums[1];

            }else if(pNums[0] != pNums[2]){
                pPairVal = pNums[0];
            }

            if(dNums[0] != dNums[1]){
                dPairVal = dNums[0];

            }else if(dNums[1] != dNums[2]){
                dPairVal = dNums[1];

            }else if(dNums[0] != dNums[2]){
                dPairVal = dNums[0];
            }
            if(pPairVal == 1) pPairVal +=100; //ace pair
            if(dPairVal == 1) dPairVal +=100; //ace pair

            if(pPairVal > dPairVal) winner = "player";
            else if(dPairVal > pPairVal) winner ="dealer";
            else if(dPairVal == pPairVal) winner = "draw";
        }

        return winner;
    }

    public String straightWinner(String[] playerID, String[] dealerID){
        String winner = "";
        boolean highFlush = false;
        int dNums[] = new int[3];
        int pNums[] = new int[3];
        for(int i = 0; i < 3; i++){
            pNums[i] = Integer.parseInt(playerID[i].substring(1));
            dNums[i] = Integer.parseInt(dealerID[i].substring(1));
        }
        Arrays.sort(pNums);
        Arrays.sort(dNums);

        //check for queen, king, ace straight
        if(pNums[0] == 1 && pNums[1] == 12 && pNums[2] == 13 && dNums[0] != 1){
            winner = "player";
            highFlush = true;
        } else if(dNums[0] == 1 && dNums[1] == 12 && dNums[2] == 13 && pNums[0] != 1){
            winner = "dealer";
            highFlush = true;
        }

        //check of all other straights
        if(highFlush == false) {
            if (pNums[0] > dNums[0]) {
                winner = "player";
            } else if (dNums[0] > pNums[0]) {
                winner = "dealer";
            } else if (dNums[0] == pNums[0]) {
                winner = "draw";
            }
        }

        return winner;
    }

    public String higher3Kind(String[] playerID, String[] dealerID){
        String winner = "";
        boolean highKind = false;
        int dNums[] = new int[3];
        int pNums[] = new int[3];
        for(int i = 0; i < 3; i++){
            pNums[i] = Integer.parseInt(playerID[i].substring(1));
            dNums[i] = Integer.parseInt(dealerID[i].substring(1));
        }
        Arrays.sort(pNums);
        Arrays.sort(dNums);

        //check for 3 aces
        if(pNums[0] == 1 && dNums[0] != 1){
            winner = "player";
            highKind = true;
        } else if(dNums[0] == 1 && pNums[0] != 1){
            winner = "dealer";
            highKind = true;
        }

        //check for all other 3 cards
        if(!highKind) {
            if (pNums[0] > dNums[0]) {
                winner = "player";
            } else if (dNums[0] > pNums[0]) {
                winner = "dealer";
            } else if (pNums[0] == dNums[0]) {
                winner = "draw";
            }
        }

        return winner;
    }

    int pairPlusBonus(String[] playerID, int wager){
        int hand = 0;
        hand = determineHand(playerID);
        if(hand == 1){
            wager = 0;
        } else if(hand == 2){ //pair
            wager += wager;
        }else if(hand ==3){ //flush
            wager = wager *3;
        }else if(hand ==4){ //straight
            wager = wager *6;
        }else if(hand == 5){ //three of a kind
            wager = wager * 30;
        }else if(hand ==6){ //straight flush
            wager = wager *40;
        }


        return wager;
    }

    boolean queenHigh(String dealerID[]){
        boolean ace = false;
        int dNums[] = new int[3];
        int hand = 0;
        hand = determineHand(dealerID);
        if(hand == 1) {

            for (int i = 0; i < 3; i++) {
                dNums[i] = Integer.parseInt(dealerID[i].substring(1));
            }
            Arrays.sort(dNums);

            if(dNums[0] == 1) return true;

            else if (dNums[2] < 12) {
                return false;
            }
        }
        return true;
    }
}

