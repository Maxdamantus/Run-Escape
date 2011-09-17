

package control;

import java.util.*;
import java.io.*;
import java.net.*;

import Game.game.*;


/**
 * A master receives events from a slave connection via a socket.
 * These events are registered with the game world. The master connection is also
 * responsible for transmitting information to the slave about the current board
 * state.
 */
public final class Master extends Thread {
	private final World world;
	private final int broadcastClock;
	private final int uid;
	private final Socket socket;

	public Master(Socket socket, int uid, int broadcastClock, Board board) {
		this.world = board;	
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}
	
	public void run() {		
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			// First, write the period to the stream				
			output.writeInt(uid);
			output.writeInt(board.width());			
			output.writeInt(board.height());
			output.write(board.wallsToByteArray());
			boolean exit=false;
			while(!exit) {
				try {
					
					if(input.available() != 0) {
						
						// read event and update Game
						/**
						 * Insert game altering here
						 */
					} 
					
					// Now, broadcast the state of the board to client
					//Update to game array 
					output.writeInt(update.length);
					output.write(update);
					output.flush();
					Thread.sleep(broadcastClock);
				} catch(InterruptedException e) {					
				}
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");
			board.disconnectPlayer(uid);
		}		
	}
}
