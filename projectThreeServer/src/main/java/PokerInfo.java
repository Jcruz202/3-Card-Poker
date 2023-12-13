import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class PokerInfo implements Serializable {
    String anteMessage;
    String pairPlusMessage;
    String wagerMessage;
    String queenHighMessage;
    String message;
    String[] cardsPicked = new String[3];

    String[] playerID = new String[3];

    String[] dealerID = new String[3];

    String[] dealerHand = new String[3];
    int anteBet;
    int pairPlus = 0;

    int lost = 0;

    int playBet;

    int total =0;
    int portNumber;
    int ipAddressNumber;
    String tempDealerHand;

    boolean queenHigh = false;


    String hand;
    int dHand = 0;

    String winner = "";


    public String sendMe(){
        return "I am sent";
    }


    public PokerInfo(){};

}