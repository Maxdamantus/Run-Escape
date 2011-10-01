
package server;

import game.GameWorld;
import game.GameWorld.DeltaWatcher;
import game.WorldDelta;

import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import data.Database;


import ui.isometric.builder.IsoInterfaceWorldBuilder;
import ui.isometric.mock.ClientMessageHandlerMock;
import util.Direction;
import util.Position;




public class Server{
	private static final int CLOCK_TIME = 20;
	private static final int BROADCAST_TIME = 5;
	private static JFileChooser fc;
	private static String filename = null;
	private static int returnVal;
	public static final int DEFAULT_PORT = 32765;
	
	private ArrayList<ServerThread> connections = new ArrayList<ServerThread>(10);
	
	public void run() throws IOException {
		//Main Class for server	
		boolean fromSave = false;		
		
		int port = DEFAULT_PORT;
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
	//		GameWorld model = Database.xmlToTree(Database.newDocumentFromFile(file));
	//		runServer(port, model);
		}
		else{
			
			GameWorld model = defaultworld();
			IsoInterfaceWorldBuilder view = new IsoInterfaceWorldBuilder("World Builder", model, new ClientMessageHandlerMock());
			view.show();
			runServer(port,model);
			
		}
		System.exit(0);
	}
	

	private void runServer(int port, GameWorld game) {		
		int uid = 0;
		Clock timer = new Clock(0);
		timer.start();
		// Listen for connections
		System.out.println("GAME SERVER LISTENING ON PORT " + port);
		try {
			game.addDeltaWatcher(new DeltaWatcher(){
				@Override
				public void delta(final WorldDelta d) {
					toAllPlayers(new ClientMessenger() {
						@Override
						public void doTo(ServerThread client) {
							client.addDelta(d);
						}
					});
				}	
			});
			// Now, we await connections.
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				// 	Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());				
				final ServerThread newSer = new ServerThread(s,uid,game, timer, this);
				connections.add(newSer);
				// MaxZ's code: send initial state
				game.allDeltas(new DeltaWatcher(){
					public void delta(WorldDelta d){
						newSer.addDelta(d);
					}
				});
				newSer.start();
				uid++;; //this will add players unique identifier in future.
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
	private boolean atleastOneConnection(ArrayList<ServerThread> connections) {
		for (ServerThread m : connections) {
			if (m.isAlive()) {
				return true;
			}			
		}
		return false;
	}
	
	private void runGame(ArrayList<ServerThread> connections, GameWorld game){ // We dont actually need this if we want the server to run forever, if you just let main return the other threads will still run
		while(atleastOneConnection(connections)){
			Thread.yield();	
			pause(10);
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
		// make a spiral instead
		int width = 20;
		game.Level.Location ll = sgm.level(0).location(new Position(0, 0), Direction.NORTH);
		ll.put(new game.things.Door(sgm, "wall_brown_1_door_closed", "wall_brown_1_door_open", false));
		for(int x = 0; x < width; x++){
			for(int y = 0; y < x; y++){
				ll.put(new game.things.GroundTile(sgm, "ground_grey_water_two_sides", true));
				ll = ll.next(ll.direction());
			}
			ll = ll.rotate(Direction.WEST);
		}
		
		return sgm;
	}


	public static interface ClientMessenger {
		public void doTo(ServerThread client);
	}
	
	public void toAllPlayers(ClientMessenger clientMessenger) {
		for(ServerThread t : connections) {
			clientMessenger.doTo(t);
		}
	}
		
}
