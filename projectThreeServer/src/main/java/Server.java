import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server {

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	ServerSocket mysocket;
	boolean serverOn = false;

	Server(Consumer<Serializable> call) {
		callback = call;
		server = new TheServer();
		server.start();
	}

	public void stop() {
		//try catch
		try {
			server.stopServer();
		}
		catch(Exception e) {
			callback.accept("Server socket did not launch");
		}
	}

	public void start(){
		server = new TheServer();
		serverOn = true;
	}


	public class TheServer extends Thread{

		public void run() {

			try{
				mysocket = new ServerSocket(5555);
				System.out.println("Server is waiting for a client!");


				//will show only to the server
				while(true) {
					PokerInfo data = new PokerInfo();
					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept(data);
					clients.add(c);
					c.start();
					count++;
				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
		public void stopServer()throws Exception{
			serverOn = false;
			mysocket.close();
		}

	}


	class ClientThread extends Thread{


		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		PokerInfo data;

		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
			data = new PokerInfo();

		}

		public void updateClients(PokerInfo data) {
			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					t.out.writeObject(data);
				}
				catch(Exception e) {}
			}
		}

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			updateClients(data);

			//will show for both the client and server
			while(true) {
				try {
					PokerInfo data = (PokerInfo) in.readObject();
					GameLogic gl = new GameLogic();

					callback.accept(data);
					int[] nums = new int[3];
					int[] dNums = new int[3];
					String[] cardsID = new String[3];
					String[] dealerID = new String[3];

					nums = gl.generateRandomNumbers(nums);
					dNums = gl.generateRandomNumbers(dNums);

					while(Arrays.equals(nums, dNums)){
						dNums = gl.generateRandomNumbers(dNums);
					}
					cardsID= gl.cardsID(nums, cardsID);
					dealerID= gl.cardsID(dNums, dealerID);
					data.playerID = gl.cardsID(nums, cardsID);
					data.dealerID = gl.cardsID(dNums, dealerID);

					data.cardsPicked = gl.dealCards(data.cardsPicked, nums);
					data.dealerHand = gl.dealCards(data.dealerHand, dNums);

					int hand = 0;
					int dHand = 0;
					dHand = gl.determineHand(dealerID);
					hand = gl.determineHand(cardsID);
					data.tempDealerHand = gl.handType(dHand);
					data.hand = gl.handType(hand);
					data.winner = gl.determineWinner(cardsID, dealerID);
					int p = data.pairPlus;
					data.pairPlus = gl.pairPlusBonus(cardsID, data.pairPlus);
					if(data.winner.equals("player")){
						if(data.pairPlus == 0)data.lost = p;
						else data.lost = 0;
						data.anteBet*=2;
						data.playBet*=2;
					}
					else if(data.winner.equals("dealer")) {
						if(data.pairPlus == 0)data.lost = data.anteBet + data.playBet + p;
						else data.lost = data.anteBet + data.playBet;
						data.anteBet = 0;
						data.playBet =0;

					}

					data.total = data.pairPlus + data.anteBet + data.playBet;
					data.queenHigh = gl.queenHigh(dealerID);


					if(!data.queenHigh) data.queenHighMessage = "Dealer does not have at least queen high";
					if(data.pairPlus == 0) data.pairPlusMessage = "Player " + count + " lost Pair Plus"; else data.pairPlusMessage = "Player " + count + " won Pair Plus";
					if(data.winner.equals("player")) data.message = "Player " + count + " beats the dealer"; else data.message = "Player " + count + " lost to dealer";
					out.writeObject(data);

				}
				catch(Exception e) {
					data.message = "OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!";
					callback.accept(data);
					data.message = "Client #"+count+" has left the server!";
					updateClients(data);
					clients.remove(this);
					break;
				}
			}
		}//end of run


	}//end of client thread
}