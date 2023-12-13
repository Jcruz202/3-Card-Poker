import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;
	PokerInfo data;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){

		callback = call;
	}

	public void run() {

		try {
			socketClient= new Socket("127.0.0.1", 5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {
			try {
				PokerInfo data = (PokerInfo) in.readObject();
				data.message = "Welcome to the game";
				callback.accept(data);
			}
			catch(Exception e) {}
		}
	}

	public void send(PokerInfo data) {

		try {
			out.writeObject(data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
