
package server;

import game.GameWorld;
import game.GameWorld.DeltaWatcher;
import game.WorldDelta;

import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import server.*;
import ui.isometric.IsoInterface;
import ui.isometric.builder.IsoInterfaceWorldBuilder;
import ui.isometric.mock.ClientMessageHandlerMock;
import util.Direction;
import util.Position;




public class Main{
	private static final int CLOCK_TIME = 20;
	private static final int BROADCAST_TIME = 5;
	private static JFileChooser fc;
	private static String filename = null;
	private static int returnVal;
	
	public static void main(String[] args) throws IOException {
		//Main Class for server	
		boolean fromSave = false;		
		
		int gameClock = CLOCK_TIME;
		int broadcastClock = BROADCAST_TIME;
		int port = 32765; // default
		String savefile;
		String choice = "null";
		while(!(choice.equals("NewGame") || choice.equals("LoadGame"))){
			Object[] possibilities = {"NewGame", "LoadGame"};
			choice = (String)JOptionPane.showInputDialog(
			                    null,
			                    "Start new, or load from file?",
			                    "Customized Dialog",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "NewGame");
		}
		if(choice.equals("LoadGame")){
			fromSave = true;
			while(filename == null){
			fc = new JFileChooser();
			returnVal = fc.showOpenDialog(new JFrame());
			File file = fc.getSelectedFile();
	        System.out.println("Opening: " + file.getName());
	        filename = file.getAbsolutePath();
			}
		}
		if(fromSave){
			GameWorld model = null;
			//will eventually do from File -> XML to world
			runServer(port, model);
		}
		else{
			
			GameWorld model = defaultworld();
			IsoInterfaceWorldBuilder view = new IsoInterfaceWorldBuilder("World Builder", model, new ClientMessageHandlerMock());
			view.show();
			runServer(port,model);
			
		}		
		System.exit(0);
	}
	

	private static void runServer(int port, GameWorld game) {		
		int uid = 0;
		int nclients = 2;
		Clock timer = new Clock(0);
		timer.start();
		// Listen for connections
		System.out.println("GAME SERVER LISTENING ON PORT " + port);
		try {
			final ArrayList<Server> connections = new ArrayList<Server>(10);
			game.addDeltaWatcher(new DeltaWatcher(){
				@Override
				public void delta(WorldDelta d) {
					for(Server svr : connections){
						svr.addDelta(d);
					}
				}	
			});
			// Now, we await connections.
			ServerSocket ss = new ServerSocket(port);
			while (1 == 1) {
				// 	Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());				
				final Server newSer = new Server(s,uid,game, timer);
				connections.add(newSer);
				// MaxZ's code: send initial state
				game.allDeltas(new DeltaWatcher(){
					public void delta(WorldDelta d){
						newSer.addDelta(d);
					}
				});
				newSer.start();
				uid++;; //this will add players unique identifier in future.
				ArrayList<Server> remove = new ArrayList<Server>();
				//check for dead clients
				if(connections.size() == nclients){
					System.out.println("A CLIENT HAS CONNECTED --- GAME BEGINS");
					runGame(connections, game);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME ENDS");
					return; // done
				}
			}
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} 
	}



	/**
	 * Check whether or not there is at least one connection alive.
	 * 
	 * @param connections
	 * @return
	 */
	private static boolean atleastOneConnection(ArrayList<Server> connections) {
		for (Server m : connections) {
			if (m.isAlive()) {
				return true;
			}			
		}
		return false;
	}
	
	private static void runGame(ArrayList<Server> connections, GameWorld game){
		while(atleastOneConnection(connections)){
			Thread.yield();		
		}
	}
	
	
	private static void pause(int delay) {
		try {
			Thread.sleep(delay);
		} 
		catch(InterruptedException e){			
		}
	}
	
	public static GameWorld defaultworld(){
		game.GameWorld sgm = new GameWorld();
		game.GameThing tile = new game.things.GroundTile(sgm);
		sgm.level(0).location(new Position(5, 0), Direction.NORTH).put(tile);
		sgm.level(0).location(new Position(5, 1), Direction.NORTH).put(new game.things.GroundTile(sgm, "ground_grey_water_two_sides", true));
		return sgm;
	}
		
}
