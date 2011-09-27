package client;

import game.PlayerMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ui.isometric.IsoInterface;
import ui.isometric.mock.IsoGameLogicMock;
import util.*;

import game.GameLogic;
import game.GameThing;

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
	private game.GameWorld sgm = new game.GameWorld();
	
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
			sgm.level(0).location(new Position(5, 0), Direction.NORTH).put(tile);
			sgm.level(0).location(new Position(5, 1), Direction.NORTH).put(new game.things.GroundTile(sgm, "ground_grey_water_two_sides"));
			view = new IsoInterface("IsoTest", sgm, this);
			view.show();
							
			
			//creating socket and readers/writers
			skt = new Socket(host,port);
			if (debug) System.out.println("connected to "+host+" on 32768");
			in = new InputStreamReader(skt.getInputStream());
			reader = new BufferedReader(in);
			out = new OutputStreamWriter(skt.getOutputStream());
			writer = new BufferedWriter(out);
			
			//test code
			UpdateThread network = new UpdateThread(reader, this.getUI());
			
			writer.write("uid Bob\n");
			network.start();
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
			String send = action+"\n"+object.gid()+"\n";
			System.out.print(send);
			writer.write(send);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Bother, the BufferedWriter broke");
		}
		
	}
	
	public IsoInterface getUI() {
		return view;
	}
}
