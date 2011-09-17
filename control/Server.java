

package control;

import game.GameModel;

import java.util.*;
import java.io.*;
import java.net.*;



/**
 * A master receives events from a slave connection via a socket.
 * These events are registered with the game world. The master connection is also
 * responsible for transmitting information to the slave about the current board
 * state.
 */
public final class Server extends Thread {
	private final GameModel model;
	private final int uid;
	private final Socket socket;

	public Server(Socket socket, int uid, GameModel model) {
		this.model = model;	
		this.socket = socket;
		this.uid = uid;
	}
	
	public void run() {		
		try {
			InputStreamReader input = new InputStreamReader(socket.getInputStream());
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
			// First, write the period to the stream				
//			output.write(uid);
//			output.writeInt(board.width());			
//			output.writeInt(board.height());
//			output.write(board.wallsToByteArray());
			boolean exit=false;
			while(!exit) {
				try {
					BufferedReader rd = new BufferedReader(input);
					String temp;
					if((temp = rd.readLine()) != null) {
						
						System.out.println(temp);
						/**
						 * Insert game altering here
						 */
					} 
					BufferedWriter bw = new BufferedWriter(output);
					// Now, broadcast the state of the board to client
					//Update to game array 
					String update = "You're gay\n";
					bw.write(update);
					bw.flush();
					Thread.sleep(100);
				} catch(InterruptedException e) {					
				}
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");
//			board.disconnectPlayer(uid);
		}		
	}
}
