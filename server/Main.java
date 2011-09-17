
package server;

import game.GameModel;

import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import server.*;




public class Main {
	private static final int CLOCK_TIME = 20;
	private static final int BROADCAST_TIME = 5;
	
	public static void main(String[] args) throws IOException {
		//Main Class for server	
		String filename = null;
		boolean fromSave = false;		
	
		int gameClock = CLOCK_TIME;
		int broadcastClock = BROADCAST_TIME;
		int port = 32768; // default
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
			savefile = "test";
		}
		
		if(fromSave){
			GameModel model = null;
			runServer(port, model);
		}
		else{
			GameModel model = null;
			runServer(port,model);
		}		
		System.exit(0);
	}
	

	private static void runServer(int port, GameModel game) {		
		int uid = 0;
		boolean started = false;
		// Listen for connections
		System.out.println("GAME SERVER LISTENING ON PORT " + port);
		try {
			ArrayList<Server> connections = new ArrayList<Server>(10);
			// Now, we await connections.
			ServerSocket ss = new ServerSocket(port);
			while (1 == 1) {
				// 	Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());				
				
				connections.add(uid, new Server(s,uid,game));
				connections.get(uid).start();
				uid++;; //this will add players unique identifier in future.
				started = true;
				System.out.println("A CLIENT HAS CONNECTED --- GAME BEGINS");
				ArrayList<Server> remove = new ArrayList<Server>();
				//check for dead clients
				for(Server ser : connections){
					if(ser.getExit() == true){
						remove.add(ser);
					}
				}
				//remove dead clients
				for(Server ser : remove){
					connections.remove(ser);
				}
				if(connections.isEmpty() && started) {
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
	private static boolean atleastOneConnection(Server... connections) {
		for (Server m : connections) {
			if (m.isAlive()) {
				return true;
			}			
		}
		return false;
	}
	
	private static void pause(int delay) {
		try {
			Thread.sleep(delay);
		} 
		catch(InterruptedException e){			
		}
	}		
		
}
