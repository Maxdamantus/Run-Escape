package client;

import game.PlayerMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ui.isometric.IsoInterface;
import ui.isometric.mock.IsoGameLogicMock;
import util.Position;

import clientinterface.Conversions;
import clientinterface.GameLogic;
import clientinterface.GameThing;

/**
 * Main class for the client.
 * @author greenwthom
 *
 */
public class Client implements GameLogic{
	private String host;
	private int port;
	private Socket skt;
	private InputStreamReader in;
	private BufferedReader reader;
	private OutputStreamWriter out;
	private BufferedWriter writer;
	private IsoInterface view;
	private game.GameModel sgm = new game.GameModel();
	
	public static void main(String[] args) {
		String host = "localhost";
		int port = 32768;
		if (args.length > 0) {
			host = args[0];
		}
		else if (args.length == 2) {
			port = Integer.parseInt(args[1]);
		}
		Client client = new Client(host, port);
		
	}
	
	public Client(String host, int port) {
		boolean debug = true;
		try {
			//creating GUI
			game.GameThing tile = new game.things.GroundTile(sgm);
			sgm.level(0).put(new Position(5, 0), tile);
			sgm.level(0).put(new Position(5, 1), new game.things.GroundTile(sgm, "ground_grey_water_two_sides"));
			view = new IsoInterface("IsoTest", Conversions.fromServerGameModel(sgm), this);
			view.show();
							
			
			//creating socket and readers/writers
			skt = new Socket(host,port);
			if (debug) System.out.println("connected to "+host+" on 32768");
			in = new InputStreamReader(skt.getInputStream());
			reader = new BufferedReader(in);
			out = new OutputStreamWriter(skt.getOutputStream());
			writer = new BufferedWriter(out);
			
			//test code
			UpdateThread network = new UpdateThread(reader, writer);
			
			writer.write("Handshake1\n");
			writer.write("Handshake3\n");
			writer.write("Handshake2\n");
			network.start();
			writer.write("still works here\n");
			writer.flush();

			
		
		
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	@Override
	public void performActionOn(String action, GameThing object) {
		try {
			writer.write(action+"\n"+object.gid()+"\n");
		} catch (IOException e) {
			System.out.println("Bother, the BufferedWriter broke");
		}
		
	}
}